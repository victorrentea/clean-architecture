package victor.training.clean.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Optional;

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

//  private String id; //"ROU-2023-01-01-asdsa6d" Semantic ID

  //  @EmbeddedId
//  private CustomerId id;
//  public record CustomerId(Long value) {
//  }
  private String name;
  private String email;

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
//  private String shippingAddressCity;
//  private String shippingAddressStreet;
//  private String shippingAddressZip;

  //  private Address shippingAddress;
  // 1) too generic?, in case I will have tomorrow🤞 (premature abstraction)
  // 2) a billing address too; {CNP||VATCode}; more work later to "make abstract -> concrete"
  @Embedded
  private ShippingAddress shippingAddress; // prefer to go from specific to generic


  // "The rule of 3" (XP) => copy -paste that Util in 2 projects,
  // but for the third extract it in my-commons-v1.jar

  public boolean canReturnOrders() { // a small business rule operating strictly on MY fields
    return goldMember || isNaturalPerson();
  }


  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private boolean isNaturalPerson() { // explaining meaning of fields in ubiquitous language
    return getLegalEntityCode().isEmpty();
  }

  // extract a "Value Object"
  // > mapped to a business term
  // ± constrained (@NotNUll, requireNotNull)
  // > defined by its fields (hash/eq) = no id (no persistent life)
  // > immutable!
  @Embeddable
  // deeper Domain Model (not flat)
  public record ShippingAddress(
      String street,
      String city,
      String zip
  ) {
    // bits of logic; constraint
  }

  private String legalEntityCode;
  private boolean discountedVat;

  public Optional<String> getLegalEntityCode() {
    return Optional.ofNullable(legalEntityCode);
  }

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }
  private Status status;
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later
}

//region Code in the project might [not] follow the rule
//class SomeCode {
//  public void correct(Customer draftCustomer) {
//    draftCustomer.setStatus(Customer.Status.VALIDATED);
//    draftCustomer.setValidatedBy("currentUser"); // from token/session..
//  }
//  public void incorrect(Customer draftCustomer) {
//    draftCustomer.setStatus(Customer.Status.VALIDATED);
//  }
//  public void activate(Customer draftCustomer) {
//    draftCustomer.setStatus(Customer.Status.ACTIVE);
//  }
//}
//endregion