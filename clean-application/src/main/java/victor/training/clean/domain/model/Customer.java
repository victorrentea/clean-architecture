package victor.training.clean.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import victor.training.clean.domain.model.Customer.Status;

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
  @Embedded // tabela ramane identica, fara ALTER TABLE
  private ShippingAddress shippingAddress;

  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

  public boolean canReturnOrders() {
    return goldMember || isNaturalPerson();
  }

  private boolean isNaturalPerson() {
    return legalEntityCode == null;
  }

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }
  @Setter(NONE)
  private Status status;
  private String validatedBy; // ⚠🤞 (JDD) Always not-null when status = VALIDATED or later

  public void validate(String currentUser) {
    if (status != Status.DRAFT) {
      throw new IllegalStateException("Can only validate DRAFT customers");
    }
    status = Status.VALIDATED;
    validatedBy = Objects.requireNonNull(currentUser);
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
  }
}

class CodeBreakingTheRule {
  public void farAway(Customer draftCustomer) {
    draftCustomer.validate("currentUser");
//    draftCustomer.setStatus(Customer.Status.VALIDATED, "<NULL>");
//    draftCustomer.setStatus(Status.ACTIVE, "?DECE??!!");
  }
}
//endregion