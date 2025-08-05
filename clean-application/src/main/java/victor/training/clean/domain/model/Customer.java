package victor.training.clean.domain.model;

import jakarta.validation.constraints.AssertTrue;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import victor.training.clean.domain.model.Country;

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
//@ValidValidatedBy
public class Customer {
  @Id
  @GeneratedValue
  private Long id;
  @NotNull
  @Size(max = 80)
  private String name;
  private String email;

  @Embedded
  private ShippingAddress shippingAddress;

//  private Address billingAddress; {String vatCode/cnp, String address}
//  public record Address( // + generic, tomorrow I could reuse it = premature abstraction.
  // use "the rule of 3" = if you need to use the same type in 3 places, then extract it. see http://www.martinfowler.com/bliki/RuleOfThree.html

  // VO = Value Object = small immutable obj lacking PK
  // = explicitated a concept floating through my code
  @Embeddable
  public record ShippingAddress( // + more precise, more "humble". keep specific, generify ON DEMAND
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

  public boolean canReturnOrders() { // bits of business rules
    return goldMember || isIndividual();
  }

  public boolean isIndividual() { // explain data
    return legalEntityCode == null;
  }

//  public void dontDoThis(OrderLarge10FieldsObj) {
//  public void dontDoThis(CustomerRepo) { DB access
//  public void dontDoThis(ExtApiClient) { REST calls
//  public void dontDoThis(ApiDto external) {

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

  // TODO impl the rule above here
//  public void transition(Status toState, String validatedBy) {
//  public void validate(String validatedBy) { // command
  public void setValidated(String validatedBy) { // event
    // guarding data mutation using methods rejecting invalid transitions
    if (status != Status.DRAFT) {
      throw new IllegalStateException("Cannot validate a customer that is not in DRAFT state");
    }
    this.status = Status.VALIDATED;
    this.validatedBy = Objects.requireNonNull(validatedBy);
  }

  public void setActive() {
    if (status != Status.VALIDATED) {
      throw new IllegalStateException("Cannot activate a customer that is not in VALIDATED state");
    }
    this.status = Status.ACTIVE;
  }
  // Some validation CAN be embedded in the object itself.
  // But: keep in @Service other more complex rules involving other objects (other entities, repos..)

  public void setDeleted() {
    if (status == Status.DELETED) {
      throw new IllegalStateException("Cannot perform this operation on a deleted customer");
    }
    status = Status.DELETED;
  }

  // method called at validation having to return true
  @AssertTrue(message = "validatedBy must be non-null when status is VALIDATED or later")
  private boolean isValidatedByPresentIfStatusValidatedOrLater() {
    return switch (status) {
      case DRAFT -> true;
      case VALIDATED, ACTIVE, DELETED -> validatedBy != null;
    };
  }
}


//region Code in the project might [not] follow the rule
class SomeCode {
  public void correct(Customer draftCustomer) {
    draftCustomer.setValidated("currentUser"); // from token/session..
  }

  public void incorrect(Customer draftCustomer) {
    draftCustomer.setValidated("someUser");
  }

  public void activate(Customer draftCustomer) {
    draftCustomer.setActive();
  }
}
//endregion