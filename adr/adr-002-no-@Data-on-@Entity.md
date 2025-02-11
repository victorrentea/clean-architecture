## Title
Don't use Lombok's @Data on ORM @Entity classes

## Status
Accepted

## Context
Our Domain Model entities are managed by JPA/Hibernate (ADR-001).  
We use Lombok to reduce Java's boilerplate.

Lombok @Data generates problematic code for our Domain Model:
1. **@HashCodeAndEquals**
   - can trigger **lazy-loading** of child collections (unless fields are marked with @HashCodeAndEquals.Excluded)
   - hashCode is **unstable**: it changes when Hibernate assigns @GeneratedValue @Id => can lead to loosing elements in a HashSet/Map
2. **@ToString** can trigger **lazy-loading** of child collections (unless fields are marked with @ToString.Excluded) 
3. **@Setters** encourage an **anemic** model lacking behavior

## Decision
We will not use @Data on @Entity classes. 

Instead:
1. HashCode/Equals will be implemented on the entity's natural key, if any (like VAT Code); otherwise, we will NOT implement hashCode/equals on entities and use the default implementation based on the instance's unique identifier in memory. 
2. ToString will only include the id and potentially a couple of more relevant fields for debugging.
3. We will prefer changing entity's state via dedicated mutator methods (not setters) everytime we can protect a domain invariant; we will use @Setter on the other fields.
4. @Getter is allowed on the classlevel (propagating on all fields)

**Side-note**: You don't need `@Builder` on a mutable `@Entity`, because we configured Lombok to generate setters that return `this` by adding `lombok.accessors.chain=true` in `lombok.config`. So you can now write: `new MyEntity().setA("a").setB("b");`

## Consequences
Positive:
- We avoid (common) potential lazy-loading issues
- We pay more attention to the design of our entities

Negative:
- We have to write more code for entities

## References
- https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
- https://thorben-janssen.com/ultimate-guide-to-implementing-equals-and-hashcode-with-hibernate/

## Compliance
Enforced by Code Review: pt ca nu am gasit un mod automat de a verifica asta :(

