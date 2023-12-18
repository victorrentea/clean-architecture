package victor.training.clean.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

import static java.util.Objects.requireNonNull;

// Value Object = small, immutable object that is used as a field in an Entity
// which brings more meaning to the DATA
@Embeddable // capture the concept of a ShippingAddress you are currently using in you req/PO/testes/FE.....
record ShippingAddress(String city, String street, String zip) {
  public ShippingAddress {
    requireNonNull(city);
    requireNonNull(street);
    requireNonNull(zip);
  }
}
@Entity
@Data // BAD: 1) hashCode uses @Id, 2) toString can trigger ORM lazy-loading, 3) setters for all fields = no encapsulation
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
  @Embedded // the fields of ShippingAddress will be columns in the Customer table
  private ShippingAddress shippingAddress;

  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }
  private Status status;
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later
}

//region Code in the project might [not] follow the rule
//class CodeFollowingTheRule {
//  public void ok(Customer draftCustomer) {
//    draftCustomer.setStatus(VALIDATED);
//    draftCustomer.setValidatedBy("currentUser"); // from token/session
//  }
//}
//class CodeBreakingTheRule {
//  public void farAway(Customer draftCustomer) {
//    draftCustomer.setStatus(ACTIVE);
//  }
//}
//endregion