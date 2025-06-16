package victor.training.clean.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.h2.schema.Domain;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

//region Reasons to avoid @Data on Domain Model
// Avoid @Data on Domain Model because:
// 1) hashCode uses @Id⚠️
// 2) toString might trigger lazy-loading⚠️
// 3) all setters/getters = no encapsulation⚠️
//endregion

// Avoid on Domain Model
//@Data // = @Getter @Setter @ToString @EqualsAndHashCode (1)

// (A) ORM/JPA Entity model
@Entity
@Getter
@Setter

// 👑 (B) Domain Model Entity: the backbone of your core complexity.
public class Customer {
  @Id
  @GeneratedValue
  private Long id;
  //  @NotNull
//  @Size(min = 4)
  private String name;
  //  @Setter
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

  @Setter(AccessLevel.NONE)
  private Status status = Status.DRAFT;
  @Setter(AccessLevel.NONE)
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later

  void setStatus(Status status) {
    this.status = status;
  }

  public void validate(@NotNull String validatedBy) {
    if (status != Status.DRAFT) {
      throw new IllegalStateException("Cannot validate a customer that is not in DRAFT status");
    }
    this.status = Status.VALIDATED;
    this.validatedBy = requireNonNull(validatedBy);
  }

  public void activate() {
    if (status != Status.VALIDATED) {
      throw new IllegalStateException("Cannot activate a customer that is not VALIDATED");
    }
    this.status = Status.ACTIVE;
  }

  public void delete() {
    if (status == Status.DELETED) {
      throw new IllegalStateException("Cannot delete a customer that is already DELETED");
    }
    this.status = Status.DELETED;
  }

  // instead of a custom-validator use this:
//  @AssertTrue(message = "validatedBy must be set if status is VALIDATED or later")
//  public boolean isValidStatusProgression() {
//    if (status == null) return true;
//    return switch (status) {
//      case DRAFT -> true;
//      case VALIDATED, ACTIVE, DELETED -> validatedBy != null;
//    };
//  }
}

//region Code in the project might [not] follow the rule
//@Service
class SomeService {
  public void correct(Customer draftCustomer) {
    // what about business rules involving external systems? => never inside Domain Model.
    // let them here, in a stateless service containing logic
    draftCustomer.validate("currentUser"); // from token/session..
  }

  public void incorrect(Customer draftCustomer) {
    draftCustomer.validate("null");
  }

  public void activate(Customer draftCustomer) {
    draftCustomer.activate();
  }
}
//endregion