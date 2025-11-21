package victor.training.clean.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import static lombok.AccessLevel.NONE;
import static victor.training.clean.domain.model.Customer.Status.*;

//region Reasons to avoid @Data on Domain Model
// Avoid @Data on Domain Model because:
// 1) hashCode uses @Id⚠️
// 2) toString might trigger lazy-loading⚠️
// 3) all setters/getters = no encapsulation⚠️
//endregion

@Entity // ORM (2)
@Getter
@Setter
//@Data // = @Getter + @Setter + @ToString + @EqualsAndHashCode (1)
// 💙 Domain Model Entity - backbone of your core complexity
public class Customer {
  @Id
  @GeneratedValue
  private Long id;

//  private String id; //"ROU-2023-01-01-asdsa6d" Semantic ID
//  private String id=randomUUID();

  //  @EmbeddedId
//  private CustomerId id;
//  public record CustomerId(Long value) {
//  }
  private String name;
  private String email;

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
//  private String shippingAddressCity;
//  private String shippingAddressStreet;
//  private String shippingAddressZip;

  //  private Address shippingAddress;
  // 1) too generic?, in case I will have tomorrow🤞 (premature abstraction)
  // 2) a billing address too; {CNP||VATCode}; more work later to "make abstract -> concrete"
  @Embedded
  private ShippingAddress shippingAddress; // prefer to go from specific to generic


  // "The rule of 3" (XP) => copy -paste that Util in 2 projects,
  // but for the third extract it in my-commons-v1.jar

  public boolean canReturnOrders() { // a small business rule operating strictly on MY fields
    return goldMember || isNaturalPerson();
  }
  // public ? badish(XXSValueObject2field) {..}

  // public ? bad(Dto) {..} = external corruption
  // public ? bad(boolean) {..} = SRP violation
  // public ? BAD(XXLEntity{20+Fields}) {..}
  // public ? bad(XService,Repo,ApiClient) {.2-200lines.}

//  public ? BAD(Contract)
//  @ManyToOne(fetchType=EAGER) //default
//  @ManyToOne(fetchType=LAZY) //magic
//  Contract{20+2lines} contract;// BAD

//  Long contractId;// (keep FK) ❤️OK "Aggregates should only keep IDs of other Aggregates"
// aggregate = cluster of objects changed atomically, having a root "owning the other objects"
// - Order{List<OrderLine>, ShippingAddressVO} aggregate

// ± performance:
//    + faster: hibernate doesn't JOIN/SELECT Contract = more control
//    - slower: I will have to contractRepo.fbi(customer.contractId) = +1 SELECT = 2..5ms
// + decoupling: reason in isolation from Contract
// + easier to split in modules tomorrow







  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  public boolean isNaturalPerson() { // explaining meaning of fields in ubiquitous language
    return getLegalEntityCode().isEmpty();
  }


  // extract a "Value Object"
  // > mapped to a business term
  // ± constrained (@NotNUll, requireNotNull)
  // > defined by its fields (hash/eq) = no id (no persistent life)
  // > immutable!
  @Embeddable
  // deeper Domain Model (not flat)
  public record ShippingAddress(
      String street,
      String city,
      String zip
  ) {
    // bits of logic; constraint
  }

  private String legalEntityCode;
  private boolean discountedVat;

  public Optional<String> getLegalEntityCode() {
    return Optional.ofNullable(legalEntityCode);
  }

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }

  @Setter(NONE)
  private Status status = DRAFT;
  @Setter(NONE)
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later

  public void validate(String validatedBy) {
    if (status != Status.DRAFT) {
      throw new IllegalStateException("Only DRAFT customers can be validated");
    }
    this.validatedBy = Objects.requireNonNull(validatedBy);
    status = VALIDATED;
  }

  public void activate() {
    if (status != Status.VALIDATED) {
      throw new IllegalStateException("Only VALIDATED customers can be activated");
    }
    status = Status.ACTIVE;
  }

  public void delete() {
    if (status == DELETED) {
      throw new IllegalStateException("Customer is already DELETED");
    }
    status = DELETED;
  }
}

//region Code in the project might [not] follow the rule
class SomeCode {
  public void correct(Customer draftCustomer) {
//    draftCustomer.setStatus(VALIDATED);
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