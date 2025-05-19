package victor.training.clean.domain.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import static lombok.AccessLevel.NONE;

//region Reasons to avoid @Data on Domain Model
// Avoid @Data on Domain Model because:
// 1) hashCode uses @Id⚠️
// 2) toString might trigger lazy-loading⚠️
// 3) all setters/getters = no encapsulation⚠️
//endregion


//@MyCustomKungFuValidator// NEVER EVER DO THIS!!! if apllied in 1..2 places.
@Data // = @Getter @Setter @ToString @EqualsAndHashCode (1)
@Entity // ORM/JPA (2)
// 👑 Domain Model Entity, the backbone of your core complexity.
public class Customer {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String email;

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
  @Embedded
  private ShippingAddress shippingAddress;

  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

  public Optional<String> getLegalEntityCode() {
    return Optional.ofNullable(legalEntityCode);
  }
  @Setter(NONE)
  private Status status = Status.DRAFT;
  @Setter(NONE)
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later

//  @Embedded Action creation;
//  record ValidationAction() {}
//  @Embedded ValidationAction validation;
  //getStatus determines the last != null

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }

  public boolean canReturnOrders() {
    return goldMember || isNaturalPerson();
  }

  private boolean isNaturalPerson() {
    return legalEntityCode == null;
  }

  //  @AssertTrue(message = "validatedBy must be set when status is VALIDATED or ACTIVE or DELETED")
//  private boolean isValidatedByPresentWhenRequired() {
//    return switch (status) {
//      case VALIDATED, ACTIVE, DELETED -> validatedBy != null && !validatedBy.isBlank();
//      default -> true;
//    };
//  }
  public void markValidated(@Nonnull String user) {
    if (status != Status.DRAFT) {
      throw new IllegalStateException("Cannot validate a customer that is not in DRAFT status");
    }
    status = Status.VALIDATED;
    validatedBy = Objects.requireNonNull(user, "system");
//    validatedBy = Objects.requireNonNullElse(user, "system");
  }

  public void markActivated() {
    if (status != Status.VALIDATED) {
      throw new IllegalStateException("Cannot activate a customer that is not in VALIDATED status");
    }
    status = Status.ACTIVE;
  }

  public void markDeleted() {
    if (status == Status.DELETED) {
      throw new IllegalStateException("Cannot delete a customer that is already DELETED");
    }
    status = Status.DELETED;
  }

  // sealed State permits Draft, Validated(validatedBy), Active(validatedBy) {
  //    validate(user)
//  State state;

}

//region Code in the project might [not] follow the rule
class SomeCode {
  public void correct(Customer draftCustomer) {
    draftCustomer.markValidated("currentUser"); // from token/session..
//    draftCustomer.validate("again"); // from token/session..
  }

  public void incorrect(Customer draftCustomer) {
    draftCustomer.markValidated(null);
  }

  public void activate(Customer draftCustomer) {
//    draftCustomer.setStatus(Customer.Status.ACTIVE);
    draftCustomer.markActivated();
  }
}
//endregion