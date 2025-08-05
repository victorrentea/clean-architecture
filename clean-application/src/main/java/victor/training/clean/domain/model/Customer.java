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
@Entity // ORM/JPA (2)
// 👑 Domain Model Entity, the backbone of your core complexity.
public class Customer {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String email;

  @Embedded
  private ShippingAddress shippingAddress;

//  private Address billingAddress; {String vatCode/cnp, String address}
//  public record Address( // + generic, tomorrow I could reuse it = premature abstraction.
  // use "the rule of 3" = if you need to use the same type in 3 places, then extract it. see http://www.martinfowler.com/bliki/RuleOfThree.html

  // VO = Value Object = small immutable obj lacking PK
  // = explicitated a concept floating through my code
  @Embeddable
  public record ShippingAddress( // + more precise, more "humble". keep specific, generify ON DEMAND
                                 String city,
                                 String street,
                                 String zip
  ) {}

  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;
  private String legalEntityCode;

  public boolean canReturnOrders() { // bits of business rules
    return goldMember || isIndividual();
  }

  public boolean isIndividual() { // explain data
    return legalEntityCode == null;
  }

//  public void dontDoThis(OrderLarge10FieldsObj) {
//  public void dontDoThis(CustomerRepo) { DB access
//  public void dontDoThis(ExtApiClient) { REST calls
//  public void dontDoThis(ApiDto external) {

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