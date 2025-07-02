package victor.training.clean.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.NONE;

//region Reasons to avoid @Data on Domain Model
// Avoid @Data on Domain Model because:
// 1) hashCode uses @Id⚠️
// 2) toString might trigger lazy-loading⚠️
// 3) all setters/getters = no encapsulation⚠️
//endregion

//@HasValidatedByForBlaBlaBla #BAN!!
@Data // = @Getter @Setter @ToString @EqualsAndHashCode (1)
@Entity // ORM/JPA (2)
// 👑 Domain Model Entity, the backbone of your core complexity.
public class Customer {
  @Id
  @GeneratedValue
  private Long id;
  @NotNull
  private String name;
  private String email;

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
  @Embedded // no ALTER TABLE is needed.
  private ShippingAddress shippingAddress;

//  public boolean f(SomeService) { // NOGO
//  public boolean f(ApiClient) { // NOGO
//  public boolean f(SomeRepo) { // NOGO
//  public boolean f(AnotherDomainModelOf70fields) { // NOGO
//  public boolean f() { // 10 ifs
  // all of these => stateless service

  public boolean canReturnOrders() { // biz rules
    return goldMember || isPerson();
  }

  public boolean isPerson() {
    return legalEntityCode == null;
  }

  // Value Object = small immutable object, w/o PK
  @Embeddable
  public record ShippingAddress(
      String city,
      String street,
      String zip
  ) {}

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
  private String validatedBy;

  @AssertTrue
  public boolean hasValidatedByForAnyStateAfterValidation() {
    return status != Status.DRAFT || validatedBy != null;
  }

  public void delete() {
    if (status == Status.DELETED) {
      throw new IllegalStateException();
    }
    status=Status.DELETED;
  }

  public void validate(String username) {
    requireNonNull(username, "username is mandatory");
    if (status != Status.DRAFT) {
      throw new IllegalStateException("Can validate a customer in DRAFT status");
    }
    this.status = Status.VALIDATED;
    this.validatedBy = username;
  }

  public void activate() {
    if (status != Status.VALIDATED) {
      throw new IllegalStateException("Can activate a customer in VALIDATED status only");
    }
    this.status = Status.ACTIVE;
  }
}

//region Code in the project might [not] follow the rule
class SomeCode {
  public void correct(Customer draftCustomer) {
    draftCustomer.validate("curr");
  }
  public void incorrect(Customer draftCustomer) {
    draftCustomer.validate("user");
  }
  public void activate(Customer draftCustomer) {
    draftCustomer.activate();
  }
}
//endregion