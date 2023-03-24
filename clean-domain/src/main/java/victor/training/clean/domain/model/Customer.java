package victor.training.clean.domain.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
class Address {
  private String city;
  private String street;
  private Integer zipCode;

  protected Address() {
  } // for Hibernate only

  public Address(String city, String street, Integer zipCode) {
    this.city = Objects.requireNonNull(city);
    this.street = Objects.requireNonNull(street);
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
}

@Entity
@Data // Avoid on @Entity
public class Customer {
  @Id
  @GeneratedValue
  private Long id;
  @NotNull
  @Size(min = 5)
  private String name;
  @Embedded
  private Address shippingAddress;

  @NotNull
  private String email;
  private LocalDate creationDate;
  private boolean goldMember;
  @ManyToOne
  private Site site;

  protected Customer() {
  } // for Hibernate only

  public Customer(String name) {
    this.name = Objects.requireNonNull(name);
    this.email = Objects.requireNonNull(email);
  }


  public int getDiscountPercentage() {
    int discountPercentage = 3;
    if (isGoldMember()) {
      discountPercentage += 1;
    }
    return discountPercentage;
  }
}
