package victor.training.clean.crm.domain.model;

import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

//@Embeddable
@AllArgsConstructor
public class CustomerAddress {
  private String city;
  private String streetAddress;
  private String addressLine2;
  private String zipCode;
  private String country;

  protected CustomerAddress() {} // for Hibernate
//  public CustomerAddress(String city, String streetAddress, String addressLine2, String zipCode, String country) { // NOSONAR
//    this.city = Objects.requireNonNull(city);
//    this.streetAddress = Objects.requireNonNull(streetAddress);
//    this.addressLine2 = addressLine2;
//    this.zipCode = zipCode;
//    this.country = Objects.requireNonNull(country);
//  }

  public String getCity() {
    return city;
  }

  public String getStreetAddress() {
    return streetAddress;
  }

  public Optional<String> getAddressLine2() {
    return Optional.ofNullable(addressLine2);
  }

  public Optional<String> getZipCode() {
    return Optional.ofNullable(zipCode);
  }

  public String getCountry() {
    return country;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CustomerAddress that = (CustomerAddress) o;
    return Objects.equals(city, that.city) && Objects.equals(streetAddress, that.streetAddress) && Objects.equals(addressLine2, that.addressLine2) && Objects.equals(zipCode, that.zipCode) && Objects.equals(country, that.country);
  }

  @Override
  public int hashCode() {
    return Objects.hash(city, streetAddress, addressLine2, zipCode, country);
  }
}
