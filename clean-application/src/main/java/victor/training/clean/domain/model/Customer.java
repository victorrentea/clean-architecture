package victor.training.clean.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
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
@Data // = @Getter @Setter
// @ToString= manually only for debugging
// @EqualsAndHashCode = avoid on entities
// @Getter = challenges encapsulation
// @Setter = prefer guarded mutators
 // data can be dangerous!
// exposes everything
// my suggestion in any non-trivial system:
// don't use @Data but it's parts:

//@Getter// better: not on the class but on the actual fields
@Entity // ORM/JPA (2) or @Document
public class Customer {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
//  @Getter
  private String email;

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
//  private String shippingAddressCity;
//  private String shippingAddressStreet;
//  private String shippingAddressZip;

  @Embedded // ideally no ALTER table should be needed (CUSTOMER is still flat)
  // you just changed the way you manipulate data in Java, in DB should remain the same
  private ShippingAddress shippingAddress;

//  private String billingAddressCity;
//  private String billingAddressStreet;
//  private String billingAddressZip;
//  private String billingVATCode;

  // class ShippingAddress <<< stick to specific by default
  // class Address if i can reuse it somewhere else TODAY!
    // risk=accidental coupling of it (reusing it wrongly) eg for VATCode
    // better: BillingInfo{address,VATCode}

  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

  public boolean isPhysicalPerson() {
    return getLegalEntityCode() == null;
  }

  // OOP: Encapsulation = data + logic
  // is an entity allowed to contain logic
  public boolean canReturnOrders() { // bits (1-3 lines of logic)
    // on top of MY data. that makes my object friendlier to use
    return isGoldMember() || isPhysicalPerson();
  }
  // BUT!!!!
  // it depends.... on what logic / what parameters
  // Examples of BAD functions in here:
  // f(CustomerRepo no) {
  // f(RecordingApi no) {
  // f(Order35Fields no) {
  // f(int ok)
  // f(Struct2field ok)
  // f() ideally 90%

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }
  @Setter(NONE)
  private Status status;
  @Setter(NONE)
  private String validatedBy; // ⚠🤞 Always not-null when status = VALIDATED or later
//  public void updateStatus(Status newStatus, String username) {
//    status= newStatus;
//    validatedBy = username;
//  }

  public Customer setStatus(Status status) {
    if (validatedBy == null && status == Status.VALIDATED)
      throw new IllegalArgumentException("How dare you forget that you SHALL CALL setValdiatedBy BEFORE");
    this.status = status;
    return this;
  }

  public Customer setValidatedBy(String validatedBy) {
    this.validatedBy = validatedBy;
    return this;
  }
}

//region Code in the project might [not] follow the rule
class CodeFollowingTheRule {
  public void ok(Customer draftCustomer) {
    // temporal coupling = order of calls matters for no obvious reason
    draftCustomer.setValidatedBy("currentUser"); // from token/session..
    draftCustomer.setStatus(Customer.Status.VALIDATED);
  }
}
class CodeBreakingTheRule {
  public void farAway(Customer draftCustomer) {
    draftCustomer.setStatus(Customer.Status.VALIDATED);
  }
}
//endregion