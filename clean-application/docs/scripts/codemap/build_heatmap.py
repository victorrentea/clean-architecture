#!/usr/bin/env python3
"""Build per-file codemap data.

Inputs:
  - /tmp/claude/bug_issues.txt : one issue number per line
  - REPO_DIR git history       : walked via subprocess
  - REPO_DIR working tree      : for current bytes/lines

Output:
  - OUT_DIR/codemap.tsv : path \t bytes \t lines \t commits \t bug_commits \t commits_per_kloc \t bugs_per_kloc \t bugs_per_commit
    Filtered to non-test .java files present in current tree.
    Sorted by bugs_per_kloc desc.
"""
import os
import re
import subprocess
import sys
from collections import defaultdict

_here = os.path.dirname(os.path.abspath(__file__))
def _git_root(start):
    try:
        return subprocess.check_output(["git", "-C", start, "rev-parse", "--show-toplevel"],
                                       text=True, stderr=subprocess.DEVNULL).strip()
    except Exception:
        return start
REPO_DIR = os.path.abspath(os.environ.get("HEATMAP_REPO") or _git_root(_here))
OUT_DIR = os.path.abspath(os.environ.get("HEATMAP_OUT") or REPO_DIR)
os.makedirs(OUT_DIR, exist_ok=True)
EXTRA_PRUNE = {d for d in os.environ.get("HEATMAP_PRUNE", "").split(",") if d}
BUG_FILE = os.environ.get("HEATMAP_BUG_FILE", os.path.join(OUT_DIR, "bug_issues.txt"))
# Optional: a regex on the commit SUBJECT flags a bug-fix commit directly
# (used for repos like petclinic that have no GitHub bug labels but use
# conventional commits, e.g. HEATMAP_BUG_COMMIT_REGEX="^(fix|bugfix)(\\(|:|!)").
_bug_subj_src = os.environ.get("HEATMAP_BUG_COMMIT_REGEX")
BUG_SUBJECT_RE = re.compile(_bug_subj_src, re.IGNORECASE) if _bug_subj_src else None
OUT_FILE = os.path.join(OUT_DIR, "codemap.tsv")
COMPLEXITY_FILE = os.path.join(OUT_DIR, "complexity-per-file.tsv")
FANIO_FILE = os.path.join(OUT_DIR, "fanio-per-file.tsv")

# load bug issue set (optional — absent for repos without a bug-issue list)
bug_ids = set()
if os.path.exists(BUG_FILE):
    with open(BUG_FILE) as f:
        bug_ids = {int(line.strip()) for line in f if line.strip()}
print(f"loaded {len(bug_ids)} bug/regression issue numbers", file=sys.stderr)
if BUG_SUBJECT_RE:
    print(f"also flagging bug commits via subject regex: {_bug_subj_src!r}", file=sys.stderr)

# regex for gh-NNN refs (case-insensitive). also accept "#NNN" when paired with closes/fixes.
GH_RE = re.compile(r'\bgh-(\d+)\b', re.IGNORECASE)
HASH_RE = re.compile(r'(?:close[sd]?|fix(?:e[sd])?|resolve[sd]?)\s+#(\d+)\b', re.IGNORECASE)

# walk git log
SENT = "___COMMIT___"
FSENT = "___FILES___"
fmt = f"{SENT}%n%H%n%s%n%b%n{FSENT}"
proc = subprocess.Popen(
    ["git", "-C", REPO_DIR, "log", "--no-merges", "--name-only", f"--pretty=format:{fmt}"],
    stdout=subprocess.PIPE, text=True, errors="replace",
)
assert proc.stdout

commits_per_file = defaultdict(int)
bug_commits_per_file = defaultdict(int)
total_commits = 0
total_bug_commits = 0

buf = []
state = "expect_sent"
sha = None
msg_lines = []
file_lines = []

def flush_commit():
    global total_commits, total_bug_commits
    if sha is None:
        return
    msg = "\n".join(msg_lines)
    subject = msg_lines[0] if msg_lines else ""
    refs = set(int(m) for m in GH_RE.findall(msg))
    for m in HASH_RE.findall(msg):
        refs.add(int(m))
    is_bug = bool(refs & bug_ids)
    if BUG_SUBJECT_RE and BUG_SUBJECT_RE.search(subject):
        is_bug = True
    total_commits += 1
    if is_bug:
        total_bug_commits += 1
    for fp in file_lines:
        if not fp:
            continue
        commits_per_file[fp] += 1
        if is_bug:
            bug_commits_per_file[fp] += 1

