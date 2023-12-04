package victor.training.clean.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.NONE;
import static victor.training.clean.domain.model.Customer.Status.*;

@Entity // Hibernate or classes you map jooq Records to/from
@Data
//1)kills encapsulation: degenerates the Java class to a typedef struct,
//    exposes new methods when I add more fields
//2)toString might be too big: even leak GDPR info PII
//3) ORM: toString/equals/hashcode might trigger lazy load
public class Customer {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String email;

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
//  private String shippingAddressCity;
//  private String shippingAddressStreet;
//  private Integer shippingAddressZipCode;
  // + less fields here (smaller class size)
  // + encapsulation: can host methods related to that data (OOP)
  // + encapsulation: Customer doesn't care what fields are in ShippingAddress
  // + better IDE experience,  + more semantics in code
  // + if you call it Address, we can also have invoicingAddress:Address
  //    +reuse vs -coupling
  // + we can pass an object instead of 3 params
  // - more cumbersome to map the jooq record to this class
  //    but that's ok, since I don't care a lot about Repo
  //    for my ORM users: use @Embeddable:
  // - effort
  @Embedded
  private ShippingAddress shippingAddress;

  // also, methods in Customer can guard the consistency of a collection of children
//  private List<String> phoneNumbers = new ArrayList<>();
//  public List<String> getPhoneNumbers() {
//    return Collections.unmodifiableList(phoneNumbers);
//  }
  // + add add Phone/Remove

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
  @Setter(NONE) // no setters
  private Status status;
  @Setter(NONE)
  @JsonIgnore // garbage , and also risky
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later WTF?!!

  public void validate(@NonNull String byWho) {
    if (status != DRAFT) {
      throw new IllegalStateException();
    }
    status = VALIDATED;
    validatedBy = byWho;
  }
}

//region Code in the project might [not] follow the rule
class CodeFollowingTheRule {
  public void ok(Customer draftCustomer) {
    draftCustomer.validate("currentUSer");
  }
}
class CodeBreakingTheRule {
  public void farAway(Customer draftCustomer) {
//    draftCustomer.setStatus(ACTIVE);
    draftCustomer.validate("currentUser");
  }
}
//endregion