package victor.training.clean.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
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

// This class is part of your Domain Model, the backbone of your core complexity.
//@Data // = @Getter @Setter @ToString @EqualsAndHashCode (1)
@Getter
@Setter
@Entity // ORM/JPA (2)
public class Customer {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String email;

  public boolean canReturnOrders() {
    return goldMember || isPhysicalPerson();
  }

  private boolean isPhysicalPerson() {
    return legalEntityCode == null;
  }

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
//  private String shippingAddressCity;
//  private String shippingAddressStreet;
//  private String shippingAddressZip;

  // Value Object  !DESIGN PATTERN = IMMUTABLE! and lacks identity
  // Canonical example: Money{amount, currency}
  @Embeddable
  public record ShippingAddress(String city, String street, String zip) {}


  @Embedded // no ALTER table is required
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

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }
  @Setter(NONE)
  private Status status=Status.DRAFT;
  @Setter(NONE)
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later

  public void validate(String user) {
    // A: ignore can be unexpected from caller side, hard to debug
    if (status != Status.DRAFT) return;
    // B: failing; let the UI be kind to user. in BE, be strict!
    if (status != Status.DRAFT) throw new IllegalStateException("Can't validate a non-DRAFT Customer");

    status = Status.VALIDATED;
    validatedBy = Objects.requireNonNull(user);
  }
  public void activate() {
    if (status != Status.VALIDATED) {
      throw new IllegalStateException("Can't activate a non-VALIDATED Customer");
    }
    status = Status.ACTIVE;
  }
  public void delete() {
    if (status != Status.ACTIVE) {
      throw new IllegalStateException("Can't delete a non-ACTIVE Customer");
    }
    status = Status.DELETED;
  }

}

//region Code in the project might [not] follow the rule
class SomeCode {
  public void correct(Customer draftCustomer) {
//    draftCustomer.setStatus(Customer.Status.VALIDATED);
//    draftCustomer.setValidatedBy("currentUser"); // from token/session..
    draftCustomer.validate("currentUser");
  }
  public void incorrect(Customer draftCustomer) {
    draftCustomer.validate("null");
  }
  public void activate(Customer draftCustomer) {
    draftCustomer.activate();
  }
}
//endregion