for raw in proc.stdout:
    line = raw.rstrip("\n")
    if line == SENT:
        flush_commit()
        sha = None
        msg_lines = []
        file_lines = []
        state = "expect_sha"
        continue
    if state == "expect_sha":
        sha = line
        state = "in_msg"
        continue
    if state == "in_msg":
        if line == FSENT:
            state = "in_files"
            continue
        msg_lines.append(line)
        continue
    if state == "in_files":
        if line:  # skip blanks between commits
            file_lines.append(line)
        continue
flush_commit()
proc.wait()

print(f"walked {total_commits} commits, {total_bug_commits} flagged as bug-linked", file=sys.stderr)
print(f"touched {len(commits_per_file)} distinct file paths (across all history)", file=sys.stderr)

# collect current java files (non-test) + their bytes + line counts
java_files = []
for root, dirs, files in os.walk(REPO_DIR):
    # prune hidden + test dirs
    parts = root.split(os.sep)
    if any(p == ".git" for p in parts) or any(p in EXTRA_PRUNE for p in parts):
        dirs[:] = []
        continue
    skip = False
    for i in range(len(parts) - 1):
        if parts[i] == "src" and parts[i+1] in ("test", "testFixtures"):
            skip = True
            break
    if skip:
        dirs[:] = []
        continue
    for fn in files:
        if fn.endswith(".java"):
            java_files.append(os.path.join(root, fn))

print(f"found {len(java_files)} current non-test java files", file=sys.stderr)

# load cognitive complexity per file
complexity = {}
if os.path.exists(COMPLEXITY_FILE):
    with open(COMPLEXITY_FILE) as f:
        next(f)  # header
        for line in f:
            parts = line.rstrip("\n").split("\t")
            if len(parts) >= 2:
                complexity[parts[0]] = int(parts[1])
    print(f"loaded complexity for {len(complexity)} files", file=sys.stderr)
else:
    print(f"WARN: {COMPLEXITY_FILE} not found, complexity will be 0", file=sys.stderr)

# load fan_in / fan_out per file
fan_in_map, fan_out_map = {}, {}
if os.path.exists(FANIO_FILE):
    with open(FANIO_FILE) as f:
        next(f)
        for line in f:
            parts = line.rstrip("\n").split("\t")
            if len(parts) >= 3:
                fan_in_map[parts[0]] = int(parts[1])
                fan_out_map[parts[0]] = int(parts[2])
    print(f"loaded fan-in/out for {len(fan_in_map)} files", file=sys.stderr)
else:
    print(f"WARN: {FANIO_FILE} not found, fan-in/out will be 0", file=sys.stderr)

rows = []
for ap in java_files:
    rel = os.path.relpath(ap, REPO_DIR)
    try:
        sz = os.path.getsize(ap)
        with open(ap, "rb") as f:
            lines = sum(1 for _ in f)
    except OSError:
        continue
    commits = commits_per_file.get(rel, 0)
    bug_commits = bug_commits_per_file.get(rel, 0)
    cog = complexity.get(rel, 0)
    fi = fan_in_map.get(rel, 0)
    fo = fan_out_map.get(rel, 0)
    kloc = lines / 1000.0 if lines else 0
    commits_per_kloc = (commits / kloc) if kloc else 0
    bugs_per_kloc = (bug_commits / kloc) if kloc else 0
    bugs_per_commit = (bug_commits / commits) if commits else 0
    cog_per_kloc = (cog / kloc) if kloc else 0
    rows.append((rel, sz, lines, commits, bug_commits, commits_per_kloc, bugs_per_kloc, bugs_per_commit, cog, cog_per_kloc, fi, fo))

rows.sort(key=lambda r: (r[6], r[4], r[3]), reverse=True)

with open(OUT_FILE, "w") as f:
    f.write("path\tbytes\tlines\tcommits\tbug_commits\tcommits_per_kloc\tbugs_per_kloc\tbugs_per_commit\tcognitive_complexity\tcomplexity_per_kloc\tfan_in\tfan_out\n")
    for r in rows:
        f.write(f"{r[0]}\t{r[1]}\t{r[2]}\t{r[3]}\t{r[4]}\t{r[5]:.2f}\t{r[6]:.2f}\t{r[7]:.3f}\t{r[8]}\t{r[9]:.2f}\t{r[10]}\t{r[11]}\n")

print(f"wrote {len(rows)} rows to {OUT_FILE}", file=sys.stderr)
