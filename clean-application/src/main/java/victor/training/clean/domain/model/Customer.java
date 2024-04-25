package victor.training.clean.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Setter;
import victor.training.clean.domain.repo.CustomerRepo;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Objects;

import static lombok.AccessLevel.NONE;


//region Reasons to avoid @Data on Domain Model
// Avoid @Data on Domain Model because:
// 1) hashCode uses @Id⚠️
// 2) toString might trigger lazy-loading⚠️
// 3) all setters/getters = no encapsulation⚠️
//endregion

// This class is part of your Domain Model, the backbone of your core complexity.
@Data //(1) =
// @Getter++ @Setter = screw encapsulation
// @ToString = can trigger LazyLoading and StackOverflowException when circular references
// @EqualsAndHashCode = don;t implement hash/eq on domain model/@Entity

@Entity // ORM/JPA (2)

@SequenceGenerator(name = "CUSTOMER_SEQ", sequenceName = "CUSTOMER_SEQ", allocationSize = 1)
public class Customer {
  @Id
  @GeneratedValue(generator = "CUSTOMER_SEQ")
  private Long id;
  @Size(min = 5) // allows null WTF?!!
  @NotBlank //
  private String name;
  @Email // violation of DRY for the sake of consistency.
  private String email;

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
//  private String shippingAddressCity;
//  private String shippingAddressStreet;
//  private String shippingAddressZip;
  @Embedded // did not change the DB schema
  private ShippingAddress shippingAddress;

  @ManyToOne
  private Country country;

  @JsonIgnore
  private LocalDate createdDate;

//  public String getDateFromatted(boolean bull) {
//    return "</b>" + DateFormat.getDateInstance().format(createdDate)+"</b>";
//  }
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

  @Setter(NONE)
  private Status status = Status.DRAFT;
  @Setter(NONE)
  private String validatedBy;



  protected Customer() {
  } // for hibernate only

  public Customer(String name) {
    this.name = Objects.requireNonNull(name);
  }

  public boolean canReturnOrders() {
    return goldMember || legalEntityCode == null;
  }

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }

  public void validate(String validatedBy) {
    if (status != Status.DRAFT) {
      throw new IllegalStateException("Can only validate DRAFT customers");
    }
    this.validatedBy = Objects.requireNonNull(validatedBy);
    status = Status.VALIDATED;
  }

  public void activate() {
    if (status != Status.VALIDATED) {
      throw new IllegalStateException("Can only activate VALIDATED customers");
    }
    status = Status.ACTIVE;
  }

  public void delete() {
    if (status != Status.ACTIVE && status != Status.VALIDATED) {
      throw new IllegalStateException("Can only deactivate ACTIVE customers");
    }
    status = Status.DELETED;
  }

//  public Customer setStatus(Status status) {
//    if (status == Status.VALIDATED) { // BAD TEMPORAL COUPLING OF THE CLIENT CODE
  // they have to remember to set validatedBy BEFORE!!
//      Objects.requireNonNull(validatedBy);
//    }
//    this.status = status;
//    return this;
//  }

//  @PrePersist @PreUpdate // BAD
//  public void checkValidatedBy() {
//    if (status == Status.VALIDATED) {
//      Objects.requireNonNull(validatedBy);
//    }
//  }

  //  public void validate(String username) {
//    setStatus(Status.VALIDATED);
//    setValidatedBy(username);
//  }
}

//region Code in the project might [not] follow the rule
class CodeFollowingTheRule {
  public void ok(Customer draftCustomer) {
//    draftCustomer.setValidatedBy("currentUser"); // from token/session..
//    draftCustomer.setStatus(Customer.Status.VALIDATED);
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