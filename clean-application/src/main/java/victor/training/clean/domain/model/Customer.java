package victor.training.clean.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

//region Reasons to avoid @Data on Domain Model
// Avoid @Data on Domain Model because:
// 1) hashCode uses @Id⚠️
// 2) toString might trigger lazy-loading⚠️
// 3) all setters/getters = no encapsulation⚠️
//endregion

@Data // = @Getter✅
// @Setter🤨 - pune selectiv pe campurile editabile liber
// @ToString❌ - periculos sa triggereze lazy load + waste constrund un string huge + GDPR
// @EqualsAndHashCode❌ - include si @Id generat de hib -> acelasi @entity sa-si schibme hashCode pe parcursul vietii
@Entity // ORM/JPA (2)


// 👑 Domain Model Entity = the backbone of your core complexity.
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
  private ShippingAddress shippingAddress;

  private String vatCode;

  //  record Address( // best because we can reuse the object in the future
  // eg. for billing address
  // Value Object (design pattern) = immutable [small] without PK (persistent id)
  //  eg: Money(amm,cur)
  //  eg: GPS(lat,lon)
  public record ShippingAddress( //enrich the language of your domain
                                 String city,
                                 String street,
                                 String zip) {}

//  private Address billingAddress; // tomorrow, perhaps, maybe. wait for tih to come.

  // don't design for reuse tomorrow, - overengineering
  // extract for reuse today - opportunistic refactoring!
  //    on-demand = brave
  @ManyToOne
  private Country country;

  private LocalDate createdDate;

  private String createdByUsername;
  private boolean goldMember;

  private String goldMemberRemovalReason;

  @Setter(AccessLevel.NONE)
  private Status status = Status.DRAFT;

  // what can I add <HERE> to make you move this method into a @Service
  // - Order{30 fields}; String/int/ShippingAddress is fine
  // - Consumer<X> - FP maniac
  // - CustomerRepo / anotherService (managed by DI)
  // public boolean canReturnOrders(<HERE>) {
  @Setter(AccessLevel.NONE)
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later
  private boolean discountedVat;

  public Optional<String> getVatCode() {
    return Optional.ofNullable(vatCode);
  }

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }

  public boolean isNaturalPerson() { // ok = explaining the meaning
    return vatCode == null;
  }

  public boolean canReturnOrders() { // ? = is this the respo of the customer; how big can this grow
    // better than in a customer Util/Hellper
    return goldMember || isNaturalPerson();
  }

  //  void setValidated(String username) { // Java specific getter setter mania
  public void validate(String validatorUsername) {
    if (status != Status.DRAFT) {
      throw new IllegalStateException();
    }
    if (validatorUsername == null || validatorUsername.isBlank()) { // who let the "" reaech this point!?
      throw new IllegalArgumentException();
    }
    status = Status.VALIDATED;
    validatedBy = validatorUsername;
  }

  public void activate() {
    if (status != Status.VALIDATED) {
      throw new IllegalStateException("Cannot activate a customer that is not validated");
    }
    status = Status.ACTIVE;
  }

  public void delete() {
    if (status == Status.DELETED) {
      throw new IllegalStateException("Cannot delete a customer that is already deleted");
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