package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;

// 💎 Value Object: immutable, no identity (PK), defined only by its fields (hash/eq pe toate campurile).
// so the concept has a name you can pass around, validate and test in isolation.
@Embeddable // stored in the same table as Customer (no separate table, no FK)
public record ShippingAddress(
        String city,
        String street,
        String zip
) {
}
