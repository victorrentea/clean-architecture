package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;

// value object = immutable class with no identity (PK)
@Embeddable
public record Address(String city, String street, String zip) {
}
