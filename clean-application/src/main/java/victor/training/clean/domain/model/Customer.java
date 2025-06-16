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

@Data // = @Getter @Setter @ToString @EqualsAndHashCode (1)

// (A) ORM/JPA Entity model
@Entity

// 👑 (B) Domain Model Entity: the backbone of your core complexity.
public class Customer {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String email;

  //  @AttributeOverrides()
  @Embedded // NO ALTER TABLE REQUIRED. Thank you ORM!
  private ShippingAddress shippingAddress;

//  public boolean badMethod(boolean ) { acceptable -> split in 2 methods
//  public boolean badMethod(String) { acceptable
//  class CustomerUtil/Hellper {public static boolean badMethod(Customer ) { // Uncle Bob will come for you!

//  public boolean badMethod(ApiClient) // coupling
//  public boolean badMethod(EntityManager) // coupling
//  public boolean badMethod(Order32Fields) // coupling

  public boolean isIndividual() { //explain data
    return legalEntityCode == null;
  }

  public boolean canReturnOrders() { // containing biz rules
//    fiscalAuthority.fetch()
    return goldMember || isIndividual(); // simple logic with only MY FILEDS
    // synthetic getters
  }

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
//  private String shippingAddressCity;
//  private String shippingAddressStreet;
//  private String shippingAddressZip;

  @Embeddable // Value Object (design pattern) = small immutable obj lacking PK
  public record ShippingAddress(String city, String street, String zip) {}

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