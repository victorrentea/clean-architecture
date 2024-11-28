package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable; /// Value Object (design pattern) : immutable, no identity, no lifecycle
//record Address < reusable💖 later
@Embeddable
public record ShippingAddress (String street, String city, String zip) {}
