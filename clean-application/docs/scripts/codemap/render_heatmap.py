#!/usr/bin/env python3
"""Render codemap.tsv to a self-contained interactive HTML (Plotly via CDN).

Two views:
  1. Treemap: size=bytes, color=bugs_per_kloc (clamped), grouped by module > file.
  2. Log-log scatter: x=lines, y=bug_commits+1, color=bugs_per_kloc.
"""
import csv
import json
import os

_here = os.path.dirname(os.path.abspath(__file__))
OUT_DIR = os.path.abspath(os.environ.get("HEATMAP_OUT") or os.environ.get("HEATMAP_REPO") or _here)
TSV = os.path.join(OUT_DIR, "codemap.tsv")
OUT = os.path.join(OUT_DIR, "codemap.html")
TITLE = os.environ.get("HEATMAP_TITLE", 'Spring Framework Codemap')
SUBTITLE = os.environ.get("HEATMAP_SUBTITLE", '5,476 non-test Java files · 31,509 commits walked · 1,760 linked to type:bug/regression issues (post-2019 only).')
# Optional: ctrl/⌘-click a file tile to open it in an editor.
#   HEATMAP_OPEN_IN  = "vscode" | "intellij"  (preselected default; an in-page picker is also shown)
#   HEATMAP_REPO_ABS = absolute repo root the tsv paths are relative to (defaults to HEATMAP_REPO)
OPEN_IN = os.environ.get("HEATMAP_OPEN_IN", "").strip().lower()
REPO_ABS = os.path.abspath(os.environ.get("HEATMAP_REPO_ABS") or os.environ.get("HEATMAP_REPO") or OUT_DIR)
BUGS_CLAMP = 30.0
COMMITS_CLAMP = 150.0
COMPLEXITY_CLAMP = 200.0
COLOR_CLAMP = BUGS_CLAMP  # alias used in scatter

rows = []
with open(TSV) as f:
    reader = csv.DictReader(f, delimiter="\t")
    for r in reader:
        rows.append({
            "path": r["path"].lstrip("./"),
            "bytes": int(r["bytes"]),
            "lines": int(r["lines"]),
            "commits": int(r["commits"]),
            "bug_commits": int(r["bug_commits"]),
            "commits_per_kloc": float(r["commits_per_kloc"]),
            "bugs_per_kloc": float(r["bugs_per_kloc"]),
            "bugs_per_commit": float(r["bugs_per_commit"]),
            "cognitive_complexity": int(r.get("cognitive_complexity", 0) or 0),
            "complexity_per_kloc": float(r.get("complexity_per_kloc", 0) or 0),
            "fan_in": int(r.get("fan_in", 0) or 0),
            "fan_out": int(r.get("fan_out", 0) or 0),
        })

# Treemap data: path = ["modules", module, filename]. Use file basename to keep labels short.
modules = sorted({r["path"].split("/", 1)[0] for r in rows})
ids, labels, parents, values, customdata, hovertemplates = [], [], [], [], [], []
metric_lines, metric_commits, metric_bugs, metric_complexity, metric_fan_in, metric_fan_out = [], [], [], [], [], []

FILE_TEMPLATE = (
    "<b>%{customdata[0]}</b><br>"
    "lines: %{customdata[1]}<br>"
    "commits: %{customdata[2]} (%{customdata[5]}/KLOC)<br>"
    "bugs: %{customdata[3]} (%{customdata[4]}/KLOC)<br>"
    "complexity: %{customdata[6]} (%{customdata[7]}/KLOC)<br>"
    "fan_in: %{customdata[8]} | fan_out: %{customdata[9]}<extra></extra>"
)
MODULE_TEMPLATE = " <extra></extra>"  # space-only suppresses the box visually

for m in modules:
    ids.append(m); labels.append(m); parents.append(""); values.append(0); customdata.append(["", "", "", "", "", "", "", "", "", ""]); hovertemplates.append(MODULE_TEMPLATE)
    metric_lines.append(0); metric_commits.append(0); metric_bugs.append(0); metric_complexity.append(0); metric_fan_in.append(0); metric_fan_out.append(0)

for r in rows:
    mod = r["path"].split("/", 1)[0]
    basename = r["path"].rsplit("/", 1)[-1]
    node_id = r["path"]
    ids.append(node_id)
    labels.append(basename)
    parents.append(mod)
    values.append(r["bytes"])
    customdata.append([
        r["path"],
        f"{r['lines']:,}",
        f"{r['commits']:,}",
        f"{r['bug_commits']:,}",
        f"{r['bugs_per_kloc']:.2f}",
        f"{r['commits_per_kloc']:.2f}",
        f"{r['cognitive_complexity']:,}",
        f"{r['complexity_per_kloc']:.1f}",
        f"{r['fan_in']:,}",
        f"{r['fan_out']:,}",
    ])
    metric_lines.append(r["lines"])
    metric_commits.append(r["commits"])
    metric_bugs.append(r["bug_commits"])
    metric_complexity.append(r["cognitive_complexity"])
    metric_fan_in.append(r["fan_in"])
    metric_fan_out.append(r["fan_out"])
    hovertemplates.append(FILE_TEMPLATE)

