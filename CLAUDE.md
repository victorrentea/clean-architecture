# clean-architecture — agent guidance

## Architecture guardrail: keep `packages.puml` in sync with the code

[`clean-application/docs/packages.puml`](clean-application/docs/packages.puml) is the **single source of
truth for the logical architecture** — which sub-package of `victor.training.clean` is allowed to depend
on which. It is enforced by
[`PackagesArchTest`](clean-application/src/test/java/victor/training/clean/PackagesArchTest.java):

- `adheresToDiagram` — every real cross-package dependency must be drawn as a `-->` arrow, and no arrow
  may exist that the code doesn't actually have.
- `diagramPackagesMatchCodePackages` — the `<<..stereotype>>` components must equal the source
  sub-packages of `victor.training.clean` exactly.

**Whenever you add, remove, move, or rename a package, or change a cross-package dependency, update
`clean-application/docs/packages.puml` in the same change** so the test stays green:

- new package  → add `[Component] <<..thatpackage>>` and the arrows for its real dependencies;
- deleted/renamed package → remove its component and the arrows touching it;
- new/removed cross-package dependency → add/erase the corresponding `[A] --> [B]` arrow.

If `PackagesArchTest` fails, **fix the diagram to match reality — do not weaken or delete the test.**

Verify with: `mvn -pl clean-application test -Dtest=PackagesArchTest`
