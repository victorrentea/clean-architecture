package victor.training.clean.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

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
  @Embedded
  private Username createdByUsername;

  private boolean goldMember;
//  @JsonIgnore WRONG!!
  private String goldMemberRemovalReason;


//  private String bannedByWhoPhoneNUmber;

  private String legalEntityCode;
  private boolean discountedVat;

  //
  @Setter(AccessLevel.NONE)
  private Status status = Status.DRAFT;
  @Setter(AccessLevel.NONE)
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later

   // protected  to allow Hibernate to subclass to create proxies
  protected Customer() { // for hibernate
  }
  public Customer(String name) {
    this.name = requireNonNull(name);
  }
  // it depends...
  // - Does this responsibility BELONG to the customer? But if code is small (MVP) - KISS
  public int getDiscountPercentage() {
    int discountPercentage = 1;
    if (goldMember) {
      discountPercentage += 3;
    }
    return discountPercentage;
  }

  // guarded mutators
  public void validate(String currentUser) {
    requireNonNull(currentUser);
    if (status != Status.DRAFT) {
      throw new IllegalStateException("Can't validate a non-DRAFT customer");
    }
    status = Status.VALIDATED;
    validatedBy = currentUser;
  }

  public void activate() {
    if (status != Status.VALIDATED) {
      throw new IllegalStateException("Can't activate a non-VALIDATED customer");
    }
    status = Status.ACTIVE;
  }

  public void delete() {
    if (status != Status.ACTIVE) {
      throw new IllegalStateException("Can't delete a non-ACTIVE customer");
    }
    status = Status.DELETED;
  }

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
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

// over the hills and far away
class CodeBreakingTheRule {
  public void farAway(Customer draftCustomer) {
//    draftCustomer.setStatus(Customer.Status.VALIDATED);
    draftCustomer.validate(null);
  }
}
//endregion