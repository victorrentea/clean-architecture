## Title

Don't Return Entities in REST

## Status

Accepted

## Context

We were exposing domain entities (Customer) directly from REST controllers:

```java

@GetMapping("{id}")
public Customer findById(@PathVariable long id)
```

That's bad.

## Decision

- Don't expose internal domain entities in REST.
- Always return DTOs.
- Use mappers to convert between entity ↔ DTO.

## Consequences

(TODO in training)
Positive:

-

Negative:
- 

## Example

Change this:

```java
@GetMapping(..)

public Customer get(id) {..}
```

To this:

```java
@GetMapping(..)

public CustomerDto get(id) {..}
```

## Compliance

Enforced via an ArchUnit.

## Notes

- Author: Victor Rentea
- Changelog:
    - Initial version