package victor.training.clean.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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