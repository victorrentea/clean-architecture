package victor.training.clean.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;

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
  @Embedded // fighting primitive obsession code smell
  private ShippingAddress shippingAddress; // -2 fields => dev=:)

  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

  // reusable bits of logic
  public boolean canReturnOrders() {
    return isGoldMember() || isIndividual();
  }

  // explanatory methods
  private boolean isIndividual() {
    return getLegalEntityCode() == null;
  }

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }
  @Setter(NONE)
  private Status status=Status.DRAFT;
  @Setter(NONE)
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later
//  public Customer setStatus(Status status, String username) {
//    this.status = status;
//    if (status == Status.VALIDATED) {
//      this.validatedBy = username;
//    }
//    return this;
//  }
  // guards changes
  public void validate(String username) {
    if (status != Status.DRAFT) {
      throw new IllegalStateException("Can only validate DRAFT customers");
    }
    this.status = Status.VALIDATED;
    this.validatedBy = Objects.requireNonNull(username);
  }
  public void activate() {
    if (status != Status.VALIDATED) {
      throw new IllegalStateException("Can only activate VALIDATED customers");
    }
    this.status = Status.ACTIVE;
  }
  public void delete() {
    if (status == Status.DELETED) {
      throw new IllegalStateException("Already deleted");
    }
    this.status = Status.DELETED;
  }
}

//region Code in the project might [not] follow the rule
class SomeCode {
  public void correct(Customer draftCustomer) {
    draftCustomer.validate("currentUser");
  }
  public void incorrect(Customer draftCustomer) {
    draftCustomer.validate("null"); // ☠️
  }
  public void activate(Customer draftCustomer) {
    draftCustomer.activate();
  }
}
//endregion