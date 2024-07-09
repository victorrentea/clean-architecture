package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;

@Embeddable
// VALUE OBJECT design pattern: Immutable, no persistent identity (no PK), equals/hashCode based on all fields
public record ShippingAddress(
    String city,
    String street,
    String zip
) {}
