#!/usr/bin/env python3
"""Compute fan-in and fan-out per non-test Java file in spring-framework.

Definitions (internal coupling — JDK/3rd-party deps not counted):
  fan_out = # distinct repo classes this file references
            (via imports + same-package siblings whose simple name appears in body)
  fan_in  = # files in the repo that reference any class declared in this file

Output: /Users/victorrentea/workspace/spring-framework/fanio-per-file.tsv
  file \t fan_in \t fan_out
"""
import os
import re
import sys
from collections import defaultdict

import subprocess
_here = os.path.dirname(os.path.abspath(__file__))
def _git_root(start):
    try:
        return subprocess.check_output(["git", "-C", start, "rev-parse", "--show-toplevel"],
                                       text=True, stderr=subprocess.DEVNULL).strip()
    except Exception:
        return start
REPO = os.path.abspath(os.environ.get("HEATMAP_REPO") or _git_root(_here))
OUT_DIR = os.path.abspath(os.environ.get("HEATMAP_OUT") or REPO)
EXTRA_PRUNE = {d for d in os.environ.get("HEATMAP_PRUNE", "").split(",") if d}
CLASS_TSV = os.path.join(OUT_DIR, "complexity-per-class.tsv")
OUT = os.path.join(OUT_DIR, "fanio-per-file.tsv")

PACKAGE_RE = re.compile(r"^\s*package\s+([\w.]+)\s*;", re.MULTILINE)
IMPORT_RE = re.compile(r"^\s*import\s+(?:static\s+)?([\w.]+)(?:\.\*)?\s*;", re.MULTILINE)


def load_class_map():
    """Return dict: fqn (dot form, outer class only) -> file path. Plus per-package class list."""
    fqn_to_file = {}
    pkg_to_classes = defaultdict(list)  # 'org.springframework.x' -> [(simple_name, file), ...]
    with open(CLASS_TSV) as f:
        next(f)
        for line in f:
            parts = line.rstrip("\n").split("\t")
            if len(parts) < 2:
                continue
            file, fqn = parts[0], parts[1]
            # fqn uses '$' for nesting; flatten to outer dot-form for import resolution
            if not fqn or "." not in fqn:
                continue
            outer_dollar = fqn.split("$", 1)[0]  # outer only
            outer_dot = outer_dollar
            # also register the inner (some imports point at inner classes)
            inner_dot = fqn.replace("$", ".")
            for key in {outer_dot, inner_dot}:
                fqn_to_file.setdefault(key, file)
            pkg = outer_dot.rsplit(".", 1)[0]
            simple = outer_dot.rsplit(".", 1)[1]
            pkg_to_classes[pkg].append((simple, file))
    return fqn_to_file, pkg_to_classes


def list_java_files():
    out = []
    for root, dirs, files in os.walk(REPO):
        parts = root.split(os.sep)
        if any(p == ".git" for p in parts) or any(p in EXTRA_PRUNE for p in parts):
            dirs[:] = []
            continue
        if any(parts[i] == "src" and parts[i + 1] in ("test", "testFixtures") for i in range(len(parts) - 1)):
            dirs[:] = []
            continue
        for fn in files:
            if fn.endswith(".java"):
                out.append(os.path.join(root, fn))
    return out


def main():
    fqn_to_file, pkg_to_classes = load_class_map()
    print(f"loaded {len(fqn_to_file)} FQN entries across {len(pkg_to_classes)} packages", file=sys.stderr)

    java_files = list_java_files()
    print(f"scanning {len(java_files)} java files", file=sys.stderr)

    fan_out = defaultdict(set)  # file -> set of target files

    for ap in java_files:
        rel = os.path.relpath(ap, REPO)
        try:
            with open(ap, encoding="utf-8", errors="replace") as f:
                src = f.read()
        except OSError:
            continue
        pkg_m = PACKAGE_RE.search(src)
        pkg = pkg_m.group(1) if pkg_m else None

        targets = set()
        for imp in IMPORT_RE.findall(src):
            # static import targets a method/field; the class is everything before the last dot
            # but our regex already trimmed the .* wildcard; for static, the last segment is member name
            # we don't reliably distinguish here — try the full path first, then strip last segment
            for candidate in (imp, imp.rsplit(".", 1)[0] if "." in imp else imp):
                tf = fqn_to_file.get(candidate)
                if tf and tf != rel:
                    targets.add(tf)
                    break

        # same-package siblings: regex-search for any sibling simple name (word boundary)
        if pkg and pkg in pkg_to_classes:
            siblings = [(s, f) for (s, f) in pkg_to_classes[pkg] if f != rel]
            if siblings:
                # build a single regex with all sibling names for one pass
                names = list({s for (s, _) in siblings})
                if names:
                    pattern = r"\b(?:" + "|".join(re.escape(n) for n in names) + r")\b"
                    found = set(re.findall(pattern, src))
                    for s, tf in siblings:
                        if s in found:
                            targets.add(tf)

        fan_out[rel] = targets

    # reverse to fan-in
    fan_in = defaultdict(int)
    for src_file, tgts in fan_out.items():
        for tf in tgts:
            fan_in[tf] += 1

    rows = []
    all_files = set(fan_out.keys()) | set(fan_in.keys())
    for f in all_files:
        rows.append((f, fan_in.get(f, 0), len(fan_out.get(f, set()))))
    rows.sort()

    with open(OUT, "w") as f:
        f.write("file\tfan_in\tfan_out\n")
        for r in rows:
            f.write(f"{r[0]}\t{r[1]}\t{r[2]}\n")
    print(f"wrote {len(rows)} rows to {OUT}", file=sys.stderr)

    # quick sanity report
    top_out = sorted(rows, key=lambda r: r[2], reverse=True)[:5]
    top_in = sorted(rows, key=lambda r: r[1], reverse=True)[:5]
    print("\ntop fan_out:", file=sys.stderr)
    for r in top_out:
        print(f"  {r[2]:4d}  {r[0]}", file=sys.stderr)
    print("\ntop fan_in:", file=sys.stderr)
    for r in top_in:
        print(f"  {r[1]:4d}  {r[0]}", file=sys.stderr)


if __name__ == "__main__":
    main()
