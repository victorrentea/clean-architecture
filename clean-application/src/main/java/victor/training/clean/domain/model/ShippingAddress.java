package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;

// Value Object= Immutable, no identity(no PK no continuity of change), usually small
@Embeddable
public record ShippingAddress(
    String city, 
    String street, 
    String zip) {}