# Scatter data: log-log lines vs bug_commits+1
scatter_rows = [r for r in rows if r["lines"] >= 50]  # skip tiny stubs
sx = [r["lines"] for r in scatter_rows]
sy = [r["bug_commits"] + 1 for r in scatter_rows]  # offset so 0 plots on log
sc = [min(r["bugs_per_kloc"], COLOR_CLAMP) for r in scatter_rows]
ssize = [max(4, min(40, (r["bytes"] / 5000) ** 0.5 * 3)) for r in scatter_rows]
stext = [
    f"{r['path']}<br>lines: {r['lines']:,} | commits: {r['commits']} | bug_commits: {r['bug_commits']}<br>bugs/KLOC: {r['bugs_per_kloc']:.2f}"
    for r in scatter_rows
]
# label only big outliers: bug_commits >= 10 OR (lines >= 200 AND bugs_per_kloc >= 20)
slabels = []
for r in scatter_rows:
    if r["bug_commits"] >= 10 or (r["lines"] >= 200 and r["bugs_per_kloc"] >= 20):
        slabels.append(r["path"].rsplit("/", 1)[-1].replace(".java", ""))
    else:
        slabels.append("")

treemap_data = {
    "ids": ids, "labels": labels, "parents": parents, "values": values,
    "customdata": customdata,
    "metrics": {
        "lines": metric_lines,
        "commits": metric_commits,
        "bugs": metric_bugs,
        "complexity": metric_complexity,
        "fan_in": metric_fan_in,
        "fan_out": metric_fan_out,
    },
    "hovertemplates": hovertemplates,
}
scatter_data = {"x": sx, "y": sy, "color": sc, "size": ssize, "text": stext, "labels": slabels}

html = """<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>__TITLE__</title>
<script src="https://cdn.plot.ly/plotly-2.35.2.min.js"></script>
<style>
  body { font-family: -apple-system, BlinkMacSystemFont, sans-serif; margin: 0; padding: 12px 20px; background: #fafafa; color: #222; }
  h1 { margin: 0 0 4px; font-size: 20px; }
  .sub { color: #666; font-size: 13px; margin-bottom: 16px; }
  .chart { background: white; border: 1px solid #ddd; border-radius: 6px; padding: 8px; margin-bottom: 24px; }
  .legend { color: #888; font-size: 12px; padding: 4px 0 12px; }
</style>
</head>
<body>
<h1>__TITLE__</h1>
<div class="sub">__SUBTITLE__</div>

<div class="chart">
  <div style="padding: 6px 4px 10px; font-size: 13px;">
    Color =
    <select id="num" style="margin: 0 4px;">
      <option value="bugs">bugs</option>
      <option value="commits">commits</option>
      <option value="complexity" selected>complexity</option>
      <option value="fan_in">fan_in</option>
      <option value="fan_out">fan_out</option>
    </select>
    per
    <select id="den" style="margin: 0 4px;">
      <option value="lines" selected>lines</option>
      <option value="commits">commits</option>
      <option value="bugs">bugs</option>
      <option value="complexity">complexity</option>
      <option value="fan_in">fan_in</option>
      <option value="fan_out">fan_out</option>
    </select>
    <span id="ratio-label" style="color: #888; margin-left: 8px;"></span>
  </div>
  <div id="treemap" style="height: 720px;"></div>
  <div class="legend">Treemap: rectangle area = file bytes. Color = ratio chosen above (clamped at p95 to keep tails from washing the scale). Click a module to zoom; hover a file for stats.</div>
</div>

<div class="chart">
  <div id="scatter" style="height: 640px;"></div>
  <div class="legend">Scatter: log–log of lines vs bug_commits+1. Marker size = sqrt(bytes), color = bugs/KLOC (clamped). Labels appear on outliers.</div>
</div>

<script>
const TREE = __TREE_JSON__;
const SCAT = __SCAT_JSON__;

function percentile(arr, p) {
  const sorted = arr.filter(v => v > 0).slice().sort((a, b) => a - b);
  if (sorted.length === 0) return 1;
  const idx = Math.floor(sorted.length * p);
  return sorted[Math.min(idx, sorted.length - 1)];
}

function computeRatio(numKey, denKey) {
  const num = TREE.metrics[numKey];
  const den = TREE.metrics[denKey];
  const out = new Array(num.length);
  for (let i = 0; i < num.length; i++) {
    out[i] = (den[i] > 0) ? (num[i] / den[i]) : 0;
  }
  return out;
}

function applyMetric() {
  const numKey = document.getElementById('num').value;
  const denKey = document.getElementById('den').value;
  // disable same-on-same in denominator
  for (const opt of document.getElementById('den').options) {
    opt.disabled = (opt.value === numKey);
  }
  if (denKey === numKey) {
    // force den to first non-conflicting option
    const den = document.getElementById('den');
    for (const opt of den.options) { if (!opt.disabled) { den.value = opt.value; break; } }
  }
  const den2 = document.getElementById('den').value;
  const ratio = computeRatio(numKey, den2);
  const cmax = percentile(ratio, 0.95) || 1;
  const title = `${numKey} / ${den2}`;
  document.getElementById('ratio-label').textContent = `(p95 = ${cmax.toExponential(2)})`;
  Plotly.restyle('treemap', {
    'marker.colors': [ratio],
    'marker.cmax': cmax,
    'marker.colorbar.title': title
  });
}

Plotly.newPlot('treemap', [{
  type: 'treemap',
  ids: TREE.ids,
  labels: TREE.labels,
  parents: TREE.parents,
  values: TREE.values,
  customdata: TREE.customdata,
  marker: {
    colors: computeRatio('complexity', 'lines'),
    colorscale: 'Reds',
    cmin: 0,
    cmax: percentile(computeRatio('complexity', 'lines'), 0.95) || 1,
    showscale: true,
    colorbar: { title: 'complexity / lines', thickness: 14 },
    line: { width: 0.5, color: '#fff' }
  },
  hovertemplate: TREE.hovertemplates,
  textposition: 'middle center',
  textfont: { size: 11 }
}], {
  margin: { t: 8, l: 8, r: 8, b: 8 }
}, { displaylogo: false });

document.getElementById('num').addEventListener('change', applyMetric);
document.getElementById('den').addEventListener('change', applyMetric);
applyMetric();

Plotly.newPlot('scatter', [{
  type: 'scatter',
  mode: 'markers+text',
  x: SCAT.x,
  y: SCAT.y,
  text: SCAT.labels,
  textposition: 'top center',
  textfont: { size: 9, color: '#444' },
  hovertext: SCAT.text,
  hovertemplate: '%{hovertext}<extra></extra>',
  marker: {
    size: SCAT.size,
    color: SCAT.color,
    colorscale: 'Reds',
    cmin: 0,
    cmax: __CLAMP__,
    showscale: true,
    colorbar: { title: 'bugs/KLOC', thickness: 14 },
    line: { width: 0.3, color: '#333' },
    opacity: 0.75
  }
}], {
  xaxis: { type: 'log', title: 'lines (log)', gridcolor: '#eee' },
  yaxis: { type: 'log', title: 'bug_commits + 1 (log)', gridcolor: '#eee' },
  margin: { t: 24, l: 60, r: 20, b: 50 },
  plot_bgcolor: '#fff',
  hovermode: 'closest'
}, { displaylogo: false });
</script>
</body>
</html>
"""

