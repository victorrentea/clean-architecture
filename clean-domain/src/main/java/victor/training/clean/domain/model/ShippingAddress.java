package victor.training.clean.domain.model;

import java.util.Objects;

//class CustomerShippingAddress { // e mai cohesive, mai putin refolosibil, dar apare coupling,
//  si maine produsu vrea inca 3 attr da' doar pt customer shipping nu si pt Company.shippingAddress
//class Address {// prea vag
//@Value // Java
public class ShippingAddress {// reusable : Customer.shippingAddress dar si Company.shippingAddress
  private final String city;
  private final String street;
  private final Integer zipCode;
// Value Object (design pattern)
  // - obiect fara identitate persistenta (continuity of change), spre deosebire de un Entity
  // - equals (hashCode) se implem pe toate campurile
  // - imutabil (readonly/final)
  // - de obicei mic

  ShippingAddress(String city, String street, Integer zipCode) {
    this.city = city;
    this.street = street;
    this.zipCode = zipCode;
  }

  public Integer getZipCode() {
    return zipCode;
  }

  public String getCity() {
    return city;
  }

  public String getStreet() {
    return street;
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
