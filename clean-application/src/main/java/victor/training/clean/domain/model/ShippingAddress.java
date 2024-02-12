package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;

import static java.util.Objects.requireNonNull;

// VAlue Object = Immutable + No Identity
@Embeddable
public record ShippingAddress(String city, String street, String zip) {
  public ShippingAddress {
    requireNonNull(city);
    requireNonNull(street);
    requireNonNull(zip);
  }
}
