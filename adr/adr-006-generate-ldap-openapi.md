## Title

Generate Client Code from OpenAPI Specification for LDAP Integration

## Status

Accepted

## Context

Our application needs to integrate with an LDAP service via its REST API with OpenAPI specification.

We considered two approaches:

1. Manually implementing client code and DTOs
2. Generating client code from the OpenAPI specification

## Decision

We will use the OpenAPI Generator to automatically generate client code from the LDAP service's OpenAPI specification.

Configuration:

- Store LDAP API spec as YAML in our project
- Use openapi-generator-maven-plugin
- Place generated code in infrastructure layer
- Add "Dto" suffix to model classes

## Consequences

Positive:

- Reduced development time
- Improved accuracy - generated code matches API spec exactly
- Easier maintenance - just regenerate when API changes
- Less risk than manual implementation - eliminates human error
- Type safety for API interactions

Negative:

- Generated code is placed in infrastructure layer
- Direct use of generated DTOs creates coupling to infrastructure
- We lose control over the code structure of generated DTOs
- To keep code clean, we need adapters to protect against potentially ugly DTO structure
- Generated code may not follow project coding standards

## Compliance

To ensure proper separation of concerns:

1. Create domain-specific models independent of generated DTOs
2. Implement adapters to convert between domain models and DTOs
3. Keep all LDAP API interaction in infrastructure layer
4. Use dependency injection for LDAP services

Compliance is enforced through code reviews.

## Notes

- Author: Developer A
- Changelog:
  - 0.1 initial proposed version on 2023-11-01
  - 0.2 clarified risk reduction and added note about adapter need
