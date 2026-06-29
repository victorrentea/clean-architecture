#!/usr/bin/env python3
"""Fetch all type:bug and type:regression issue numbers from spring-projects/spring-framework.

Strategy: GitHub search API is capped at 1000 results/query and 10 req/min unauth.
We partition by created-date range, recursively splitting any window whose count >= 1000.
"""
import json
import os
import sys
import time
import urllib.error
import urllib.parse
import urllib.request
from datetime import date, timedelta

REPO = os.environ.get("FETCH_BUGS_REPO", "spring-projects/spring-framework")
LABELS = ['"type: bug"', '"type: regression"']
_outdir = os.environ.get("HEATMAP_OUT", ".")
OUT = os.environ.get("HEATMAP_BUG_FILE", os.path.join(_outdir, "bug_issues.txt"))
LOG = os.path.join(os.path.dirname(OUT) or ".", "fetch_bugs.log")

# Optional auth (GITHUB_TOKEN / GH_TOKEN) lifts the search rate limit 10 -> 30 req/min.
GH_TOKEN = os.environ.get("GITHUB_TOKEN") or os.environ.get("GH_TOKEN")
# pacing
SEARCH_LIMIT_PER_MIN = 30 if GH_TOKEN else 10
SLEEP_BETWEEN_CALLS = 2.1 if GH_TOKEN else 6.5  # stay under the per-minute cap

session_calls = 0
session_start = time.time()
log_fp = open(LOG, "w")


def log(msg):
    print(msg, file=log_fp, flush=True)
    print(msg, file=sys.stderr, flush=True)


def gh_search(label, since, until, page):
    """Run one search query page. Returns (total_count, items)."""
    global session_calls
    q = f'repo:{REPO} label:{label} is:issue created:{since}..{until}'
    qs = urllib.parse.urlencode({"q": q, "per_page": 100, "page": page})
    url = f"https://api.github.com/search/issues?{qs}"
    headers = {"Accept": "application/vnd.github+json"}
    if GH_TOKEN:
        headers["Authorization"] = f"Bearer {GH_TOKEN}"
    req = urllib.request.Request(url, headers=headers)
    while True:
        try:
            with urllib.request.urlopen(req, timeout=30) as r:
                data = json.load(r)
            session_calls += 1
            time.sleep(SLEEP_BETWEEN_CALLS)
            return data.get("total_count", 0), data.get("items", [])
        except urllib.error.HTTPError as e:
            if e.code == 403 or e.code == 429:
                # rate-limit: sleep until reset header if available
                reset = e.headers.get("X-RateLimit-Reset")
                wait = 65
                if reset:
                    wait = max(5, int(reset) - int(time.time()) + 2)
                log(f"  rate-limited, sleeping {wait}s")
                time.sleep(wait)
                continue
            log(f"  HTTP {e.code} on {url}: {e.read()[:200]}")
            raise


def enumerate_label(label, since, until, out_set):
    """Recursively partition date range when count >= 1000."""
    total, items = gh_search(label, since, until, 1)
    log(f"  {label} {since}..{until} -> total={total}")
    if total == 0:
        return
    if total < 1000:
        # paginate fully
        for it in items:
            out_set.add(it["number"])
        pages_needed = (total - 1) // 100 + 1
        for p in range(2, pages_needed + 1):
            _, more = gh_search(label, since, until, p)
            for it in more:
                out_set.add(it["number"])
        return
    # split
    d1 = date.fromisoformat(since)
    d2 = date.fromisoformat(until)
    if d1 >= d2:
        log(f"  cannot split further at {since}, taking first 1000")
        for it in items:
            out_set.add(it["number"])
        for p in range(2, 11):
            _, more = gh_search(label, since, until, p)
            for it in more:
                out_set.add(it["number"])
        return
    mid = d1 + (d2 - d1) // 2
    enumerate_label(label, since, mid.isoformat(), out_set)
    next_day = (mid + timedelta(days=1)).isoformat()
    enumerate_label(label, next_day, until, out_set)


def main():
    bug_numbers = set()
    today = date.today().isoformat()
    earliest = "2008-01-01"  # well before spring's github existence
    for label in LABELS:
        log(f"== {label} ==")
        before_count = len(bug_numbers)
        enumerate_label(label, earliest, today, bug_numbers)
        log(f"  added {len(bug_numbers) - before_count} new numbers (set size now {len(bug_numbers)})")
    with open(OUT, "w") as f:
        for n in sorted(bug_numbers):
            f.write(f"{n}\n")
    log(f"Wrote {len(bug_numbers)} issue numbers to {OUT}")
    log(f"Total API calls: {session_calls}, wall time: {int(time.time() - session_start)}s")


if __name__ == "__main__":
    main()
