package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;

import java.util.Objects;

// 👑 Value Object design pattern
// = Immutable (small) lacking identity (PK)
@Embeddable
public record ShippingAddress(
    String city,
    String street,
    String zip) {
}
