package victor.training.clean.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Configurable;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

//region Reasons to avoid @Data on Domain Model
// Avoid @Data on Domain Model because:
// 1) hashCode uses @Id⚠️
// 2) toString might trigger lazy-loading⚠️
// 3) all setters/getters = no encapsulation⚠️
//endregion
//@Configurable// ☠️ @Autowired in Domain Model; also lockin to a DARK feat of 🔒spring too much
@Data // = @Getter + @Setter + @ToString + @EqualsAndHashCode (1)
@Entity // ORM (2)
// 💙 Domain Model Entity - backbone of your core complexity
public class Customer {
  @Id
  @GeneratedValue
  private Long id;
  @Size(min = 4)
  private String name;
  private String email;

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
//  private String shippingAddressCity;
//  private String shippingAddressStreet;
//  private String shippingAddressZip;
  @Embedded
  private ShippingAddress shippingAddress;
  // shared data structure: 😊more reuse; 🙁coupling
  // DRY leads to coupling
  @ManyToOne
  private Country country;

  public boolean canReturnOrders() { // reusable small business logic
    return goldMember || isNaturalPerson();
  }

  /**
   * A natural person, not a company. -- out of sync as people don't update them
   */
  // isNotCompany()
  // hasNoLegalEntityCode() - ask business WHY?!
  // ⚠️ - might force me (the tech expert) to TEACH the business what the
  //  didn't know (embarrasing) ~> don't you want to become a business expert/PO?
  // ⚠️ - it does require developers to work on their comm skills: "play dumb" first.
  // play more event storming/glossary of terms/.feature file biz-signed-off
  private boolean isNaturalPerson() { // explain a concept;
    return getLegalEntityCode().isEmpty(); // ❤️✅
  }
//  public void bad(isChina:Bool=true) {🤔SRP violation = !clean code
//  public void bad(timeout:Int=1000) {🤔mysterious❌✅

// bad in a Domain Model data structure
//  public void bad(CustomerDto) {❌

//  public void bad(CustomerRepo) {❌ allows SQL from inside
//  public void bad(LdapApiClient) {❌ allows network from inside

//  public void bad(Contract{entity with 20 fields}) {❌ too many reasons to change, too much coupling





  // what if I will have another address type in future? eg. billingAddress
//  @Embedded
//  private Address billingAddress; // WRONG❌ in RO it must have + VAT CODE
  // ⚠️ speculative early abstraction failed
  // 🤔 should I go from:
  // A> generic (Address) -> specific
  // B> specific (ShippingAddress) -> generic = extract common Address later

  // the moment you know least about a problem is in the beginning
  // Uncle Bob: "Architecture is the art of deffering decisions"

  // XP: the rule of 3 = 3 similar things -> extract common abstraction
  // = 'don't be clever too early'

  // Value Object design pattern = small immutable object lacking PK
  @Embeddable
  /*record Address = A*/
  public record ShippingAddress(String street, String city, String zip/*, String vatCode*/) {} //B

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

  public Optional<String> getLegalEntityCode() {
    return Optional.ofNullable(legalEntityCode); //
  }


  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }

  @Setter(AccessLevel.NONE)
  private Status status;
  @Setter(AccessLevel.NONE)
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later
  // how to enforce this?
  // 1) in a @Service (easy to forget)
  // 2) constructor of this Aggregate (DDD) + factory method << ARE YOU USING IT? = Extreme OOP in BE
  // in DDD: Aggregate = consistency boundary of related entities = transaction boundary
  // example: Order{total, List<OrderLineItem>}, root=Order, OrderLineItem cannot exist without Order
  // Aggregate root ensures consistency of the whole graph of objects inside the Aggregate
  // rules of aggregate design
  // a) only root has global identity (PK)
  // b) only root is referenced from outside
  // c) root enforces all invariants/rules for the whole aggregate
  // 3) @nnotations w/o custom validaators but with
//  @AssertTrue // auto-cjecled at repo.save
//  private boolean isValidatedBySetWhenStatusIsValidatedOrLater() {
//    if (status == Status.VALIDATED || status == Status.ACTIVE || status == Status.DELETED) {
//      return validatedBy != null;
//    }
//    return true;
//  }
  //

  // Guarded state mutations to enforce business invariants (rules)
  public void validate(String currentUsername) {
    if (this.status != Status.DRAFT) {
      throw new IllegalStateException("Only DRAFT customers can be validated");
    }
    this.status = Status.VALIDATED;
    this.validatedBy = requireNonNull(currentUsername);
  }

  public void activate() {
    if (this.status != Status.VALIDATED) {
      throw new IllegalStateException("Only VALIDATED customers can be activated");
    }
    this.status = Status.ACTIVE;
  }

  public void delete() {
    if (this.status == Status.DELETED) {
      throw new IllegalStateException("Customer is already DELETED");
    }
    this.status = Status.DELETED;
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
    // forgot to set validatedBy // PREVENT THIS!
    draftCustomer.validate("null");
  }

  public void activate(Customer draftCustomer) {
//    draftCustomer.setStatus(Customer.Status.ACTIVE);
    draftCustomer.activate();
  }
}
//endregion