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
  private String name;
  private String email;

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
  @Embedded
  private ShippingAddress shippingAddress;

  public boolean canReturnOrders() { // small business rule
    return goldMember || isIndividual();
  }

  public boolean isIndividual() { // explaining data semantics; RICH domain model
    return legalEntityCode == null;
  }

  @Embeddable
  // Value Object design pattern = small immutable object without PK
  //   explicitating a domain concept floatin through my code
  //   deeper domain model
//  public record Address( // "higher" abstraction, more generic
  public record ShippingAddress( // more specific // YAGNI! KISS, don't speculate
                                 String city,
                                 String street,
                                 String zip
//      ,String vatCode
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