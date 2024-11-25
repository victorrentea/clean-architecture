## Title
Don't use @Data on ORM @Entity classes

## Status
Accepted

## Context
We use Hibernate/JPA on our Domain Model.
We use Lombok to reduce the boilerplate of Java.

Lombok @Data generates problematic code for an @Entity:
- @Setters encourage an anemic Domain Model lacking behavior
- @ToString can trigger lazy-loading of collections, unless marked with @ToString.Excluded 
- @HashCodeAndEquals can trigger lazy-loading; hashCode changes when Hibernate assigns the @GeneratedValue @Id -> therefore, it's not stable. can lead to loosing elements in a HashSet/Map

## Decision
We will not use @Data on @Entity classes.

hashcode/equals on entities
- implement on a "natural key" (if exists), eg VAT code for a company, SSN for a person
- don't implement <<<<<
- exclude the @Id and children collections?


Voices:
- https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
- https://thorben-janssen.com/ultimate-guide-to-implementing-equals-and-hashcode-with-hibernate/
WIP