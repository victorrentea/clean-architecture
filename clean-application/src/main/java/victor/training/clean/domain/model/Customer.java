package victor.training.clean.domain.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Optional;

//region Reasons to avoid @Data on Domain Model
// Avoid @Data on Domain Model because:
// 1) hashCode uses @Id⚠️
// 2) toString might trigger lazy-loading⚠️
// 3) all setters/getters = no encapsulation⚠️
//endregion

@Data // = @Getter + @Setter + @ToString + @EqualsAndHashCode (1)
@Entity // ORM (2)
// 💙 Domain Model Entity - backbone of your core complexity
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

  @Valid
  @Embedded //no  ALTER TABLE needed
  private ShippingAddress shippingAddress;

  @Setter(AccessLevel.NONE)
  private Status status = Status.DRAFT;
  @Setter(AccessLevel.NONE)
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later

  //
//  record Address(...) { 😊reusABLE, 🙁overgeneric?
  @Embeddable
  public record ShippingAddress(
      // declarative
      @NotBlank @Size(min = 3) String city,
      @NotBlank String street,
      String zip) {

    // programatic self-validation
    public ShippingAddress {
      // same validations as @
      //NotBlank and @Size(min=3) but in code, throwing IllegalArgumentException if not valid
      if (city == null || city.isBlank() || city.length() < 3) {
        throw new IllegalArgumentException("City must be at least 3 characters long");
      }
      if (street == null || street.isBlank()) {
        throw new IllegalArgumentException("Street must not be blank");
      }
    }

    // 😊specific
  }
//  record BillingAddress(String address, vatCode/cnp) {
  // Premature Abstraction = The more generic you go up front,
  // the more chances you take of the
  // abstraction being wrong in the moment when you know the least about
  // your problem.
  // Design Humbly, specific

  // extends is 😈Evil: eg: how can child constrain the parent field/behavior?

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

  public boolean canReturnOrders() { // instead of CustomerUtil🚽, CustomerHellper😈
    return goldMember || isNaturalPerson();
  }

  private boolean isNaturalPerson() { // ubiquitous language with biz.
    return getLegalEntityCode().isEmpty();
  }

  public void validate(String by) {
    if (status != Status.DRAFT) throw new IllegalStateException("Can only validate a DRAFT customer");
    if (by == null || by.isBlank()) throw new IllegalArgumentException("Validator username is required");
    this.status = Status.VALIDATED;
    this.validatedBy = by;
  }

  public void activate() {
    if (status != Status.VALIDATED) throw new IllegalStateException("Can only activate a VALIDATED customer");
    this.status = Status.ACTIVE;
  }

  public void delete() {
    if (status == Status.DELETED) throw new IllegalStateException("Customer is already DELETED");
    this.status = Status.DELETED;
  }
}

//region Code in the project might [not] follow the rule
class SomeCode {
  public void correct(Customer draftCustomer) {
    draftCustomer.validate("currentUser");
  }

  public void activate(Customer draftCustomer) {
    draftCustomer.activate();
  }

  public void delete(Customer draftCustomer) {
    draftCustomer.delete();
  }
}
//endregion