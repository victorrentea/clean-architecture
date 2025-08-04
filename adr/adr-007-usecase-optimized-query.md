## Title

Search using Usecase-Optimized Query (CQRS) without entering 'domain'

## Status
Accepted
(Training Note: never deleted, but can be `Superseded` by a later ADR)

## Context
We are using an ORM (Hibernate/JPA). 

Fetching a full @Entity from DB can be expensive, 
as by default, JPA eager loads all its fields and
all @ManyToOne/@OneToOne links, 
which adds unnecessary columns, JOINs 
and sometimes even additional SELECT queries.

If only a few fields are needed, 
it's more CPU/memory/network efficient
to fetch only those fields from the DB,
especially when the entity is large,
and we retrieve many entities,
such as in a search flow.

## Decision

Every search use-case will select directly DTOs instead of Entities from DB.

That is, instead of doing:
```java
@Query("SELECT u FROM User u WHERE ...")
List<User> search(criteria); // BAD
```

Use one of the following two options:
1) Instantiate the DTO from the JQPL query:
```java
@Query("SELECT new com.example.dto.UserDto(u.id, u.name) FROM User u WHERE ...")
List<UserDto> search(criteria);
```
The DTO must then have a constructor that accepts all fields:
a record, Lombok's @Value, or handwritten.

2) Using [Spring Projections](https://docs.spring.io/spring-data/jpa/reference/repositories/projections.html) for larger DTOs:
```java
@Query("SELECT u.id as id, u.name as fullName FROM User u WHERE ...")
List<UserDto> search(...);
```
Such a DTO needs to have getters and setters (eg. Lombok's @Data)
matching the column aliases ('id', and 'fullName' above).

## Consequences
Positive:
- Better performance - by reading less from DB.
- Simplicity: Less mapping needed - the ORM populates directly the DTO object of the REST API.

Negative:
- Coupling - such code depends on DTOs => can't be in `domain.repository` -
has to be placed next to controller.
- Risk - domain logic might leak in DTO, if applying logic after SELECT.
- Risk - update DB using DTOs, bypassing any integrity protection
enforced by the Domain Model.

## Compliance
Enforcing is done via Code Review,
since we couldn't find any automated way to enforce it😢

## Notes
- Author: Developer A 
- Changelog: 
  - 0.1 initial proposed version on 2023-11-01
