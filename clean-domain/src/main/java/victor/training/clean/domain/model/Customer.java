package victor.training.clean.domain.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import static lombok.AccessLevel.NONE;

//region Reasons to avoid @Data on Domain Model
// Avoid @Data on Domain Model because:
// 1) hashCode uses @Id⚠️
// 2) toString might trigger lazy-loading⚠️
// 3) all setters/getters = no encapsulation⚠️
//endregion

// This class is part of your Domain Model, the backbone of your core complexity.
@Data // = @Getter @Setter @ToString @EqualsAndHashCode (1)
// Setter = mutable, no encapsulation : anyone can freely change any field

@Entity // ORM/JPA (2)
public class Customer { // mutable💖 ORM entity
  @Id
  @GeneratedValue
  private Long id;
  @Size(min=5,message = "Whatever message I want")
  // + enforced automatically by any Spring Data JPA repo
  // + less code
  // + reports ALL validation errors!!! (polite of you)
  private String name;
  @Email
  @Column(unique = true)
  private String email;

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
//  private String shippingAddressCity;
//  private String shippingAddressStreet;
//  private String shippingAddressZip;

  @Embedded // DOES NOT CHANGE THE DB SCHEMA, no alter-table needed.
//  @AttributeOverrides({
//      @AttributeOverride(name = "city", column = @Column(name = "shipping_address_city")),
//      @AttributeOverride(name = "street", column = @Column(name = "shipping_address_street")),
//      @AttributeOverride(name = "zip", column = @Column(name = "shipping_address_zip"))
//  })
  private ShippingAddress shippingAddress;

  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  // compiler-checked
//  public Optional<String> getLegalEntityCode() {
//    return Optional.ofNullable(legalEntityCode);
//  }

  /** @return null if physical person. */
  @Nullable // IDE-checked
  public String getLegalEntityCode() {
    return legalEntityCode;
  }

  public static final String PERSON_ENTITY = "PERSON"; // null-object pattern

  private String legalEntityCode;
//  private boolean isPhysicalPerson; // redundant field with legalEntityCode
//  private enum type; // redundant field with legalEntityCode

  // if the there is large complexity different between Legal/Physical => 2 subclasses
  // perhaps +7 fields for one
  private boolean discountedVat;



  // Avoid coupling:
  //.. to data
  //  public boolean canReturnOrders(AnotherHugeEntity30fields) { // NEVER
  //.. to logic
  //  public boolean canReturnOrders(ARepo) { // NEVER
  //  public boolean canReturnOrders(AnApiClient) { // NEVER
  //  public boolean canReturnOrders(AnotherDIManagedBean) { // NEVER
  // but okish to have 1-2 primitive/small params

  public boolean canReturnOrders() {
    return goldMember || isPhysicalPerson();
  }

  private boolean isPhysicalPerson() {
    return legalEntityCode == null;
  }

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED;
  }
  @Setter(NONE)
  private Status status = Status.DRAFT;
  @Setter(NONE)
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later

//  @AssertTrue
//  public void method() {
//    if (status == Status.VALIDATED && validatedBy == null) {
//      throw new IllegalStateException("validatedBy must be set when status = VALIDATED");
//    }
//  }

  public void validate(String user) {
    if (status != Status.DRAFT) {
      throw new IllegalStateException("Can only validate a DRAFT Customer");
    }
    this.validatedBy = Objects.requireNonNull(user);
    this.status = Status.VALIDATED;
//     20 lines of logic (7 if(), 1 for)
    // switch (...)
  }

  public void activate() {
    if (status != Status.VALIDATED) { // infant state machine
      throw new IllegalStateException("Can only activate a VALIDATED Customer");
    }
    this.status = Status.ACTIVE;
  }

  protected Customer() {
  } // for hibernate only

  public Customer(String name) {
    this.name = Objects.requireNonNull(name);
  }
}

//region Code in the project might [not] follow the rule
class CodeFollowingTheRule {
  public void ok(Customer draftCustomer) {
//    draftCustomer.setStatus(Customer.Status.VALIDATED);
//    draftCustomer.setValidatedBy("currentUser"); // from token/session..
    draftCustomer.validate("currentUser");
  }
}

class CodeBreakingTheRule {
  public void farAway(Customer draftCustomer) {
//    draftCustomer.setStatus(Customer.Status.VALIDATED);
    draftCustomer.validate("currentUser");
    System.out.println(draftCustomer.getLegalEntityCode().toUpperCase());
  }
}
//endregion


// don't make ORM entities IMMUTABLE. Let them have setters
// or better: mutator methods like validate() that guard a domain invariant