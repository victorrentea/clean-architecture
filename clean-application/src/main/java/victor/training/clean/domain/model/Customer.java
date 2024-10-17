package victor.training.clean.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.mockito.internal.stubbing.answers.Returns;

import java.time.LocalDate;
import java.util.Objects;

import static lombok.AccessLevel.NONE;

//region Reasons to avoid @Data on Domain Model
// Avoid @Data on Domain Model because:
// 1) hashCode uses @Id⚠️
// 2) toString might trigger lazy-loading⚠️
// 3) all setters/getters = no encapsulation⚠️
//endregion

record Money(int amount, Currency currency) {
}
enum Currency {
  EUR, USD
}

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
  @Embedded
  private ShippingAddress shippingAddress; // -2 fields, more semantic

  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

  public boolean canReturnOrders() { // more reusable
    return goldMember || isIndividual();
  }

  private boolean isIndividual() { // 🧠-friendlier names
    return legalEntityCode == null;
  }

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }
  @Setter(NONE)
  private Status status = Status.DRAFT;
  @Setter(NONE)
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later

  public void validate(String currentUser) {
    if (status != Status.DRAFT) {
      throw new IllegalStateException("Can't validate a non-draft Customer");
    }
    status = Status.VALIDATED;
    validatedBy = Objects.requireNonNull(currentUser);
  }
  public void activate() {
    if (status != Status.VALIDATED) {
      throw new IllegalStateException("Can't activate a non-validated Customer");
    }
    status = Status.ACTIVE;
  }
  public void delete() {
    if (status == Status.DELETED) {
      throw new IllegalStateException("Can't delete a deleted Customer");
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
//    draftCustomer.setStatus(Customer.Status.VALIDATED);
    draftCustomer.validate("null");

  }
  public void activate(Customer draftCustomer) {
//    draftCustomer.setStatus(Customer.Status.ACTIVE);
    draftCustomer.activate();
  }
}
//endregion