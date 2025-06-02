package victor.training.clean.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.AccessLevel;
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

@Data // = @Getter @Setter @ToString @EqualsAndHashCode (1)
@Entity // ORM/JPA (2)
// 👑 Domain Model Entity, the backbone of your core complexity.
public class Customer {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String email;

  @Setter(NONE)
  private Status status = Status.DRAFT;
  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
//  private String shippingAddressCity;
//  private String shippingAddressStreet;
//  private String shippingAddressZip;
  @Embedded
  private ShippingAddress shippingAddress;
  @Setter(NONE)
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later

  //bits of logic based on my fields.
  public boolean canReturnOrders() {
    return goldMember || isPhysicalPerson();
  }


  // shouldn't I name it as "Address" to be more reuseable? (DRY) as a BillingAddress?
  // billing address might have other required fields, or extra fields (e.g. VAT number)

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

  public boolean isPhysicalPerson() {
    return legalEntityCode == null;
  }

  // guarding state mutations with methods that enforce invariants
  public void validate(String username) {
    if (status != Status.DRAFT) {
      throw new IllegalStateException("Cannot validate a customer that is not in DRAFT status");
    }
    validatedBy = Objects.requireNonNull(username);
    status = Status.VALIDATED;
  }

  public void activate() {
    if (status != Status.VALIDATED) {
      throw new IllegalStateException("Cannot activate a customer that is not in VALIDATED status");
    }
    status = Status.ACTIVE;
  }

//  @AssertTrue // opt1 validate at peristence time < most team prefer this
//  public boolean hasValidatedBy() {
//    if (status == Status.DRAFT) return true;
//    return validatedBy != null;
//  }

  public void delete() {
    if (status == Status.DELETED) {
      throw new IllegalStateException("Cannot delete a customer that is already DELETED");
    }
    status = Status.DELETED;
  }

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED;
  }

  @Embeddable
  // 👑 Value Object, a small object that represents a value in the domain.
  // It is immutable and has no identity.
  public record ShippingAddress(String city, String street, String zip) {}


  // opt2: O-O-P: Customer's methods prevent object from being in an invalid state

}

//region Code in the project might [not] follow the rule
class SomeCode {
  public void correct(Customer draftCustomer) {
    draftCustomer.validate("currentUser"); // from token/session..
  }

  public void incorrect(Customer draftCustomer) {
    draftCustomer.validate("user");
  }

  public void activate(Customer draftCustomer) {
    draftCustomer.activate();
  }
}
//endregion