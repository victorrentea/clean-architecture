package victor.training.clean.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static lombok.AccessLevel.NONE;
import static victor.training.clean.domain.model.Customer.Status.DRAFT;
import static victor.training.clean.domain.model.Customer.Status.VALIDATED;

//region Reasons to avoid @Data on Domain Model
// Avoid @Data on Domain Model because: see ADR-002
// 1) hashCode uses @Id⚠️ generated by ORM
// 2) toString might trigger lazy-loading of collections⚠️
// 3) setters/getters on all fields = no encapsulation⚠️ ~ typedef struct
//endregion

@Entity // ORM/JPA (2)
// 👑 Domain Model Entity, the backbone of your core complexity.
//@Data // = @Getter @Setter @ToString @EqualsAndHashCode (1)
@Getter
@Setter // 🙈
public class Customer {
  @Id
  @GeneratedValue
  private Long id;
  @Size(min = 5)
  @NotNull
  private String name;
  @Email
  @NotNull
  private String email;

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
//  private String shippingAddressCity;
//  private String shippingAddressStreet;
//  private String shippingAddressZip;
  // any client doing setShippingAddressCity("Bucharest")
  //      and ..Street("A") and ..Zip("B")
  // will now call customer.setShippingAddress(new ShippingAddress(
  //     "Bucharest", "A", "B"))

  @Embedded // no ALTER table needed
  private ShippingAddress shippingAddress;

  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

//  public static Customer toEntity(CustomerDto dto) {
//    // this method could go:
//    // - stay here if simple
//    // - DTO
//    // - DM "Customer" = WRONG because I couple my holy DM to ONE of the way I present this data
//    // - CustomerMapper (hand written or MapStruct-generated)
//    Customer customer = new Customer();
//    customer.setEmail(dto.email());
//    customer.setName(dto.name());
//    customer.setCreatedDate(LocalDate.now());
//    customer.setCountry(new Country().setId(dto.countryId()));
//    customer.setLegalEntityCode(dto.legalEntityCode());
//    return customer;
//  }

  public boolean canReturnOrders() {
    return goldMember || isIndividual();
  }

  private boolean isIndividual() {
    return legalEntityCode == null;
  }

  public Optional<String> getLegalEntityCode() {
    return Optional.ofNullable(legalEntityCode);
  }

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED;

    public void shouldBeOneOf(Status... expected) {
      Stream.of(expected)
          .filter(e -> e == this)
          .findAny()
          .orElseThrow(() -> new IllegalStateException("Expected status: " + expected));
    }
  }
  @Setter(NONE) // sorry for using Lombok
  private Status status = DRAFT;
  @Setter(NONE)
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later

  public void validate(String username) {
    status.shouldBeOneOf(DRAFT);
    status = VALIDATED;
    validatedBy = Objects.requireNonNull(username);
  }

  public void activate() {
    status.shouldBeOneOf(VALIDATED);
    status = Status.ACTIVE;
  }

  public void delete() {
    status = Status.DELETED;
  }
}
// so the customer can go from draft to validated but not active but you can also get from draft directly to delete it right and if it's activated, it can get deleted once it is deleted. Can you get back to activate it? No OK I noticed it down.
// how about active to delete it no.

//region Code in the project might [not] follow the rule
class SomeCode {
  public void correct(Customer draftCustomer) {
//    draftCustomer.setStatus(VALIDATED);
//    draftCustomer.setValidatedBy("currentUser"); // from token/session..
    draftCustomer.validate("currentUser");
  }
  public void incorrect(Customer draftCustomer) {
//    draftCustomer.setStatus(VALIDATED);
    draftCustomer.validate("null");
  }
  public void activate(Customer draftCustomer) {
    draftCustomer.activate();
  }
}
//endregion