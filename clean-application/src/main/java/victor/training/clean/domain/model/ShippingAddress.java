package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;

import static java.util.Objects.requireNonNull;

// Value Object = small, immutable object that is used as a field in an Entity
// which brings more meaning to the DATA
@Embeddable // capture the concept of a ShippingAddress you are currently using in you req/PO/testes/FE.....
public record ShippingAddress(String city, String street, String zip) {
  public ShippingAddress {
    requireNonNull(city);
    requireNonNull(street);
    requireNonNull(zip);
  }
}
