package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;

@Embeddable
// Value Object Design Pattern = Immutable, no identity, no lifecycle
public record ShippingAddress(String city, String street, String zip) {}
