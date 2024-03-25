package victor.training.clean.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.NONE;
import static victor.training.clean.domain.model.Customer.Status.DRAFT;
import static victor.training.clean.domain.model.Customer.Status.VALIDATED;

@Entity
@Data // avoid on ORM @Entity because:
// 1) hashCode uses @Id⚠️ 2) toString triggers lazy-loading⚠️ 3) all setters = no encapsulation⚠️
// insteawd only use @Getter @Entity
public class Customer {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String email;

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
  private String shippingAddressCity; // remove
  private String shippingAddressStreet; // remove
  private String shippingAddressZip; // remove

  @Embedded // no changes to DB schema!
  private ShippingAddress shippingAddress; // add
  //  YOUR DOMAIN MODEL should be under MY STRICT EXCLUSIVE CONTROL


  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

  // behavior IN the Entity OOP = ok
  public int discountPercentage() {
    int discountPercentage = 1;
    if (goldMember) {
      discountPercentage += 3;
    }
    return discountPercentage;
  }
  // abuse if
  // - complex logic 35 lines of business rules
  // - unwanted dependencies: f(RestTemplate, KafkaTemplate, CustomerRepo, AService)
  // - pollute it with highly-dependeny formatting/parsing on a single use-case

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }
  @Setter(NONE) // no setter please
  private Status status = DRAFT;
  @Setter(NONE) // no setter please
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later

  public void validate(String byWho) {
    if (status != Status.DRAFT) {
      throw new IllegalStateException("Can only validate DRAFT customers");
    }
    status = VALIDATED;
    validatedBy = requireNonNull(byWho);
  }
  public void activate() {
    if (status != VALIDATED) {
      throw new IllegalStateException("Can only activate VALIDATED customers");
    }
    status = Status.ACTIVE;
  }
  public void delete() {
    if (status != Status.ACTIVE) {
      throw new IllegalStateException("Can only delete ACTIVE customers");
    }
    status = Status.DELETED;
  }
}

//region Code in the project might [not] follow the rule
class CodeFollowingTheRule {
  public void ok(Customer draftCustomer) {
//    draftCustomer.setStatus(VALIDATED);
//    draftCustomer.setValidatedBy("currentUser"); // from token/session..
    draftCustomer.validate("currentUser");
  }
}
class CodeBreakingTheRule {
  public void farAway(Customer draftCustomer) {
//    draftCustomer.setStatus(VALIDATED); // ilegal: missing setValidatedBy
    draftCustomer.validate("currentUser");
  }
}
//endregion