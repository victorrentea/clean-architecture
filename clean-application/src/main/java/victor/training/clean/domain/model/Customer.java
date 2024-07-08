package victor.training.clean.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;

import static lombok.AccessLevel.NONE;

//region Reasons to avoid @Data on Domain Model
// Avoid @Data on Domain Model because:
// 1) hashCode uses @Id⚠️
// 2) toString might trigger lazy-loading⚠️
// 3) all setters/getters = no encapsulation⚠️
//endregion

// This class is part of your Domain Model, the backbone of your core complexity.
@Data // = @Getter @Setter @ToString @EqualsAndHashCode (1)
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

  @Embedded // did not change the DB schema (no ALTER TABLE)
  private ShippingAddress shippingAddress;
  // the DOMAIN MODEL should remain UNDER YOUR STRICT EXCLUSIVE CONTROL.
  // no one should be able to stop you from refactoring it as you see fit.

  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;


  // RULE: YOU SHALL HAVE NO LOGIC IN THE DATA STRUCTURES (=PTSD)
  public boolean isPhysicalPerson() {
    return getLegalEntityCode() == null;
  }

  public boolean canReturnOrders() {
    return isGoldMember() || isPhysicalPerson();
  }

  // f(boolean b) - AVOID??±
  // f(CustomerService omg) - AVOID
  // f(CustomerRepo/ApiClient omg) - AVOID
  // f(LargeOtherEntity34Fields contract) - AVOID; can grow too large
  // f(Supplier<X> lambdaKungFu) - AVOID

  public boolean isActive() { // 💖
    return status == Status.ACTIVE;
  }


  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }
  @Setter(NONE)
  private Status status = Status.DRAFT;
  @Setter(NONE)
  private String validatedBy; // 🤞HDD: ⚠ Always not-null when status = VALIDATED or later

  // guarded mutator
  public void validate(String validatedBy) {
    if (status != Status.DRAFT) {
      throw new IllegalStateException("Can't validate a non-draft customer");
    }
    status = Status.VALIDATED;
    this.validatedBy = validatedBy;
  }

  public void activate() {
    if (status != Status.VALIDATED) {
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
//    draftCustomer.setStatus(Customer.Status.VALIDATED);
//    draftCustomer.setValidatedBy("currentUser"); // from token/session..
    draftCustomer.validate("currentUser");

  }
}

class CodeBreakingTheRule {
  public void farAway(Customer draftCustomer) {
//    draftCustomer.setStatus(Customer.Status.VALIDATED);
    draftCustomer.validate("currentUser");
  }
}
//endregion