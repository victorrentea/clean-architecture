#!/usr/bin/env python3
"""Render codemap.tsv to a Three.js CodeCity."""
import csv
import json
import os
import sys
import urllib.parse
from pathlib import Path


_here = Path(__file__).resolve().parent
OUT_DIR = Path(os.environ.get("HEATMAP_OUT") or os.environ.get("HEATMAP_REPO") or _here).resolve()
TSV = Path(sys.argv[1]).resolve() if len(sys.argv) > 1 else OUT_DIR / "codemap.tsv"
OUT = OUT_DIR / "codecity.html"
TITLE = os.environ.get("HEATMAP_TITLE", "Code City")
REPO_ABS = Path(os.environ.get("HEATMAP_REPO_ABS") or os.environ.get("HEATMAP_REPO") or Path.cwd()).resolve()


def _number(row, key, cast=float):
    value = row.get(key, "0") or "0"
    return cast(value)


def _district(path):
    parts = path.split("/")
    if "java" in parts:
        java_index = parts.index("java")
        package_parts = parts[java_index + 1:-1]
        if package_parts:
            return ".".join(package_parts)
    if len(parts) > 1:
        return parts[-2]
    return "root"


rows = []
with TSV.open() as f:
    reader = csv.DictReader(f, delimiter="\t")
    for row in reader:
        path = row["path"].lstrip("./")
        rows.append({
            "path": path,
            "name": path.rsplit("/", 1)[-1].replace(".java", ""),
            "district": _district(path),
            "bytes": _number(row, "bytes", int),
            "lines": _number(row, "lines", int),
            "commits": _number(row, "commits", int),
            "bug_commits": _number(row, "bug_commits", int),
            "commits_per_kloc": _number(row, "commits_per_kloc"),
            "bugs_per_kloc": _number(row, "bugs_per_kloc"),
            "bugs_per_commit": _number(row, "bugs_per_commit"),
            "cognitive_complexity": _number(row, "cognitive_complexity", int),
            "complexity_per_kloc": _number(row, "complexity_per_kloc"),
            "fan_in": _number(row, "fan_in", int),
            "fan_out": _number(row, "fan_out", int),
        })

OUT_DIR.mkdir(parents=True, exist_ok=True)

