package victor.training.clean.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

// 💖Value Object (design pattern)
//    = Obiect mic Immutable fara ID(PK)
//    eg: Money{amount, currency}
//@Value // = getters + final fields + equals/hashCode + toString + constructor
// pt java8 nostalgici
//class ShippingAddress {
//  String city;
//  String street;
//  String zip;
//}


//region Reasons to avoid @Data on Domain Model
// Avoid @Data on Domain Model because:
// 1) hashCode uses @Id⚠️
// 2) toString might trigger lazy-loading⚠️
// 3) all setters/getters = no encapsulation⚠️
//endregion

// This class is part of your Domain Model, the backbone of your core complexity.
//@Data // = @Getter
// @Setter = imi bat joc ca de typedef struct, nu OOP
// @ToString = lazy loading log.trace("Uite: " + customer);
// @EqualsAndHashCode = nu pe entitati pt ca:
//     poate triggera lazy-loading pe liste
//     incorect pe @Id> poti pune @Exclude
@Getter
@Setter
@Entity // ORM/JPA (2)
public class Customer {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String email;

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
//  private String shippingAddressCity;
//  private String shippingAddressStreet;
//  private String shippingAddressZip;

  @Embedded // ORM = lasa schema neatinsa, nici un alter table
  private ShippingAddress shippingAddress;
//  @Embedded // ORM = lasa schema neatinsa, nici un alter table
//  private ShippingAddress invoiceAddress;

  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

  public boolean canReturnOrders() {
    return goldMember || isPhysicalPerson();
  }

  public boolean isPhysicalPerson() {
    return legalEntityCode == null;
  }

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }

  @Setter(AccessLevel.NONE)
  private Status status = Status.DRAFT;
  @Setter(AccessLevel.NONE)
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later

  public void validate(String user) {
    if (status != Status.DRAFT) {
      throw new IllegalStateException("Can't validate a non-DRAFT Customer");
    }
    status = Status.VALIDATED;
    validatedBy = Objects.requireNonNull(user);
  }

  public void activate() {
    if (status != Status.VALIDATED) {
      throw new IllegalStateException("Can't activate a non-VALIDATED Customer");
    }
    status = Status.ACTIVE;
  }

  public void delete() {
    if (status != Status.ACTIVE) {
      throw new IllegalStateException("Can't delete a non-ACTIVE Customer");
    }
    status = Status.DELETED;
  }
}


//region Code in the project might [not] follow the rule
class SomeCode {
  public void correct(Customer draftCustomer) {
//    draftCustomer.validate(null);
    draftCustomer.validate("currentUser");
//    draftCustomer.setStatus(Customer.Status.VALIDATED, user);
//    draftCustomer.setValidatedBy("currentUser"); // from token/session..
  }
  public void incorrect(Customer draftCustomer) {
    draftCustomer.validate("null");
//    draftCustomer.setStatus(Customer.Status.VALIDATED, user);
  }
  public void activate(Customer draftCustomer) {
//    draftCustomer.setStatus(Customer.Status.ACTIVE);
    draftCustomer.activate(); // OOP
  }
}
//endregion