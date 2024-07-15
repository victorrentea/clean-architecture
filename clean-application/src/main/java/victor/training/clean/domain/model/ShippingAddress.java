package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;

@Embeddable
public record ShippingAddress(String city, String street, String zip) {}
