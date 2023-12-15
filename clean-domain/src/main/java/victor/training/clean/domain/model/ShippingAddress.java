package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;

//@ValueObject // VALUE OBJECT ™️
@Embeddable
public record ShippingAddress(String city, String street, String zip) {
}