html = """<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>__TITLE__</title>
<link rel="icon" href="__FAVICON__">
<script type="importmap">
{
  "imports": {
    "three": "https://cdn.jsdelivr.net/npm/three@0.166.1/build/three.module.js",
    "three/addons/": "https://cdn.jsdelivr.net/npm/three@0.166.1/examples/jsm/"
  }
}
</script>
<style>
  html, body {
    width: 100%;
    height: 100%;
    margin: 0;
    overflow: hidden;
    background: #f4f5f7;
    color: #1f2933;
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
  }
  #scene {
    position: fixed;
    inset: 0;
  }
  .panel {
    position: fixed;
    left: 16px;
    top: 16px;
    z-index: 2;
    width: min(430px, calc(100vw - 32px));
    background: rgba(255, 255, 255, 0.88);
    border: 1px solid rgba(140, 148, 160, 0.45);
    border-radius: 8px;
    box-shadow: 0 12px 40px rgba(15, 23, 42, 0.14);
    backdrop-filter: blur(10px);
    padding: 12px 14px;
  }
  h1 {
    margin: 0 0 3px;
    font-size: 17px;
    font-weight: 650;
    display: flex;
    align-items: center;
    gap: 8px;
  }
  h1 .logo {
    flex: none;
    width: 22px;
    height: 22px;
  }
  .sub {
    margin: 0 0 11px;
    color: #52606d;
    font-size: 12px;
    line-height: 1.35;
  }
  .controls {
    display: grid;
    grid-template-columns: repeat(3, minmax(0, 1fr));
    gap: 8px;
  }
  label {
    display: grid;
    gap: 3px;
    color: #52606d;
    font-size: 11px;
    font-weight: 600;
    text-transform: uppercase;
  }
  select {
    min-width: 0;
    border: 1px solid #c8d0da;
    border-radius: 6px;
    background: #fff;
    color: #1f2933;
    font-size: 13px;
    padding: 6px 8px;
  }
  #hover {
    position: fixed;
    left: 0;
    top: 0;
    z-index: 2;
    max-width: min(520px, calc(100vw - 32px));
    pointer-events: none;
    background: rgba(17, 24, 39, 0.84);
    color: #fff;
    border-radius: 8px;
    padding: 9px 11px;
    font-size: 12px;
    line-height: 1.45;
    opacity: 0;
    transform: translate(-50%, calc(-100% - 12px));
    transition: opacity 120ms ease, transform 120ms ease;
  }
  #hover.visible {
    opacity: 1;
    transform: translate(-50%, calc(-100% - 18px));
  }
  #hover b {
    display: block;
    font-size: 13px;
    margin-bottom: 3px;
  }
  #hover .fqn {
    display: block;
    margin-bottom: 4px;
    color: #aab4c8;
    font-size: 11px;
    font-family: ui-monospace, SFMono-Regular, Menlo, monospace;
  }
  .city-label {
    padding: 3px 8px;
    border-radius: 6px;
    background: rgba(17, 24, 39, 0.86);
    color: #fff;
    font-size: 12px;
    font-weight: 650;
    white-space: nowrap;
    pointer-events: none;
    text-shadow: 0 1px 2px rgba(0, 0, 0, 0.4);
    box-shadow: 0 6px 18px rgba(15, 23, 42, 0.28);
  }
</style>
</head>
<body>
<div id="scene"></div>
<section class="panel">
  <h1>__LOGO_SVG__<span>__TITLE__</span></h1>
  <p class="sub">Drag to pan, Cmd/Ctrl-drag to rotate, scroll to zoom. Cmd/Ctrl-double-click opens a file in VS Code.</p>
  <div class="controls">
    <label>
      Color
      <select id="colorMetric">
        <option value="complexity_per_kloc" selected>cyclomatic complexity / KLOC</option>
        <option value="bugs_per_kloc">bugs / KLOC</option>
        <option value="commits_per_kloc">commits / KLOC</option>
        <option value="fan_in">fan in</option>
        <option value="fan_out">fan out</option>
      </select>
    </label>
    <label>
      Height
      <select id="heightMetric">
        <option value="lines">lines</option>
        <option value="cognitive_complexity" selected>cyclomatic complexity</option>
        <option value="commits">commits</option>
        <option value="bug_commits">bug commits</option>
        <option value="fan_out">fan out</option>
      </select>
    </label>
    <label>
      Area
      <select id="areaMetric">
        <option value="bytes" selected>bytes</option>
        <option value="lines">lines</option>
        <option value="cognitive_complexity">cyclomatic complexity</option>
        <option value="commits">commits</option>
        <option value="fan_out">fan out</option>
      </select>
    </label>
  </div>
</section>
<div id="hover"></div>

<script>
const FILES = __FILES_JSON__;
const REPO_ABS = __REPO_ABS__;
</script>
<script type="module">
import * as THREE from "three";
import { OrbitControls } from "three/addons/controls/OrbitControls.js";
import { CSS2DRenderer, CSS2DObject } from "three/addons/renderers/CSS2DRenderer.js";
import * as d3 from "https://cdn.jsdelivr.net/npm/d3@7/+esm";

const mount = document.getElementById("scene");
const hover = document.getElementById("hover");
const areaSelect = document.getElementById("areaMetric");
const heightSelect = document.getElementById("heightMetric");
const colorSelect = document.getElementById("colorMetric");
const citySize = 900;
const districtGap = 14;
const fileGap = 3;
const districtStep = 6;
const maxHeight = 190;
const minHeight = 5;
let buildings = [];
let districts = [];
let cityLabels = [];

const scene = new THREE.Scene();
scene.background = new THREE.Color(0xf4f5f7);
scene.fog = new THREE.Fog(0xf4f5f7, 900, 2200);

const camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, 1, 4000);
camera.position.set(720, 620, 760);

const renderer = new THREE.WebGLRenderer({ antialias: true });
renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));
renderer.setSize(window.innerWidth, window.innerHeight);
renderer.shadowMap.enabled = true;
renderer.shadowMap.type = THREE.PCFSoftShadowMap;
mount.appendChild(renderer.domElement);
renderer.domElement.style.cursor = "move";

const labelRenderer = new CSS2DRenderer();
labelRenderer.setSize(window.innerWidth, window.innerHeight);
labelRenderer.domElement.style.position = "fixed";
labelRenderer.domElement.style.inset = "0";
labelRenderer.domElement.style.pointerEvents = "none";
mount.appendChild(labelRenderer.domElement);

const controls = new OrbitControls(camera, renderer.domElement);
controls.enableDamping = true;
controls.dampingFactor = 0.06;
controls.screenSpacePanning = true;
controls.zoomToCursor = true;
controls.minDistance = 140;
controls.maxDistance = 1900;
controls.target.set(0, 0, 0);
controls.mouseButtons.LEFT = THREE.MOUSE.PAN;
controls.mouseButtons.RIGHT = THREE.MOUSE.PAN;
controls.update();

// OrbitControls inverts the LEFT action when a modifier is held: with LEFT=PAN,
// Cmd/Ctrl-drag automatically becomes an orbit. We only re-center the pivot so the
// orbit spins around the point in the middle of the viewport.
function setRotationPivotToViewportCenter() {
  centerRaycaster.setFromCamera({ x: 0, y: 0 }, camera);
  if (centerRaycaster.ray.intersectPlane(groundPlane, rotationPivot)) {
    controls.target.copy(rotationPivot);
    controls.update();
  }
}

function onPointerDown(event) {
  if (event.metaKey || event.ctrlKey) {
    setRotationPivotToViewportCenter();
  }
}

// Move cursor by default (you can pan); a circular-arrow cursor while Cmd/Ctrl is
// held to hint that dragging now orbits the camera.
const ROTATE_CURSOR_SVG =
  "<svg xmlns='http://www.w3.org/2000/svg' width='28' height='28' viewBox='0 0 28 28'>" +
  "<g fill='none' stroke-linecap='round' stroke-linejoin='round'>" +
  "<path d='M21 8 A9 9 0 1 0 22.7 14' stroke='#ffffff' stroke-width='5'/>" +
  "<path d='M21.4 2.6 21 8 15.8 7.4' stroke='#ffffff' stroke-width='5'/>" +
  "<path d='M21 8 A9 9 0 1 0 22.7 14' stroke='#111827' stroke-width='2.4'/>" +
  "<path d='M21.4 2.6 21 8 15.8 7.4' stroke='#111827' stroke-width='2.4'/>" +
  "</g></svg>";
const ROTATE_CURSOR = `url("data:image/svg+xml,${encodeURIComponent(ROTATE_CURSOR_SVG)}") 14 14, grab`;

function updateCursor(event) {
  renderer.domElement.style.cursor = (event.metaKey || event.ctrlKey) ? ROTATE_CURSOR : "move";
}

function updatePointer(event) {
  pointer.x = (event.clientX / window.innerWidth) * 2 - 1;
  pointer.y = -(event.clientY / window.innerHeight) * 2 + 1;
}

function pickBuilding(event) {
  updatePointer(event);
  raycaster.setFromCamera(pointer, camera);
  return raycaster.intersectObjects(buildings.map(b => b.mesh), false)[0] || null;
}

function openInEditor(rel) {
  const abs = REPO_ABS + "/" + rel;
  window.location.href = "vscode://file" + encodeURI(abs);
}

scene.add(new THREE.HemisphereLight(0xffffff, 0x8592a3, 2.1));
const sun = new THREE.DirectionalLight(0xffffff, 2.2);
sun.position.set(-420, 780, 520);
sun.castShadow = true;
sun.shadow.mapSize.set(2048, 2048);
sun.shadow.camera.left = -650;
sun.shadow.camera.right = 650;
sun.shadow.camera.top = 650;
sun.shadow.camera.bottom = -650;
scene.add(sun);

const groundMaterial = new THREE.MeshStandardMaterial({
  color: 0xb8bdc3,
  roughness: 0.74,
  metalness: 0.02,
});
const ground = new THREE.Mesh(new THREE.BoxGeometry(citySize + 36, 8, citySize + 36), groundMaterial);
ground.position.y = -5;
ground.receiveShadow = true;
scene.add(ground);

const raycaster = new THREE.Raycaster();
const centerRaycaster = new THREE.Raycaster();
const pointer = new THREE.Vector2();
const groundPlane = new THREE.Plane(new THREE.Vector3(0, 1, 0), 0);
const rotationPivot = new THREE.Vector3();

function percentile(values, p) {
  const sorted = values.filter(v => Number.isFinite(v) && v > 0).sort((a, b) => a - b);
  if (sorted.length === 0) return 1;
  return sorted[Math.min(sorted.length - 1, Math.floor(sorted.length * p))];
}

function metricMax(key) {
  return percentile(FILES.map(f => Number(f[key]) || 0), 0.95);
}

function colorFor(value, max) {
  const t = Math.max(0, Math.min(1, value / (max || 1)));
  // Light blue (0) -> burgundy red (max): the city reads light with hot spots in red.
  return new THREE.Color(0xe8eefc).lerp(new THREE.Color(0x800020), t);
}

// A hatch pattern for a terrace top: parallel lines in the terrace edge colour,
// their direction rotating with the nesting level so each floor is distinguishable.
function hatchTexture(edgeColor, level) {
  const size = 64;
  const gap = 16;
  const canvas = document.createElement("canvas");
  canvas.width = canvas.height = size;
  const ctx = canvas.getContext("2d");
  ctx.fillStyle = "#e8eefc";
  ctx.fillRect(0, 0, size, size);
  ctx.strokeStyle = "#" + edgeColor.getHexString();
  ctx.lineWidth = 3;
  ctx.beginPath();
  const dir = level % 4;
  if (dir === 0) {
    for (let y = gap / 2; y < size; y += gap) { ctx.moveTo(0, y); ctx.lineTo(size, y); }
  } else if (dir === 2) {
    for (let x = gap / 2; x < size; x += gap) { ctx.moveTo(x, 0); ctx.lineTo(x, size); }
  } else if (dir === 1) {
    for (let k = -size; k < size * 2; k += gap) { ctx.moveTo(k, size); ctx.lineTo(k + size, 0); }
  } else {
    for (let k = -size; k < size * 2; k += gap) { ctx.moveTo(k, 0); ctx.lineTo(k + size, size); }
  }
  ctx.stroke();
  const texture = new THREE.CanvasTexture(canvas);
  texture.wrapS = texture.wrapT = THREE.RepeatWrapping;
  texture.colorSpace = THREE.SRGBColorSpace;
  return texture;
}

function insertPackage(parent, parts, file, areaMetric) {
  if (parts.length === 0) {
    parent.children.push({ name: file.name, value: Math.max(1, Number(file[areaMetric]) || 0), file });
    return;
  }
  const name = parts[0];
  let child = parent.children.find(c => c.name === name && !c.file);
  if (!child) {
    child = {
      name,
      packageName: parent.packageName ? `${parent.packageName}.${name}` : name,
      children: [],
    };
    parent.children.push(child);
  }
  insertPackage(child, parts.slice(1), file, areaMetric);
}

function buildHierarchy(areaMetric) {
  const root = { name: "root", packageName: "", children: [] };
  for (const file of FILES) {
    insertPackage(root, file.district.split("."), file, areaMetric);
  }
  return d3.hierarchy(root)
    .sum(d => d.value || 0)
    .sort((a, b) => b.value - a.value);
}

function clearCity() {
  for (const label of cityLabels) {
    label.removeFromParent();
    label.element.remove();
  }
  cityLabels = [];
  for (const entry of buildings) {
    scene.remove(entry.mesh);
    entry.mesh.geometry.dispose();
    entry.mesh.material.dispose();
  }
  buildings = [];
  districts = [];
  for (const object of [...scene.children]) {
    if (object.userData.kind === "package") {
      scene.remove(object);
      object.geometry.dispose();
      if (Array.isArray(object.material)) {
        object.material.forEach(m => { if (m.map) m.map.dispose(); m.dispose(); });
      } else {
        object.material.dispose();
      }
    }
  }
}

function rebuildCity() {
  clearCity();
  const areaMetric = areaSelect.value;
  const heightMetric = heightSelect.value;
  const colorMetric = colorSelect.value;
  const maxMetric = metricMax(heightMetric);
  const maxColor = metricMax(colorMetric);

  const layout = d3.treemap()
    .size([citySize, citySize])
    .paddingOuter(districtGap)
    .paddingInner(fileGap)
    .round(true);
  const root = layout(buildHierarchy(areaMetric));

  // Each package becomes a terraced platform: the deeper it is nested, the higher
  // it rises, so a parent package (e.g. victor) visibly contains its children
  // (rest, mapper, ...). A thin outline delimits every package from its siblings.
  for (const node of root.descendants()) {
    if (node === root || !node.children) {
      continue;
    }
    const level = Math.max(0, node.depth - 1);
    const width = Math.max(2, node.x1 - node.x0);
    const depth = Math.max(2, node.y1 - node.y0);
    const slab = districtStep + 2;
    const topY = node.depth * districtStep;
    const cx = node.x0 + width / 2 - citySize / 2;
    const cz = node.y0 + depth / 2 - citySize / 2;
    // Heat-colour the riser (vertical "height" faces) with the same scale as the
    // buildings; hatch the light top surface with lines in that same edge colour,
    // rotating the hatch direction per nesting level so each floor stands apart.
    const leaves = node.leaves();
    const avgColor = leaves.reduce((sum, l) => sum + (Number(l.data.file[colorMetric]) || 0), 0) / leaves.length;
    const edgeColor = colorFor(avgColor, maxColor);
    const sideMaterial = new THREE.MeshStandardMaterial({ color: edgeColor, roughness: 0.85 });
    const topTexture = hatchTexture(edgeColor, level);
    topTexture.repeat.set(width / 50, depth / 50);
    const topMaterial = new THREE.MeshStandardMaterial({ map: topTexture, roughness: 0.9 });
    const block = new THREE.Mesh(
      new THREE.BoxGeometry(width, slab, depth),
      [sideMaterial, sideMaterial, topMaterial, topMaterial, sideMaterial, sideMaterial]
    );
    block.position.set(cx, topY - slab / 2, cz);
    block.receiveShadow = true;
    block.userData.kind = "package";
    block.userData.name = node.data.packageName || node.data.name;
    block.userData.fileCount = node.leaves().length;
    scene.add(block);
    districts.push(block);

    const outline = new THREE.LineSegments(
      new THREE.EdgesGeometry(block.geometry),
      new THREE.LineBasicMaterial({ color: 0xcdd6e4, transparent: true, opacity: 0.45 })
    );
    outline.position.copy(block.position);
    outline.userData.kind = "package";
    scene.add(outline);
  }

  for (const leaf of root.leaves()) {
    const file = leaf.data.file;
    const width = Math.max(4, leaf.x1 - leaf.x0 - 1);
    const depth = Math.max(4, leaf.y1 - leaf.y0 - 1);
    const metricValue = Number(file[heightMetric]) || 0;
    const colorValue = Number(file[colorMetric]) || 0;
    const height = minHeight + Math.pow(metricValue / (maxMetric || 1), 0.62) * maxHeight;
    const geometry = new THREE.BoxGeometry(width, height, depth);
    const material = new THREE.MeshStandardMaterial({
      color: colorFor(colorValue, maxColor),
      roughness: 0.58,
      metalness: 0.06,
    });
    const mesh = new THREE.Mesh(geometry, material);
    const baseY = leaf.parent.depth * districtStep;
    mesh.position.set(
      leaf.x0 + (leaf.x1 - leaf.x0) / 2 - citySize / 2,
      baseY + height / 2,
      leaf.y0 + (leaf.y1 - leaf.y0) / 2 - citySize / 2
    );
    mesh.castShadow = true;
    mesh.receiveShadow = true;
    mesh.userData.file = file;
    mesh.userData.baseColor = material.color.clone();
    scene.add(mesh);
    buildings.push({ mesh, file, height });
  }
  labelTopBuildings(areaMetric, heightMetric, colorMetric);
  window.__CODEMAP_3D_READY__ = {
    buildings: buildings.length,
    areaMetric,
    heightMetric,
    colorMetric,
  };
}

function addPermanentLabel(entry) {
  const div = document.createElement("div");
  div.className = "city-label";
  div.textContent = entry.file.name;
  const label = new CSS2DObject(div);
  const top = entry.mesh.position.y + entry.height / 2;
  label.position.set(entry.mesh.position.x, top + 16, entry.mesh.position.z);
  label.center.set(0.5, 1);
  scene.add(label);
  cityLabels.push(label);
}

// Permanently label the standout buildings: the largest footprint, the tallest,
// and the most intensely colored class for the currently selected metrics.
function labelTopBuildings(areaMetric, heightMetric, colorMetric) {
  const winners = new Set();
  for (const metric of [areaMetric, heightMetric, colorMetric]) {
    let best = null;
    let bestValue = -Infinity;
    for (const entry of buildings) {
      const value = Number(entry.file[metric]) || 0;
      if (value > bestValue) {
        bestValue = value;
        best = entry;
      }
    }
    if (best) winners.add(best);
  }
  for (const entry of winners) {
    addPermanentLabel(entry);
  }
}

function qualifiedName(file) {
  const module = file.path.includes("/src/") ? file.path.split("/src/")[0] : file.path.split("/")[0];
  const fqn = file.district && file.district !== "root" ? `${file.district}.${file.name}` : file.name;
  return `${module}/${fqn}`;
}

function formatHover(file) {
  return `<b>${file.name}</b><span class="fqn">${qualifiedName(file)}</span>` +
    `lines: ${file.lines.toLocaleString()} | cyclomatic complexity: ${file.cognitive_complexity.toLocaleString()} | ` +
    `commits: ${file.commits.toLocaleString()} | bug commits: ${file.bug_commits.toLocaleString()}<br>` +
    `coupling fan in/out: ${file.fan_in.toLocaleString()} / ${file.fan_out.toLocaleString()}`;
}

function formatDistrictHover(district) {
  return `<b>package: ${district.userData.name}</b>` +
    `${district.userData.fileCount.toLocaleString()} Java files`;
}

function positionHoverNearObject(object) {
  const center = new THREE.Vector3();
  object.getWorldPosition(center);
  if (object.geometry && object.geometry.boundingBox === null) {
    object.geometry.computeBoundingBox();
  }
  const height = object.geometry?.boundingBox
    ? object.geometry.boundingBox.max.y - object.geometry.boundingBox.min.y
    : 0;
  center.y += height / 2 + 10;
  center.project(camera);
  const x = (center.x * 0.5 + 0.5) * window.innerWidth;
  const y = (-center.y * 0.5 + 0.5) * window.innerHeight;
  hover.style.left = `${Math.max(24, Math.min(window.innerWidth - 24, x))}px`;
  hover.style.top = `${Math.max(24, Math.min(window.innerHeight - 24, y))}px`;
}

function onPointerMove(event) {
  updateCursor(event);
  const hit = pickBuilding(event);
  for (const entry of buildings) {
    entry.mesh.material.emissive.setHex(0x000000);
  }
  if (hit) {
    hit.object.material.emissive.setHex(0x5a0f1e);
    hover.innerHTML = formatHover(hit.object.userData.file);
    positionHoverNearObject(hit.object);
    hover.classList.add("visible");
  } else {
    const districtHit = raycaster.intersectObjects(districts, false)[0];
    if (districtHit) {
      hover.innerHTML = formatDistrictHover(districtHit.object);
      positionHoverNearObject(districtHit.object);
      hover.classList.add("visible");
    } else {
      hover.classList.remove("visible");
    }
  }
}

function onDoubleClick(event) {
  if (!event.metaKey && !event.ctrlKey) {
    return;
  }
  const hit = pickBuilding(event);
  if (!hit) {
    return;
  }
  event.preventDefault();
  openInEditor(hit.object.userData.file.path);
}

function onResize() {
  camera.aspect = window.innerWidth / window.innerHeight;
  camera.updateProjectionMatrix();
  renderer.setSize(window.innerWidth, window.innerHeight);
  labelRenderer.setSize(window.innerWidth, window.innerHeight);
}

areaSelect.addEventListener("change", rebuildCity);
heightSelect.addEventListener("change", rebuildCity);
colorSelect.addEventListener("change", rebuildCity);
window.addEventListener("resize", onResize);
window.addEventListener("pointermove", onPointerMove);
window.addEventListener("pointerdown", onPointerDown, true);
window.addEventListener("keydown", updateCursor);
window.addEventListener("keyup", updateCursor);
window.addEventListener("blur", () => { renderer.domElement.style.cursor = "move"; });
window.addEventListener("dblclick", onDoubleClick);

rebuildCity();

function animate() {
  controls.update();
  renderer.render(scene, camera);
  labelRenderer.render(scene, camera);
  requestAnimationFrame(animate);
}
animate();
</script>
</body>
</html>
"""

_LOGO_SHAPES = (
    '<rect x="2.5" y="9" width="5.5" height="12.5" rx="1" fill="#5b8def"/>'
    '<rect x="9" y="3.5" width="6" height="18" rx="1" fill="#1e3a8a"/>'
    '<rect x="16" y="11.5" width="5.5" height="10" rx="1" fill="#3a86ff"/>'
)
LOGO_SVG = f'<svg class="logo" viewBox="0 0 24 24" aria-hidden="true">{_LOGO_SHAPES}</svg>'
FAVICON = "data:image/svg+xml," + urllib.parse.quote(
    f'<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">{_LOGO_SHAPES}</svg>'
)

html = (html
        .replace("__TITLE__", TITLE)
        .replace("__LOGO_SVG__", LOGO_SVG)
        .replace("__FAVICON__", FAVICON)
        .replace("__FILES_JSON__", json.dumps(rows))
        .replace("__REPO_ABS__", json.dumps(str(REPO_ABS))))
OUT.write_text(html)
print(f"wrote {OUT} ({OUT.stat().st_size / 1024:.1f} KB)")
