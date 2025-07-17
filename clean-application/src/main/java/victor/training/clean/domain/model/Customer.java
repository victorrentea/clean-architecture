package victor.training.clean.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDate;
import java.util.Optional;

//region Reasons to avoid @Data on Domain Model
// Avoid @Data on Domain Model because:
// 1) hashCode uses @Id⚠️
// 2) toString might trigger lazy-loading⚠️
// 3) all setters/getters = no encapsulation⚠️
//endregion

@Data // = @Getter✅
// @Setter🤨 - pune selectiv pe campurile editabile liber
// @ToString❌ - periculos sa triggereze lazy load + waste constrund un string huge + GDPR
// @EqualsAndHashCode❌ - include si @Id generat de hib -> acelasi @entity sa-si schibme hashCode pe parcursul vietii
@Entity // ORM/JPA (2)


// 👑 Domain Model Entity = the backbone of your core complexity.
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
  private ShippingAddress shippingAddress;

  private String vatCode;

  public boolean isNaturalPerson() {
    return vatCode == null;
  }

  //  record Address( // best because we can reuse the object in the future
  // eg. for billing address
  // Value Object (design pattern) = immutable [small] without PK (persistent id)
  //  eg: Money(amm,cur)
  //  eg: GPS(lat,lon)
  public record ShippingAddress( //enrich the language of your domain
                                 String city,
                                 String street,
                                 String zip) {}
//  private Address billingAddress; // tomorrow, perhaps, maybe. wait for tih to come.

  // don't design for reuse tomorrow, - overengineering
  // extract for reuse today - opportunistic refactoring!
  //    on-demand = brave

  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  public boolean canReturnOrders() {
    // better than in a customer Util/Hellper
    return goldMember || isNaturalPerson();
  }
  private boolean discountedVat;

  public Optional<String> getVatCode() {
    return Optional.ofNullable(vatCode);
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