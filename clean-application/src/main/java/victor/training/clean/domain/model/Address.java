package victor.training.clean.domain.model;

import javax.persistence.Embeddable;

@Embeddable // effectively immutable Value Object stored in a Domain @Entity
public class Address {
  private String city;
  private String street;
  private Integer zipCode;

  protected Address() {
  } // for Hibernate only

  Address(String city, String street, Integer zipCode) {
    this.city = city;
    this.street = street;
    this.zipCode = zipCode;
  }

  public String getStreet() {
    return street;
  }

  public String getCity() {
    return city;
  }

  public Integer getZipCode() {
    return zipCode;
  }
}
