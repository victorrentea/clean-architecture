package victor.training.clean.domain.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.util.Objects.requireNonNull;
@Embeddable // effectively immutable Value Object stored in a Domain @Entity
class Address {
  private String city;
  private String street;
  private Integer zipCode;

  protected Address() {} // for Hibernate only

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

//@Configurable // dont !! allows injection of @Autowired into hibernatet entity DONT!!
@Entity
@Data // TODO remove:
// Avoid lombok @Entity on ORM Domain @Entity
// 1) hashCode on @Id [ORM]
// 2) toString lazy-loading collections [ORM]
// 3) setters for everything = lack of encapsulation
public class Customer {
  @Id
  @GeneratedValue
  private Long id;
  @Size(min = 5, message = "{customer-name-too-short}") // yes
//  @CustomerEmail
  private String name;
  @Email
  private String email;


  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
//  private String shippingAddressCity;
//  private String shippingAddressStreet;
//  private Integer shippingAddressZipCode;
  @Embedded // the fields inside this object are stored in the CUSTOMER table
  private Address shippingAddress;

  @ManyToOne
  private Country country;

  private LocalDate creationDate;
  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

  public int getDiscountPercentage() {
    int discountPercentage = 1;
    if (goldMember) {
      discountPercentage += 3;
    }
    return discountPercentage;
  }


//  enum Status {DRAFT, ACTIVE, INACTIVE}
//
//  Status status = Status.DRAFT;
//  String activatedBy;
//
//  public void activate(String user) {
//    if (status != Status.DRAFT) {
//      throw new IllegalStateException();
//    }
//    status = Status.ACTIVE;
//    activatedBy = user;
//  }
//  public Customer setStatus(Status status) {
//    this.status = status;
//    return this;
//  }
  //  public Customer(String name) {
////    if (name.length() < 5) {
////      throw new IllegalArgumentException();
////    }
//    this.name = name;
//    Validator.validate(this);
//    throw new RuntimeException("Method not implemented");
//  }


  // Domain (entity or service) cannot know of the API (my, external)
//  private CustomerDto fromDto(int discountPercentage) {
//    return CustomerDto.builder()
//        .id(customer.getId())
//        .name(customer.getName())
//        .email(customer.getEmail())
//        .countryId(customer.getCountry().getId())
//        .creationDateStr(customer.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
//        .gold(customer.isGoldMember())
//        .discountPercentage(discountPercentage)
//        .goldMemberRemovalReason(customer.getGoldMemberRemovalReason())
//        .legalEntityCode(customer.getLegalEntityCode())
//        .discountedVat(customer.isDiscountedVat())
//        .build();
//  }

  // criminal act:
//  @Autowired
//  CustomerRepo repo;
//  public void save() {
//    repo.save(this);
//  }

  // MVC
//  public String streetAddress() {
//    return shippingAddressStreet + " " + shippingAddressCity;// presentation concern
//  }
}
