package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;

//VO = Value Object = Immutable si fara PK
@Embeddable
public record ShippingAddress(
    String city,
    String street,
    String zip) {}
