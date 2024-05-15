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
@Data // = @Getter @Setter @ToString @EqualsAndHashCode (1)
@Entity // ORM/JPA (2)
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
  @Embedded // NU modifica tabela din spate.
  private ShippingAddress shippingAddress;

//  private BillingAddress shippingAddress; // +VAT code

  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

//  public boolean canReturnOrders(Repo, @Service, -> ) { RAU: coupling la behavior
//  public boolean canReturnOrders(AltaEntitateCu20Campuri ) { RAU: coupling la alte date
      // ==> mut logica in stateless @Service
// public boolean canReturnOrders() { 30 linii de cod, 7 ifuri < de ob pt ca Entitea asta e huge
//      => extragi VO sau spargi @Entity in 2-3

//  public boolean canReturnOrders(String ) { Merge
//  public boolean canReturnOrders(String,int ) { :/
//  public boolean canReturnOrders(ShippingAddress ) { VO mic 2-5 campuri ~~ merge
  public boolean canReturnOrders() { //90% asa, fara param, pe baza datelor MELE,
    // ideal PURE FUNCTIONS: produc rezultate din ce am eu
    return goldMember || isPhysicalPerson();
  }

  private boolean isPhysicalPerson() {
    return legalEntityCode == null;
  }

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }
  private Status status;
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later
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