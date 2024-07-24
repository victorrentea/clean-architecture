package victor.training.clean.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

import static java.util.Objects.requireNonNull;
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
  @Embedded // the DATABASE SCHEMA (TABLE STRUCTURE) DOES NOT HAVE TO CHANGE!!
  private ShippingAddress shippingAddress;

  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

  public boolean isPhysicalPerson() { // ideal
    return legalEntityCode == null;
    // don't shovel here 12 lines of hard-core biz logc
  }

//  public boolean foo(boolean okish?) {
//  public boolean foo(int perhaps) {
//  public boolean foo(SmallStructure perhaps) {

  // NO
//  public boolean foo(DataStructure12Fields noWay) {
//  public boolean foo(DtoOfAnotherAPI domainCorruption) {
//  public boolean foo(CustomerRepo ohShitThisWillNeedMocking) { // WE NEVER MOCK DATA STRUCTURES!
//  public boolean foo(LdapApiClient canCallOut) {


  public boolean canReturnOrders() {
    return isGoldMember() || isPhysicalPerson();
  }

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }
  @Setter(NONE)
  private Status status = Status.DRAFT;
  @Setter(NONE)
  private String validatedBy; // ⚠ Always(🤞HDD) not-null when status = VALIDATED or later

  public void validate(String validatedBy) {
    if (status != Status.DRAFT) {
      throw new IllegalStateException("Can only validate a DRAFT customer");
    }
    this.validatedBy = requireNonNull(validatedBy);
    status = Status.VALIDATED;
  }
  public void activate() {
    if (status != Status.VALIDATED) {
      throw new IllegalStateException("Can only activate a VALIDATED customer");
    }
    status = Status.ACTIVE;
  }
  public void delete() {
    if (status != Status.ACTIVE && status != Status.DRAFT) {
      throw new IllegalStateException("Can only delete an ACTIVE customer");
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
    draftCustomer.validate(null);
  }
}
//endregion