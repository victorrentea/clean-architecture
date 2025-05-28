package victor.training.clean.domain.model;

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

@Data // = @Getter @Setter @ToString @EqualsAndHashCode (1)
@Entity // ORM/JPA (2)
// 👑 Domain Model Entity, the backbone of your core complexity.
public class Customer {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String email;

  @Embedded // does not require any ALTER TABLE;
  private ShippingAddress shippingAddress;

  public boolean canReturnOrders() { // reuse (DRY) - very discoverable
    return goldMember || isPrivatePerson();
  }

  public boolean isPrivatePerson() { // explain
    return legalEntityCode == null;
  }

  // "Value Object" design pattern = small immutable type lacking PK
//  record Address(.., String vatCode) - risk=the broader the name the more chances to reuse & coupling
  @Embeddable
  public record ShippingAddress(String city, String street, String zip) {}

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
  private Status status = Status.DRAFT;
  @Setter(NONE)
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later

  //  public void setValid(Status newStatus) {
//  public void setValidStatus(Status newStatus) { // never go for the first name
  public void validate(String currentUser) { // OOP
    if (status != Status.DRAFT) {
      throw new IllegalStateException("Cannot validate a customer that is not in DRAFT status");
    }
    status = Status.VALIDATED;
    validatedBy = Objects.requireNonNull(currentUser);
  }

  public void activate() {
    if (status != Status.VALIDATED) {
      throw new IllegalStateException("Cannot activate a customer that is not VALIDATED");
    }
    status = Status.ACTIVE;
  }

  public void delete() {
    if (status == Status.DELETED) {
      throw new IllegalStateException("Cannot redelete");
    }
    status = Status.DELETED;
  }

  private void isTransitionAllowed() {

  }
}

class SomeCode {
  public void correct(Customer draftCustomer) {
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

// paranoid? question: what if despite my encapsulated business rules
// in Java someone updates directly the database bypassing the Java logic?
// DON'T TRIGGER/CHECK CONSTRAING
// instead: have the database PRIVATE TO YOUR DEV. Be full-stack owner of your DB