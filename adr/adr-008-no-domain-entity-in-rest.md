## ADR: Don’t Return Entities in REST

Status

✅ Accepted

Context

We were exposing domain entities (Customer) directly from REST controllers:

@GetMapping("{id}")
public Customer findById(@PathVariable long id)

That’s bad.

Decision

❌ No more entities in REST.
✅ Always return DTOs.
Use mappers to convert between entity ↔ DTO.

Why
• 🚫 Don’t leak internal model
• 🔐 Control what’s exposed
• 🔄 Freely evolve domain
• 📉 Avoid breaking clients

Tradeoff

👎 Adds boilerplate
👍 Long-term safety + flexibility

Example
Change this:

- public Customer findById(...)
  To this:
- public CustomerDto findById(...)

Compliance
Enforced via an ArchUnit.