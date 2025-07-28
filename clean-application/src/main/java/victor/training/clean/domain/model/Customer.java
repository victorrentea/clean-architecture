package victor.training.clean.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static lombok.AccessLevel.NONE;

//region Reasons to avoid @Data on Domain Model
// Avoid @Data on Domain Model because:
// 1) hashCode uses @Id⚠️
// 2) toString might trigger lazy-loading⚠️
// 3) all setters/getters = no encapsulation⚠️
//endregion

@Data // = @Getter @Setter @ToString @EqualsAndHashCode (1)
//@Entity // ORM/JPA (2)
// 👑 Domain Model Entity, the backbone of your core complexity.
public class Customer {
//  public Customer(String legalEntityCode,String email) {
//    this.legalEntityCode = Objects.requireNonNull(legalEntityCode);
//    this.email = Objects.requireNonNull(email);
  ////    // to report all errors at once, collect them first
  ////    List<String> errors = new ArrayList<>();
  ////    if (legalEntityCode == null) {
  ////      errors.add("Legal entity code is null");
  ////    }
  ////    if (email == null) {
  ////      errors.add("Email is null");
  ////    }
//  }
  @Id
  @GeneratedValue

  private Long id;
  private String name;
  @Email
  private String email; // yummy

  @Embedded
  private ShippingAddress shippingAddress;


  // explicitated a domain concept into its own type
  // Value Object (design pattern) = small, immutable, lacking PK
  @Embeddable
  public record ShippingAddress(
      String city, String street, String zip) {}
  // wait for the requirement for billing tomorrow to go from specific -> generic (KISS)

  // it can be reused tomorrow for billing (for reuse tomorrow)
  //  record Address
  //  private Address billingAddress;

  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  @NotNull
  private String legalEntityCode;
  private boolean discountedVat;

  // DON'T:
  //- public boolean canReturnOrders() { SecurityContextHolder...hasRole
  //- { 12 x if
  //- f(3 params) or f(GodObject20FieldsUnrelated)
  //- f(CustomerRepo) or f(AnApiClient)

  public boolean canReturnOrders() {
    return goldMember || isPerson();
  }

  public boolean isPerson() {
    return legalEntityCode == null;
  }

  public Optional<String> getLegalEntityCode() {
    return Optional.ofNullable(legalEntityCode);
  }


  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }

  @Setter(NONE)
  private Status status = Status.DRAFT;
  @Setter(NONE)
  private String validatedBy;

  public void validate(String user) {
    if (status != Status.DRAFT) {
      throw new IllegalStateException();
    }
    validatedBy = Objects.requireNonNull(user);
    status = Status.VALIDATED;
  }

  public void activate() {
    if (status != Status.VALIDATED) {
      throw new IllegalStateException();
    }
    status = Status.ACTIVE;
  }

  public void delete() {
    if (status == Status.DELETED) {
      throw new IllegalStateException();
    }
    status = Status.DELETED;
  }
}

//region Code in the project might [not] follow the rule
class SomeCode {
  public void correct(Customer draftCustomer) {
    draftCustomer.validate("currentUser"); // from token/session..
  }

  public void incorrect(Customer draftCustomer) {
    draftCustomer.validate("null");
  }

  public void activate(Customer draftCustomer) {
    draftCustomer.setStatus(Customer.Status.ACTIVE);
  }
}
//endregion