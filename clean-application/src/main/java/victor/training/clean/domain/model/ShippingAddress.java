package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;

// every class you write in Java should be record
// unless an ORM @Entity or Spring @Service&friends
@Embeddable
// Value Object = small, immutable, lacking identity (no continuity of change)
// the more VO you identify the DEEPER and more SEMANTIC RICH becomes your domain model
public record ShippingAddress(
    String city,
    String street,
    String zip
) {}
