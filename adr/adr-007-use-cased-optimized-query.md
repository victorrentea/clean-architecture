## Title
Search Entities using Use-Case-Optimized Queries (CQRS)

## Status
Accepted
(Training Note: never deleted, but can be `Superseded` by a later ADR)

## Context
We are using an ORM (Hibernate/JPA). 

Fetching a JPA @Entity from DB can be expensive, 
as by default, JPA eager loads all its fields and
all @ManyToOne/@OneToOne links, 
which adds unnecessary columns, JOINs 
and sometimes even additional SELECT queries.

If a flow requires only a few fields,
it's more CPU/memory/network efficient to fetch 
only those fields from the DB, especially when
the entity is large, and we retrieve many of them, 
like in a search.

## Decision
For every search use-case we will select DTOs instead of Entities.

For example, this can be implemented in two ways:
1) Instantiating the DTO from the JQPL query:
```java
@Query("SELECT new com.example.dto.UserDto(u.id, u.name) FROM User u WHERE ...")
List<UserDto> search(...);
```
Such DTO must have a constructor taking all fields:
a record, Lombok's @Value, or handwritten.

2) Using [Spring Projections](https://docs.spring.io/spring-data/jpa/reference/repositories/projections.html) for larger DTOs:
```java
@Query("SELECT u.id as id, u.name as fullName FROM User u WHERE ...")
List<UserDto> search(...);
```
Such a DTO needs to have getters and setters (eg. @Data)
matching the column aliases ('id', and 'fullName' above).

## Consequences
Positive:
- Better performance - by reading less from DB.
- Less mapping needed - the ORM populates directly the DTO object.

Negative:
- Coupling - this repository method depends on DTOs => can't be in `domain`.
We will place it in another Spring Data Repo interface next to controller.
- Risk - domain logic in DTO, if applying logic after SELECT.
- Risk - updating DB using DTOs, bypassing the integrity protection
potentially enforced by the Domain Model.

## Compliance
Enforcing is done via Code Review.
(since we couldn't find any automated way to check it😢)

## Notes
- Author: Developer A 
- Changelog: 
  - 0.1 initial proposed version on 2023-11-01


Can you give some examples, what should NOT be put into ADR (what kind of decision)?
- functional requirements/docs
- taste-based
- emotional talk


only why and what should be answered by ADR not how(implementation details)
