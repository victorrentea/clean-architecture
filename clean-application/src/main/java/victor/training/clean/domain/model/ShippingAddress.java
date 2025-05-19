package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;

// extracted a concept into a new
// Value Object (DDD Design Pattern) = no persistent ID, immutable, typically small
// the more of these you create, the smarter you are, the more semantic the code becomes
@Embeddable // the DB schema does not change
public record ShippingAddress(
    String city,
    String street,
    String zip
) {}
