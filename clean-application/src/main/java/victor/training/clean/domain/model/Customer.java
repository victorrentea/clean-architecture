package victor.training.clean.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Optional;

import static lombok.AccessLevel.NONE;

//region Reasons to avoid @Data on Domain Model
// Avoid @Data on Domain Model because:
// 1) hashCode uses @Id⚠️
// 2) toString might trigger lazy-loading⚠️
// 3) all setters/getters = no encapsulation⚠️
//endregion

@Entity // ORM (2)
@Data // = @Getter + @Setter + @ToString + @EqualsAndHashCode (1)
// 💙 Domain Model Entity - backbone of your core complexity
public class Customer { // part of the Domain Model (wtf is that?>?!)
  // 🙁 not all projects have a Domain Model
  //
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String email;

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
//  private String shippingAddressCity;
//  private String shippingAddressStreet;
//  private String shippingAddressZip;

//  record Address(String street, String city, String zip, String vatCode) {}
  // ❤️ more generic, as it's future-ready @tiago

  // KISS: the moment you know the least is at the start.go humble, specific,
  // and move to more generic "Address" tomorrow after BillingAddress appears
  @Embedded
  private ShippingAddress shippingAddress;


  // Domain Logic methods 🧠🧠 inside my 👑Rich Domain Model (data structure)
  // ✅ if it uses my fields
  public boolean isPerson() {
    return getLegalEntityCode().isEmpty();
  }

  public boolean canReturnOrders() {
    return goldMember || isPerson();
  }

  // 🤔void m(int age) {}
  // 🤔void m(..) {MUTATE my fields}
  // ❌boolean isEuCountry(String iso) {10 lines}
  // ❌void m(Order35field param) {🧠🧠🧠🧠}
  // ❌void m(CustomerRepo toCallDB) {😱😱😱😱OMG}
  // ❌void m(IRSClient irsFriend) {external api calls}







  @Embeddable
      // "Value Object" design pattern for reading clarity> explicitate a Domain Concept in code
      // =
  public record ShippingAddress(String street, String city, String zip) {
  }

//  private Address invoicingAddress;

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

  // State transition methods with validation
  public void validate(String username) {
    if (status != Status.DRAFT) {
      throw new IllegalStateException("Can only validate a DRAFT customer");
    }
    if (username == null || username.isBlank()) {
      throw new IllegalArgumentException("validatedBy is required");
    }
    this.validatedBy = username;
    this.status = Status.VALIDATED;
  }

  public void activate() {
    if (status != Status.VALIDATED) {
      throw new IllegalStateException("Can only activate a VALIDATED customer");
    }
    this.status = Status.ACTIVE;
  }

  public void delete() {
    if (status == Status.DELETED) {
      throw new IllegalStateException("Customer already deleted");
    }
    this.status = Status.DELETED;
  }
}

//region Code in the project might [not] follow the rule
//class SomeCode {
//  public void correct(Customer draftCustomer) {
//    draftCustomer.validate("currentUser"); //✅ encapsulated transition
//  }
//  public void incorrect(Customer draftCustomer) {
//    // ❌ Can't do this anymore - setStatus is blocked by @Setter(NONE)
//    // draftCustomer.setStatus(Customer.Status.VALIDATED);
//  }
//  public void activate(Customer validatedCustomer) {
//    validatedCustomer.activate(); //✅ only works if already VALIDATED
//  }
//}
//endregion