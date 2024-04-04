package victor.training.clean.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static java.util.Objects.requireNonNull;
import static victor.training.clean.domain.model.Customer.Status.*;

@Entity
//@Data // avoid on ORM @Entity because:
@Getter
@Setter
// 1) hashCode uses @Id⚠️
// 2) toString prints everything + ORM can triggers lazy-loading⚠️
// 3) all setters = no encapsulation⚠️
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

  @Embedded // The DB table structure will NOT change
  private ShippingAddress shippingAddress; // the 3 columns remain in the CUSTOMER table

  @ManyToOne
  private Country country;

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

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }
  @Setter(AccessLevel.NONE)
  private Status status = DRAFT;
  @Setter(AccessLevel.NONE)
//  @JsonIgnore
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later
//  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate validatedOn;

//  private String bannedByWhoAddress;
//  public void setStatus(Status newStatus) {
//    status = newStatus;
//    if (newStatus == VALIDATED) {
//      requireNonNull(validatedBy, "validatedBy must be set");
//    }
//  }

  public Status status() {
    return status;
  }

  // guarded mutators
  public void validate(String validatedBy) {
    if (status != DRAFT) {
      throw new IllegalStateException("Can only validate DRAFT customers");
    }
    requireNonNull(validatedBy, "validatedBy must be set");
    status = VALIDATED;
    this.validatedBy = validatedBy;
  }
  public void activate() {
    if (status != VALIDATED) {
      throw new IllegalStateException("Can only activate VALIDATED customers");
    }
    status = Status.ACTIVE;
  }
  public void delete() {
    if (status != ACTIVE) {
      throw new IllegalStateException("Can only delete ACTIVE customers");
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
//    draftCustomer.setValidatedBy(null);
    draftCustomer.validate("null");
  }
}
//endregion