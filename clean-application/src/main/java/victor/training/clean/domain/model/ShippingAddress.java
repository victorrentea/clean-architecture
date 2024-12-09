package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;

// Value Object = immutable, lacking PK ± part of an Entity
@Embeddable
public record ShippingAddress(
    String city,
    String street,
    String zip) {}
