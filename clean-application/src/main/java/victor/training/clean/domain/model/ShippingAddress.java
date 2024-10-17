package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;

// Value Object grouping values that make more sense together
// 1. Immutable final (ideally record)
// 2. no identity (no PK), no lifecycle, no continuity of change
@Embeddable
public record ShippingAddress(String city, String street, String zip) {
}
