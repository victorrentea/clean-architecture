package victor.training.clean.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import victor.training.clean.application.dto.CustomerDto;

import java.time.LocalDate;
import java.util.Optional;

//region Reasons to avoid @Data on Domain Model
// Avoid @Data on Domain Model because:
// 1) hashCode uses @Id⚠️
// 2) toString might trigger lazy-loading⚠️
// 3) all setters/getters = no encapsulation⚠️
//endregion

// eg: Money{amount, currency}

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
  @Embedded // no ALTER table, just ORM mapping
  private ShippingAddress shippingAddress; // adjusted without breaking the clients or DB schema

  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

  public boolean canReturnOrders() {
    // can this appear in a 'record':
    // a) "record" = java 17+ keyword
    // b) "Record" = message in Kafka = DTO ("api-model")

    // who is defining that contract?
    // REST API=>server
    // Message=>consumer(commands) or producer(events)?

    // is Kafka:
    // (1) transportation => record == dto  << mostly the case
    // (2) source-of-truth(storage)=event sourcing => record == domain model?
    //    :( KStream,KTables processing, hard to build&maintain, expert in (re)partitioning
    // now moving from 2->1
    return goldMember || isPhysicalPerson();
  }

  private boolean isPhysicalPerson() {
    return legalEntityCode == null;
  }

  public Optional<String> getLegalEntityCode() {
    return Optional.ofNullable(legalEntityCode);
  }

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }
  private Status status;
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later

  public void method(CustomerDto bad) {

  }
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