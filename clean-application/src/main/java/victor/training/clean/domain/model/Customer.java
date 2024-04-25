package victor.training.clean.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;


//region Reasons to avoid @Data on Domain Model
// Avoid @Data on Domain Model because:
// 1) hashCode uses @Id⚠️
// 2) toString might trigger lazy-loading⚠️
// 3) all setters/getters = no encapsulation⚠️
//endregion

// This class is part of your Domain Model, the backbone of your core complexity.
@Data //(1) =
// @Getter++ @Setter = screw encapsulation
// @ToString = can trigger LazyLoading and StackOverflowException when circular references
// @EqualsAndHashCode = don;t implement hash/eq on domain model/@Entity

@Entity // ORM/JPA (2)

@SequenceGenerator(name = "CUSTOMER_SEQ", sequenceName = "CUSTOMER_SEQ", allocationSize = 1)
public class Customer {
  @Id
  @GeneratedValue(generator = "CUSTOMER_SEQ")
  private Long id;
  private String name;
  private String email;

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
//  private String shippingAddressCity;
//  private String shippingAddressStreet;
//  private String shippingAddressZip;
  @Embedded // did not change the DB schema
  private ShippingAddress shippingAddress;

  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

  private Status status;
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later
  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }
}

//region Code in the project might [not] follow the rule
//class CodeFollowingTheRule {
//  public void ok(Customer draftCustomer) {
//    draftCustomer.setStatus(Customer.Status.VALIDATED);
//    draftCustomer.setValidatedBy("currentUser"); // from token/session..
//  }
//}

//class CodeBreakingTheRule {
//  public void farAway(Customer draftCustomer) {
//    draftCustomer.setStatus(Customer.Status.VALIDATED);
//  }
//}
//endregion