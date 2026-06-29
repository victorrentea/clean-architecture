# clean-application — Architecture Docs

Source-of-truth architecture artifacts for `victor.training.clean`. They are **kept in sync with the
code by guardrail tests** — if the code drifts, the build fails until the diagram is updated.

## Packages (logical architecture)

Source: [`packages.puml`](packages.puml). Validated by
[`PackagesArchTest`](../src/test/java/victor/training/clean/PackagesArchTest.java):

- **`adheresToDiagram`** — ArchUnit asserts that every real cross-package dependency is drawn as an
  arrow, and that no arrow exists that the code doesn't actually have.
- **`diagramPackagesMatchCodePackages`** — the set of `<<..stereotype>>` components must equal the set
  of source sub-packages of `victor.training.clean` exactly, so creating or deleting a package forces a
  diagram edit.

Run it: `mvn -pl clean-application test -Dtest=PackagesArchTest`

![Packages](https://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/victorrentea/clean-architecture/blip25_06/clean-application/docs/packages.puml)

> The diagram is rendered live by the public [PlantUML proxy](https://plantuml.com/) from the
> GitHub-hosted source. The URL pins the `blip25_06` branch — swap it for the branch you view this on.
> If the proxy is ever blocked, render locally with the PlantUML CLI or paste `packages.puml` into
> [www.plantuml.com/plantuml](https://www.plantuml.com/plantuml/uml).

## How to react when `PackagesArchTest` fails

The failure message tells you which half drifted. The fix is **always to make the diagram match the
code** (never to weaken the test):

| Failure | Meaning | Fix in `packages.puml` |
|---|---|---|
| `diagramPackagesMatchCodePackages` reports a package to **ADD** | a new sub-package appeared in code | add `[Name] <<..thatpackage>>` and the arrows for its real dependencies |
| `diagramPackagesMatchCodePackages` reports a package to **REMOVE** | a sub-package was deleted/renamed | delete its component and any arrows touching it |
| `adheresToDiagram` reports a violation | a new cross-package dependency was introduced (or an old one removed) | add the missing `[A] --> [B]` arrow (or erase the arrow the code no longer needs) |

## Code Map & Code City

Two interactive, self-contained HTML views of the codebase, ported from petclinic. They are
**descriptive** (a quick "what is this code shaped like?" tour), not enforced guardrails — regenerate
them on demand from the git history + source.

- **Code Map** — [`generated/codemap/codemap.html`](generated/codemap/codemap.html): a Plotly
  treemap (rectangle area = file bytes, color = a metric ratio you pick: complexity, fan-in/out,
  bug-fix commits…) plus a lines-vs-bug-fixes scatter.
- **Code City** — [`generated/codemap/codecity.html`](generated/codemap/codecity.html): a Three.js
  3D city where each package is a district and each file is a building (footprint, height, and color
  are independently selectable metrics).

Open either file in a browser (both pull their JS from a CDN and embed all data inline — no server).
⌘/Ctrl-click a tile/building to open that file in VS Code.

### Regenerate

```bash
cd clean-application/docs/scripts/codemap
pip install -r requirements.txt --target .pylibs   # one-time (vendors tree-sitter)
./generate.sh                                       # writes generated/codemap/*.{tsv,html}
```

The generators are repo-agnostic (driven by `HEATMAP_*` env vars set in `generate.sh`); see
[`scripts/codemap/README.md`](scripts/codemap/README.md) for the full pipeline and configuration.
`render_codecity.py` has a structural unit test: `python3 -m pytest scripts/codemap/test_render_codecity.py`.
