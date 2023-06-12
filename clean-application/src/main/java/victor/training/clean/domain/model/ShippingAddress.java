package victor.training.clean.domain.model;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class ShippingAddress {
  private String city;
  private String street;
  private Integer zipCode;

  protected ShippingAddress() {
  } // for Hibernate only

  public ShippingAddress(String city, String street, Integer zipCode) {
    this.city = city;
    this.street = street;
    //this.city = Objects.requireNonNull(city);
    //    this.street = Objects.requireNonNull(street);
    this.zipCode = zipCode;
  }
}
