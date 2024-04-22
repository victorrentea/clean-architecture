package victor.training.clean.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

import static java.util.Objects.requireNonNull;

@Entity // should I use ORM on Domain Model?

// 👑 Sacred Domain Object
@Data // is bad because
// - blind @Getter, @Setter for all = no encapsulation
// - @ToString on all fields -> might trigger Lazy-Loading
// - hashCode/equals uses @Id⚠️


public class Customer {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String email;
//  @OneToMany
//  List<Child> childList;

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
//  private String shippingAddressCity;
//  private String shippingAddressStreet;
//  private String shippingAddressZip;

  @Embedded
  private ShippingAddress shippingAddress;

  // few total fields -> better things to do
  // too many fields (PAIN eg 16 fields) -> extract

//  private String billingAddressVATCode;
//  private String billingAddressCity;
//  private String billingAddressStreet;
//  private String billingAddressZip;

  @ManyToOne
  private Country country;

//  @Getter(AccessLevel.NONE) // bye-bye getter (in practice, not often I can do this)
  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

  //

  // it depends...
  // - Does this responsibility BELONG to the customer? But if code is small (MVP) - KISS
  public int getDiscountPercentage() {
    int discountPercentage = 1;
    if (goldMember) {
      discountPercentage += 3;
    }
    return discountPercentage;
  }

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }
  private Status status;
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later
}

//region Code in the project might [not] follow the rule
//class CodeFollowingTheRule {
//  public void ok(Customer draftCustomer) {
//    draftCustomer.setStatus(VALIDATED);
//    draftCustomer.setValidatedBy("currentUser"); // from token/session..
//  }
//}
//class CodeBreakingTheRule {
//  public void farAway(Customer draftCustomer) {
//    draftCustomer.setStatus(VALIDATED);
//  }
//}
//endregion