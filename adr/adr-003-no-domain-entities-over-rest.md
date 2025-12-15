## Title

Do not expose Domain Model entities via REST endpoints

## Status

Accepted

## Context

Domain Model entities encapsulate business rules and internal structures. Exposing them directly over HTTP couples
external clients to internal representations, making refactoring risky and breaking compatibility. It can also leak
persistence details, invariants, lazy-loaded graphs, or sensitive fields, and complicate serialization, versioning, and
security. Our architecture separates concerns between layers and favors stable, purpose-built API contracts.

## Decision

- REST controllers must not return or accept classes from the `..domain..` packages (e.g.,
  `victor.training.clean.domain.model.*`).
- Public APIs will use dedicated request/response DTOs (a.k.a. API models/view models) tailored to client needs.
- Mapping between DTOs and domain entities is performed in the Application layer (e.g., via MapStruct mappers), not in
  the controllers nor in the Domain layer.

## Consequences

- Pros:
    - Stable, versionable API contracts decoupled from domain internals
    - Freedom to refactor the Domain Model without breaking external clients
    - Better control over exposed fields, avoiding over-posting/under-posting and sensitive data leaks
    - Smaller, purpose-specific payloads and improved performance/UX
- Cons:
    - Requires DTO types and mapping code
    - Additional discipline to keep controllers free of domain types

## Compliance

- Enforced by an ArchUnit test: `ArchitectureTest.domain_not_leaked_via_controller_methods`.
