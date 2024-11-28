package victor.training.clean.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import static lombok.AccessLevel.NONE;
import static victor.training.clean.domain.model.Customer.Status.ACTIVE;
import static victor.training.clean.domain.model.Customer.Status.VALIDATED;

// in computer science, there are only two problems:
// 1) Naming things
// 2) Cache invalidation
// the rule of 3: extr stuff when it repeats 3 times

// Favor composition over inheritance. ie. dont use 'extends'🤨

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

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
//  private String shippingAddressCity;
//  private String shippingAddressStreet;
//  private String shippingAddressZip;

  @Embedded // ORM did not change the DB schema; there is no alter table needed
  // LIKE: - 2 fields
  // LIKE: more semantics
  private ShippingAddress shippingAddress;

  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

  public boolean canReturnOrders() { // business rules
    return goldMember || isPhysicalPerson();
  }

  public boolean isPhysicalPerson() { // explaining better the data
    return legalEntityCode == null;
  }

  public Optional<String> getLegalEntityCode() {
    return Optional.ofNullable(legalEntityCode);
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
      throw new IllegalStateException("Can't validate a non-draft customer");
    }
    status = VALIDATED;
    validatedBy = Objects.requireNonNull(currentUser);
  }
  public void activate() {
    if (status != VALIDATED) {
      throw new IllegalStateException("Can't activate a non-validated customer");
    }
    status = ACTIVE;
  }
  public void delete() {
    if (status == Status.DELETED) {
      throw new IllegalStateException("Can't delete a deleted customer");
    }
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
    draftCustomer.activate();
  }
}
//endregion