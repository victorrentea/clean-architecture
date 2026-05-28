package victor.training.clean.domain.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

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

//@Data // = NEVER!!!!!
@Getter
@Setter

@Entity // ORM (2) ok iff <=> friendly private DB
// 💙 Domain Model Entity - backbone of your core complexity
public class Customer {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String email;

  @Embedded
  private ShippingAddress shippingAddress;

  @Embeddable
  public record ShippingAddress(String city, String street, String zip) {}

//  public record Address(String city, String street, String zip) {}
  // high change you need it for billing address tomorrow = RISK☢️
  //   InvoiceAddress(String address, String vat)
  // shorter

  // Aim humble. Go specific first. Generalize JIT
  //It is good to think in advance, not to do in advance. = Guessing
  // The moment in which you are supposed to take design decisions (when we typically take design decisions) is the exact moment when we know the least about the problem we are solving at the beginning.
  // Architecture is the art of deferrign decisions.


  // Value Object = immutable small object w/o ID
  // We just have to burn in the type system a concept that was already lurking in the code.

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

  public boolean isNaturalPerson() { //ubiquitous language
    // A language that the entire team speaks in peace. Business, developers, testers, managers, UX, everyone is on the same language
    return getLegalEntityCode().isEmpty();
  }

  public boolean canReturnOrders() {
    return goldMember || isNaturalPerson();
  }

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }
  @Setter(NONE)
  private Status status = Status.DRAFT;
  @Setter(NONE)
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later

  public void validate(String validatedBy) {
    if (status != Status.DRAFT) {
      throw new IllegalStateException();
    }
    status = Status.VALIDATED;
    this.validatedBy = Objects.requireNonNull(validatedBy);
  }
  public void activate() {
    if (status != Status.VALIDATED) {
      throw new IllegalStateException();
    }
    status = Status.ACTIVE;
  }
  public void delete() {
    if (status == Status.DELETED) {
      throw new IllegalStateException();
    }
    status = Status.DELETED;
  }
}

class SomeCode {
  public void correct(Customer draftCustomer) {
    draftCustomer.validate("currentUser");
  }
  public void incorrect(Customer draftCustomer) {
//    draftCustomer.validate(null);
    draftCustomer.activate();
  }
  public void activate(Customer draftCustomer) {
    draftCustomer.activate();
  }
}
//endregion