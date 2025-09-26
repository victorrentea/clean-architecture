## Title
Search using Usecase-Optimized Query (CQRS)

## Status
Accepted
(Training Note: never delete and ADR; but can be `Supersed` it with a later ADR)

## Context
We are using an ORM (Hibernate/JPA). 

Fetching a full @Entity from DB can be expensive. 
By default JPA eager loads all its fields and
all @ManyToOne/@OneToOne links, 
which adds unnecessary columns, JOINs 
and sometimes even additional SELECT queries.

If only a few fields are needed, 
it's more CPU/memory/network efficient 
to fetch only ose fields from the DB, 
especially when the entity is large, 
and we retrieve many of them, 
such as in a search flow.

Also, search flows usually involve very little logic in Java.
Creating an additional domain object to map the query results
doesn't provide any value, but requires more mapping and duplicated code. 

## Decision
Every search use-case will select directly DTOs instead of Entities.

That is, ❌DON'T DO:
```java
@Query("SELECT u FROM User u WHERE ...")
List<User> search(criteria); // ❌BAD
```

Use one of the following two options:
1) ✅ Instantiate the DTO from the JQPL query:
```java
@Query("SELECT new com.example.dto.UserDto(u.id, u.name) FROM User u WHERE ...")
List<UserDto> search(criteria);
```
The DTO must then have a constructor that accepts all fields:
a record, Lombok's @Value, or handwritten.

2) ✅ Use [Spring Projections](https://docs.spring.io/spring-data/jpa/reference/repositories/projections.html) for larger DTOs:
```java
@Query("SELECT u.id as id, u.name as fullName FROM User u WHERE ...")
List<UserDto> search(...);
```
Such a DTO needs to have getters and setters (eg. Lombok's @Data)
matching the column aliases ('id', and 'fullName' above).

## Consequences
Positive:
- Better performance - by reading less from DB.
- Less boilerplate (mapping, DTO) - the ORM directly populates the DTO object of the REST API.

Negative:
- Coupling - repository will depend on DTOs (REST API) => -> move it next to the controller.
- Risk - domain logic might grow in DTO, if applying logic after SELECT.
- Risk - update DB using DTOs, bypassing the integrity protection
enforced by the Domain Model/Services.

## Compliance
Enforcing is done via Code Review,
since we couldn't find yet any automated way to enforce it😢. (despite 2 hours research)

## Notes
- Author: Developer A 
- Changelog: 
  - 0.1 initial proposed version on 2023-11-01