html = (html
        .replace("__TREE_JSON__", json.dumps(treemap_data))
        .replace("__SCAT_JSON__", json.dumps(scatter_data))
        .replace("__CLAMP__", str(COLOR_CLAMP))
        .replace("__TITLE__", TITLE)
        .replace("__SUBTITLE__", SUBTITLE))

# Optional ctrl/⌘-click-to-open-in-editor. Kept off unless HEATMAP_OPEN_IN is set, so
# the default page stays byte-for-byte identical to the original.
if OPEN_IN:
    sel_vscode = " selected" if OPEN_IN != "intellij" else ""
    sel_intellij = " selected" if OPEN_IN == "intellij" else ""
    editor_control = (
        '<span style="margin-left: 16px; color: #444;">⌘/Ctrl-click a file → open in '
        '<select id="editor" style="margin: 0 4px;">'
        f'<option value="vscode"{sel_vscode}>VS Code</option>'
        f'<option value="intellij"{sel_intellij}>IntelliJ</option>'
        '</select></span>'
    )
    html = html.replace(
        '<span id="ratio-label" style="color: #888; margin-left: 8px;"></span>',
        '<span id="ratio-label" style="color: #888; margin-left: 8px;"></span>\n    ' + editor_control,
        1,
    )
    editor_script = """
const REPO_ABS = __REPO_ABS__;
function openInEditor(rel) {
  const abs = REPO_ABS + '/' + rel;
  const editorEl = document.getElementById('editor');
  const mode = editorEl ? editorEl.value : 'vscode';
  if (mode === 'intellij') {
    // IntelliJ built-in web server. Requires the IDE running with
    // Settings > Build, Execution, Deployment > Debugger > "Allow unsigned requests".
    fetch('http://localhost:63342/api/file' + encodeURI(abs), { mode: 'no-cors' }).catch(function () {});
  } else {
    window.location.href = 'vscode://file' + encodeURI(abs);
  }
}
document.getElementById('treemap').on('plotly_treemapclick', function (d) {
  const ev = d && d.event;
  if (ev && (ev.ctrlKey || ev.metaKey)) {
    const pt = d.points && d.points[0];
    const id = pt && pt.id;
    if (id && id.indexOf('/') !== -1) openInEditor(id);
    return false; // suppress the default zoom on a modified click
  }
});
""".replace("__REPO_ABS__", json.dumps(REPO_ABS))
    html = html.replace('</script>\n</body>', editor_script + '</script>\n</body>', 1)

with open(OUT, "w") as f:
    f.write(html)

print(f"wrote {OUT} ({os.path.getsize(OUT) / 1024:.1f} KB)")
