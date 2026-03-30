package victor.training.clean.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
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

@Entity // 1) ⚔️ ORM (2) - should I map ORM enttiy directly on my core domain model?

//@Data // 2) ⚔️ Lombok❤️ = @Getter + @Setter + @ToString + @EqualsAndHashCode (1)
// 🙁 @Setter = not all props are mutable
// 🙁 @EqualsAndHashCode = is strange if applied to ID (assigned on repo.save) and fields:
//    the same obj is not equal to itself after it's saved in DB
//    👍 use for hash/eq the natural key: eg: pnr for person
// 🙁 @ToString + @Entity => LAZY LOADING on toString

//class OCJP extends Customer {
//  public void method() {
//    System.out.println(" " + id);
//  }
//}

@Getter
@Setter
// 💙 Domain Model Entity - backbone of your core complexity
public class Customer {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String email;

  // 🤔 Hmm... 3 fields with the same prefix. What TODO
  //  > extract a separate
  //  Value Object = small immutable object w/o persistent identity
  // 👑: we have captured in our syntax a core domain concept that wasn't yet explicitly modeled.
  // ✅ benefit: methods and code all over the place are going to suddenly have to take fewer parameters and have fewer variables,
  // because we can use this value object all over the place.
  // => try to "push it in existing code"😱
//  private String shippingAddressCity;
//  private String shippingAddressStreet;
//  private String shippingAddressZip;

  // since it feels general-purpose, i guess🤔
  // NOW at the start of the proj, when I know the least
//  record Address

  // KISS: go from specific -> general when you have more points to triangulate your desing
  // "The rule of 3": it's ok to copy once, not twice
  @Embedded // no ALTER TABLE NEEDED
  private ShippingAddress shippingAddress;

  public boolean isGoldMember() {
    return goldMember /*|| smth new*/;
  }

  public boolean canReturnOrders() { // DRY
//    return isGoldMember() || isCompany();
    return goldMember || isCompany();
  }

  private boolean isCompany() {// explain the business concept
    return getLegalEntityCode().isEmpty();
  }

  //   tomorrow you'll add a billing address, in RO: {pnr/vat, string address}
  @Embeddable
  public record ShippingAddress(String city, String street, String zip) {
  }

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

  public void validate(String validatedBy) {
    if (status != Status.DRAFT) {
      throw new IllegalStateException("Only customers in DRAFT status can be validated");
    }
    if (validatedBy == null || validatedBy.isBlank()) {
      throw new IllegalArgumentException("validatedBy must be provided when validating a customer");
    }
    this.validatedBy = validatedBy;
    status = Status.VALIDATED;
  }

  public void activate() {
    if (status != Status.VALIDATED) {
      throw new IllegalStateException("Only customers in VALIDATED status can be activated");
    }
    status = Status.ACTIVE;
  }

  public void delete() {
    if (status == Status.DELETED) {
      throw new IllegalStateException("Customer is already deleted");
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
    draftCustomer.validate("NULL"); // for your SQL dev❤️
  }

  public void activate(Customer draftCustomer) {
    draftCustomer.activate();
  }
}
//endregion