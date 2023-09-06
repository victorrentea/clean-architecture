package victor.training.clean.domain.model;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.Embeddable;

@Getter
@ToString
@Embeddable
public class ShippingAddress {
  private String city;
  private String street;
  private Integer zipCode;

  protected ShippingAddress() {
  } // for Hibernate only

  // for developers
  public ShippingAddress(String city, String street, Integer zipCode) {
    this.city = city;
    this.street = street;
    this.zipCode = zipCode;
  }
}
