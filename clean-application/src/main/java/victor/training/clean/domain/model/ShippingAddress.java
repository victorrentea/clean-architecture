package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;

// Value Object = Immutable small object that is used to carry data.
// Does not have an identity (PK).
// eg: Money{amount, currency}
@Embeddable
public record ShippingAddress(String city, String street, String zip) {}
