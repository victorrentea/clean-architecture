package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.Objects;

//(immutable❤️)
//record in Hib 6.2
@Embeddable
@Getter // nu pot @Value pt ca face campurile finale
public class ShippingAddress {
  // lack "continuity of change" = PK
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

  @Override
  public String toString() {
    return "ShippingAddress{" +
           "city='" + city + '\'' +
           ", street='" + street + '\'' +
           ", zipCode=" + zipCode +
           '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ShippingAddress that = (ShippingAddress) o;
    return Objects.equals(city, that.city) && Objects.equals(street, that.street) && Objects.equals(zipCode, that.zipCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(city, street, zipCode);
  }
}
