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
