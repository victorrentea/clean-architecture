package victor.training.clean.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.*;
import static victor.training.clean.domain.model.Customer.Status.DRAFT;
import static victor.training.clean.domain.model.Customer.Status.VALIDATED;

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

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
//  private String shippingAddressCity;
//  private String shippingAddressStreet;
//  private String shippingAddressZip;

  @Embedded //did not change the DB schema
  private ShippingAddress shippingAddress;

  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

  public boolean canReturnOrders() { // little biz rules reused
    return goldMember || isPhysicalPerson();
  }

  private boolean isPhysicalPerson() { // explain data meaning
    return legalEntityCode == null;
  }

  public Optional<String> getLegalEntityCode() {
    return Optional.ofNullable(legalEntityCode);
  }

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }
  @Setter(NONE)
  private Status status = DRAFT;
  @Setter(NONE)
  private String validatedBy; // Domain Invariant: ⚠ Always not-null when status = VALIDATED or later

 public void validate(String validatedBy) {
    if (status != DRAFT) {
      throw new IllegalStateException("Can only validate DRAFT customers");
    }
    status = VALIDATED;
   this.validatedBy = requireNonNull(validatedBy);
  }

  public void activate() {
    if (status != VALIDATED) {
      throw new IllegalStateException("Can only activate VALIDATED customers");
    }
    status = Status.ACTIVE;
  }

  public void delete() {
//    if (status != Status.ACTIVE) {
//      throw new IllegalStateException("Can only delete ACTIVE customers");
//    }
    status = Status.DELETED;
  }
}

//region Code in the project might [not] follow the rule
class SomeCode {
  public void correct(Customer draftCustomer) {
    draftCustomer.validate("currentUser"); // from token/session..
  }
  public void incorrect(Customer draftCustomer) {
    draftCustomer.validate("null");
  }
  public void activate(Customer draftCustomer) {
//    draftCustomer.validate(Customer.Status.ACTIVE, "WTH?Y!!! incorrect to overwrite ");
    draftCustomer.activate();
  }
}
//endregion