package victor.training.clean.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
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
public class Customer { // Domain Model entity representing
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String email;


//  public record Address ( // too generic, too early
  // start "humble"=specific and generify stuff when strictly needed
//  private ShippingAddress billingAddress; // wrong

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
  @Embedded
  private ShippingAddress shippingAddress;

  // easy to discover
  // rich domain model
  public boolean canReturnOrders(/*XXXhere*/) { // I can put business logic inside the domain model
    // better than a CustomerUtil/Hellper
    // cannot add the method INSIDE if: part of shared .jar / generated code (eg from a swagger)
    // => that's why I want to create myself the central datastructure I use in most of my complexity
    return goldMember || legalEntityCode == null;
  }


  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;

  // "Value Object" design pattern = small, immutable lacking PK (identity)
  // eg: Money(amount, currency) =
  // they make a domain concept explicit in the type system
  @Embeddable// jpa
  public record ShippingAddress(
      String city,
      String street,
      String zip
  ) {}
  // DON'T PUSH LOGIC IN if it uses stuff unrelated to Customer:
  // add tiny bits of reusable business logic.
  // don't add /*XXXhere*/
  // - NOT an Order{30 fields}
  // - NOT CustomerRepo{hit the DB}
  // - NOT GPayClient{calling REST}
  // - NOT stuff highly specific to a single use case: asXmlForAmazonPayouts();
  // don't bring such things in the Domain Model

  private boolean discountedVat;

  public Optional<String> getLegalEntityCode() {
    return Optional.ofNullable(legalEntityCode);
  }


  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }

  @Setter(NONE)
  private Status status = Status.DRAFT;
  private String validatedBy;

  public void validate(String user) {
    if (status != Status.DRAFT) {
      throw new IllegalStateException();
    }
    validatedBy = Objects.requireNonNull(user);
    status = Status.VALIDATED;
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