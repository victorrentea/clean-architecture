package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;

@Embeddable // these fields should be stored in the same table as the entity
public record ShippingAddress(String city, String street, String zip) {
  // optimization on the Java side of the ORM for cleaner model
}
