package victor.training.clean.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import victor.training.clean.domain.model.Customer.Status;
import victor.training.clean.domain.repo.CustomerRepo;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Objects;

import static lombok.AccessLevel.NONE;

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

//  @Inject
//  private CustomerRepo repo;
//  @Inject
//  private FiscalAuthorityApi fiscalAuthorityApi;

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
//  private String shippingAddressCity;
//  private String shippingAddressStreet;
//  private String shippingAddressZip;;
  @Embedded // ! no ALTER TABLE NEEDED
  private ShippingAddress shippingAddress =
      new ShippingAddress("null", null, null);

  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

  public boolean canReturnOrders() {
    return goldMember || isIndividual();
  }

  public boolean isIndividual() {
    return getLegalEntityCode() == null;
  }

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }
  @Setter(NONE)
  private Status status=Status.DRAFT;
  @Setter(NONE)
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later

//  public void onEvent(CustomerValidatedEvent event) {
//    status = Status.VALIDATED;
//    validatedBy = event.getValidatedBy();
//  }

  public void validate(String validatedBy) {
    // return List.of(new CustomerValidatedEvent(validatedBy));
    if (status != Status.DRAFT) {
      throw new IllegalStateException("Cannot validate a non-draft Customer");
    }
    status = Status.VALIDATED;
    this.validatedBy = Objects.requireNonNull(validatedBy);
  }

  public void activate() {
    if (status != Status.VALIDATED) {
      throw new IllegalStateException("Cannot activate a non-validated Customer");
    }
    status = Status.ACTIVE;
  }

  public void delete() {
    if (status != Status.ACTIVE) {
      throw new IllegalStateException("Cannot delete a non-active Customer");
    }
    status = Status.DELETED;
  }

}

//region Code in the project might [not] follow the rule
class CodeFollowingTheRule {
  public void ok(Customer draftCustomer) {
//    draftCustomer.setStatus(Customer.Status.VALIDATED, "currentUser");
//    draftCustomer.setValidatedBy("currentUser"); // from token/session..
    draftCustomer.validate("currentUser");
  }
}

class CodeBreakingTheRule {
  public void farAway(Customer draftCustomer) {
//    draftCustomer.setStatus(Customer.Status.VALIDATED, "NULL");
//    draftCustomer.setStatus(Status.ACTIVE, "????????");
    draftCustomer.validate(null); // exception
  }
}
//endregion