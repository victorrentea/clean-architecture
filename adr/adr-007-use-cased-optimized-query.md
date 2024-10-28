## Title
Search Entities using Use-Case-Optimized Queries 

## Status
Superseded

## Context
We are using an ORM (Hibernate). 

Fetching a full @Entity from DB can be expensive, 
because by default, JPA loads the entity with all its fields
and @ManyToOne/@OneToOne links, which adds unnecessary
columns, JOINs and sometimes even additional SELECT statements.

If a flow requires only a small subset of the fields,
it is more CPU/memory/network efficient to fetch 
only the needed fields from the DB, especially if
we retrieve multiple entities (like in a search)
and/or the entity is very large. 

## Decision
We will select DTOs (rather than full Entities) 
for every search use-case.

## Consequences
Positive:
- Better performance by reading less data from DB.
- Less mapping code needed, as the ORM populates directly the DTO object.

Negative:
- May lead to implementing business rules on DTOs rather than Domain Entities.
- May lead developers to UPDATE data directly in DB using DTOs. 
   This can bypass the consistency protections that the Domain Model wants to enforce.

## Compliance
Any search usecase should use this approach.

This can be achieved by writing a JQPL query 
that uses the `SELECT new` technique to directly populate 
the required attributes into the DTO object:

```java
@Query("SELECT new com.example.dto.UserDto(u.id, u.name) FROM User u WHERE ...")
```

The DTO must have a constructor taking all fields (eg be a record).

Alternatively, for objects containing more attributes, 
[Spring Projections](https://docs.spring.io/spring-data/jpa/reference/repositories/projections.html) 
provide a better alternative be used:
```java
@Query("SELECT u.id as id, u.name as name FROM User u WHERE u.name = :name")
List<UserProjection> findByName(@Param("name") String name);
```

Enforcing is done via PR Review.

## Notes
- Author: Developer A 
- Changelog: 
  - 0.1 initial proposed version on 2023-11-01
