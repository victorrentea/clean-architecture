## Title
Lombok's @Data is not allowed on @Entity classes

## Status
Accepted

## Context
We are using an ORM (Hibernate/JPA).
Lombok helps us reduce the boilerplate of Java language.

Lombok's @Data generates:
- Getters and Setters
- @RequiredArgsConstructor -
- @HashCodeAndEquals
- @ToString

## Decision
We will avoid generating hash/equals/tostring on Entities

WIP