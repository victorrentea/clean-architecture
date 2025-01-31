package victor.training.clean.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

//region Reasons to avoid @Data on Domain Model
// Avoid @Data on Domain Model because:
// 1) hashCode uses @Id⚠️
// 2) toString might trigger lazy-loading⚠️
// 3) all setters/getters = no encapsulation⚠️
//endregion

@Data // = @Getter @Setter @ToString @EqualsAndHashCode (1)
@Entity // ORM/JPA (2)
//@Configurable // don't use it @Value("${)
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
  @Embedded // there is no need to ALTER TABLE> the 3 columns are still in the same table
  private ShippingAddress shippingAddress;

  // i had to call buisness => game won!

//  private String invoiceAddressCity;
//  private String invoiceAddressStreet;
//  private String invoiceAddressZip;
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

  // do this unless:
  // - logic inside is compplex -> a separate class (SRP)
  // - customer is created after "jan 2023" -> out as it depends on a @Value("${

  // reuse of a business rule involving just the state of te customer = OOP
  public boolean canReturnOrders() { // method (synthetic getter), not field!
    return goldMember || isPhysicalPerson();
  }

  // explain the data, clarity
  private boolean isPhysicalPerson() {
    return legalEntityCode == null;
  }

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }

  @Setter(AccessLevel.NONE)
  private Status status = Status.DRAFT;
  @Setter(AccessLevel.NONE)
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later
  // to much magic
//  @PreUpdate
//  @PrePersist
//  private void validate() {
//    if (status == Status.VALIDATED && validatedBy == null) {
//      throw new IllegalStateException("validatedBy is mandatory when status is VALIDATED");
//    }
//  }

//  public void setStatus(Status status) { // produces temporal coupling
//    if (status == Status.VALIDATED && validatedBy == null) {
//      throw new IllegalStateException("validatedBy is mandatory when status is VALIDATED");
//    }
//    this.status = status;
//  }

//  public void updateStatus(Status newStatus, String currentUser) { // useless 2nd param sometimes!..
//    if (newStatus == Status.VALIDATED && currentUser == null) {
//      throw new IllegalStateException("currentUser is mandatory when status is VALIDATED");
//    }
//    this.status = newStatus;
//    this.validatedBy = Objects.requireNonNull(currentUser);
//  }

  // guarded mutations
  public void validate(String currentUser) {
    if (status != Status.DRAFT) {
      throw new IllegalStateException("Can't validate a non-draft customer");
    }
    status = Status.VALIDATED;
    validatedBy = Objects.requireNonNull(currentUser);
  }

  public void activate() {
    if (status != Status.VALIDATED) {
      throw new IllegalStateException("Can't activate a non-validated customer");
    }
    status = Status.ACTIVE;
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
//    draftCustomer.updateStatus(Customer.Status.VALIDATED, "currentUser");
//    draftCustomer.setStatus(Customer.Status.VALIDATED);
//    draftCustomer.setValidatedBy("currentUser"); // from token/session..
    draftCustomer.validate("currentUser");
  }
  public void incorrect(Customer draftCustomer) {
//    draftCustomer.setStatus(Customer.Status.VALIDATED);
    draftCustomer.validate("currentUser");

  }
  public void activate(Customer draftCustomer) {
//    draftCustomer.setStatus(Customer.Status.ACTIVE);
//    draftCustomer.updateStatus(Customer.Status.ACTIVE, nul????);
    draftCustomer.activate();
  }
}
//endregion