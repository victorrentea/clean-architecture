package victor.training.clean.domain.model;

import jakarta.persistence.*;
import lombok.Data;
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

@Entity // ORM (2)
@Data // = @Getter + @Setter + @ToString + @EqualsAndHashCode (1)
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
  @Embedded // no ALTER TABLE
  private ShippingAddress shippingAddress;

  // Rich Domain Model contains bits of logic
  // ~>CustomerUtil🚽❌❌❌❌❌ DON'T; lost = "OOP paradigm!
  public boolean isNaturalPerson() {
    // ✅ more discoverable than a Util = reuse
    // ✅ speaking the ubiquituous language - explain terms
    return getLegalEntityCode().isEmpty();
  }

  public boolean canReturnOrders() {
    return goldMember || isNaturalPerson();
  }


  //  record ShippingAddressDetails( -Details/-Info/-Data = dull words
  // Value Object design pattern = small immutable object lacking PK(id)
  // ⭐️capturing a domain (business) concept
  @Embeddable
  public record ShippingAddress(
          String city,
          String street,
          String zip
  ) {
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

  public void validate(String user) {
    if (status != Status.DRAFT) throw new IllegalStateException();
    validatedBy = Objects.requireNonNull(user);
    status = Status.VALIDATED;
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