package victor.training.clean.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.NONE;
import static victor.training.clean.domain.model.Customer.Status.DRAFT;
import static victor.training.clean.domain.model.Customer.Status.VALIDATED;

@Embeddable
record ShippingAddress(String city, String street, String zip) {
}

class ShippingAddress2 {
  public final String city;
  public final String street;
  public final String zip;

  ShippingAddress2(String city, String street, String zip) {
    this.city = city;
    this.street = street;
    this.zip = zip;
  }
}

@Entity
@Data // avoid on ORM @Entity because:
// 1) hashCode uses @Id⚠️ 2) toString triggers lazy-loading⚠️ 3) all setters = no encapsulation⚠️
public class Customer {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String email;

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
  private String shippingAddressCity; // TODO delete
  private String shippingAddressStreet; // TODO delete
  private String shippingAddressZip; // TODO delete

  @Embedded // This does NOT mean to change the DB schema.
  private ShippingAddress shippingAddress;

  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

  //bits of domain logic INSIDE the Domain Model
  public int discountPercentage() {
    int discountPercentage = 1;
    if (goldMember) {
      discountPercentage += 3;
    }
    return discountPercentage;
  }

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }
  @Setter(NONE)
  private Status status = DRAFT;
  @Setter(NONE) // stops the @Data/@Setter on the class to generate a setter for this field
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later

  public Status status() {
    return status;
  }
//  static {
//    Customer c;
//    c.status();
//    System.out.println(c.readOnlyField()); // ZERO performance impact thanks to JIT optimized
//    System.out.println(c.readOnlyField); // expose public final field?
//  }

  // guarded mutation methods
  public void validate(String validatedBy) {
    if (status != DRAFT) {
      throw new IllegalStateException("Can't validate a non-draft customer");
    }
    status = VALIDATED;
    this.validatedBy = requireNonNull(validatedBy);
  }
  public void activate() {
    if (status != VALIDATED) {
      throw new IllegalStateException("Can't activate a non-validated customer");
    }
    status = Status.ACTIVE;
  }
  public void delete() {
    if (status != Status.ACTIVE) {
      throw new IllegalStateException("Can't delete a non-active customer");
    }
    status = Status.DELETED;
  }
}

//region Code in the project might [not] follow the rule
class CodeFollowingTheRule {
  public void ok(Customer draftCustomer) {
//    draftCustomer.setStatus(VALIDATED);
//    draftCustomer.setValidatedBy("currentUser"); // from token/session..
    draftCustomer.validate("currentUser");
  }
}
class CodeBreakingTheRule {
  public void farAway(Customer draftCustomer) {
//    draftCustomer.setStatus(VALIDATED);
  }
}
//endregion