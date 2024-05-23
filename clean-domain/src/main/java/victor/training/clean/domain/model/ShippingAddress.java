package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;

// VALUE OBJECT = Immutable, no identity, no lifecycle
@Embeddable
public record ShippingAddress(
    String city,
    String street,
    String zip) {
}
