package victor.training.clean.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.NONE;

// level2: ask: do I really have a single Customer? or should I break the entire @Entity: CustomerContactDetails, CustoemrShippingDetails, CustomerBillingDetails
@Entity // hibernate
@Data // BAD: 1) hashCode uses @Id, 2) toString can trigger ORM lazy-loading, 3) setters for all fields = no encapsulation
public class Customer {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String email;

//  private List<Phones>

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
//  private String shippingAddressCity;
//  private String shippingAddressStreet;
//  private String shippingAddressZip;
  @Embedded // the fields of ShippingAddress will be columns in the Customer table
  private ShippingAddress shippingAddress;

  @ManyToOne
  private Country country;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate createdDate;
  private String createdByUsername;

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

  public enum Status { // the state transitions are DRAFT->VALIDATED->[ACTIVE]->DELETED
    DRAFT, VALIDATED, ACTIVE, DELETED
  }
  @Setter(NONE)
  private Status status;
  @Setter(NONE)
  @JsonIgnore
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later

  public void validate(String user) {
    if (status != Status.DRAFT) {
      throw new IllegalStateException("Can only validate DRAFT customers");
    }
    status = Status.VALIDATED;
    validatedBy = requireNonNull(user);
  }
  public void activate() {
    if (status != Status.VALIDATED) {
      throw new IllegalStateException("Can only activate VALIDATED customers");
    }
    status = Status.ACTIVE;
  }
  public void delete() {
    if (status != Status.ACTIVE) {
      throw new IllegalStateException("Can only delete ACTIVE customers");
    }
    status = Status.DELETED;
  }

}

//region Code in the project might [not] follow the rule
class CodeFollowingTheRule {
  public void ok(Customer draftCustomer) {
    draftCustomer.validate("currentUser");
//    draftCustomer.setStatus(VALIDATED);
//    draftCustomer.setValidatedBy("currentUser"); // from token/session
  }
}
class CodeBreakingTheRule {
//  public void farAway(Customer draftCustomer) {
//    draftCustomer.setStatus(ACTIVE);
//  }
}
//endregion