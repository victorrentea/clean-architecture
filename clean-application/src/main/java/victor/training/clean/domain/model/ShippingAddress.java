package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;

@Embeddable
// Value Object = Immutable, no identity(no PK) => no lifecycle
// no "continuity of change"
public record ShippingAddress(
    String city,
    String street,
    String zip
) {}
