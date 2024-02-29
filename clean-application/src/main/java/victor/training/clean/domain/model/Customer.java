package victor.training.clean.domain.model;

import lombok.AccessLevel;
import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Setter;

import java.time.LocalDate;

import static java.util.Objects.requireNonNull;

@Entity // ORM
@Data // BAD: 1) hashCode uses @Id, 2) toString can trigger ORM lazy-loading, 3) setters for all fields = no encapsulation
public class Customer {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String email;

  private ShippingAddress shippingAddress;
  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

  public int discountPercentage() {
    int discountPercentage = 1;
    if (goldMember) {
      discountPercentage += 3;
    }
    return discountPercentage;
  }
  // logic in the structure?
  // +discoverability
  // +simpler code inside
  // +guard domain constrains

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }
  @Setter(AccessLevel.NONE)
  private Status status = Status.DRAFT;
  @Setter(AccessLevel.NONE)
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later

  public void validate(String who) { // guarded mutations
    if (status != Status.DRAFT) {
      throw new IllegalStateException();
    }
    status = Status.VALIDATED;
    validatedBy = who;
  }

  public void activate() {


  }

}
//
////region Code in the project might [not] follow the rule
//class CodeFollowingTheRule {
//  public void ok(Customer draftCustomer) {
//    draftCustomer.setStatus(Customer.Status.VALIDATED);
//    draftCustomer.setValidatedBy("currentUser"); // from token/session
//  }
//}
//class CodeBreakingTheRule {
//  public void farAway(Customer draftCustomer) {
//    draftCustomer.setStatus(Customer.Status.VALIDATED);
//  }
//}
////endregion