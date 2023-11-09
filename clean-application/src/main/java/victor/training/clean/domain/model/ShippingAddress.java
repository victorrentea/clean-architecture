package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;

//(imuutable❤️)
//record in Hib 6.2
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
    this.zipCode = zipCode;
  }
}
