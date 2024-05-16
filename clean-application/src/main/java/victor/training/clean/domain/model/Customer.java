package victor.training.clean.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import static java.util.Optional.ofNullable;
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
  @Embedded // did not change the DB table schema
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
    return ofNullable(legalEntityCode);
  }

  // maybe even refactor 2 subclasses: PhysicalPersonCustomer, LegalEntityCustomer
  // - serious different logic (> 7 x if(isPhysicalPerson) {..} else {..})
  // - different >3 fields
  // - for stateless behavior, also extends for Template Method design pattern
  //   !!! prefer composition over inheritance: inject the class to use, don't extend it

  // Domain Behavior INSIDE the domain model
  public boolean canReturnOrders() {
    return goldMember || isPhysicalPerson();
  }
  // What logic should NOT get inside?
  // or, what paramters should NOT be passed to this method?
  // m(ObjWhoseFieldsAreChanged)
  // m(LargeDataStructure)
  // m(ShippingAddress) > should be immutable and small
  // m(CustomerRepo) => statless @Service
  // m(LdapApiAdapter) => statless @Service

  private boolean isPhysicalPerson() {
    return legalEntityCode == null;
  }

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }
  @Setter(NONE) // Hibernate doesn't need setters/getters, it uses reflection to access fields
  private Status status = Status.DRAFT;
  @Setter(NONE)
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later🤞

  // guard domain invariants with behavior, killing setters (DDD)
  // Encapsulation
  public void validate(String validatedBy) {
    if (status != Status.DRAFT) {
      throw new IllegalStateException("Can't validate a Customer that is not in DRAFT status");
    }
    this.status = Status.VALIDATED;
    this.validatedBy = Objects.requireNonNull(validatedBy);
  }
  public void activate() {
    if (status != Status.VALIDATED) {
      throw new IllegalStateException("Can't activate a Customer that is not in VALIDATED status");
    }
    this.status = Status.ACTIVE;
  }
  public void delete() {
    if (status != Status.ACTIVE) {
      throw new IllegalStateException("Can't delete a Customer that is not in ACTIVE status");
    }
    this.status = Status.DELETED;
  }
}

//region Code in the project might [not] follow the rule
class CodeFollowingTheRule {
  public void ok(Customer draftCustomer) {
//    draftCustomer.setStatus(Customer.Status.VALIDATED);
//    draftCustomer.setValidatedBy("currentUser"); // from token/session..
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