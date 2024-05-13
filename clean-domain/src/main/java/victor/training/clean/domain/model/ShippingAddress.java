package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;


// Value Object = Immutable, no identity (continuity), no lifecycle
// the more value objects you identify, the better.
@Embeddable
public record ShippingAddress(
    String city,
    String street,
    String zip) {}
