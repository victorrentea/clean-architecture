package victor.training.clean.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


import static java.util.Objects.requireNonNull;

// SHOCK: my domain model maps via JPA/ORM/Doctrine to DB schema, like 95% of Java teams do today
@Entity // >80% of teams use JPA/Hibernate/Doctrine
// JPA + DOMAIN MODE = 💖💖💖💖💖💖
@Data // Lombok BAD on Domain Model:
// 1) hashCode uses @Id,
// 2) toString can trigger ORM lazy-loading,
// 3) setters for all fields = no encapsulation
public class Customer {

//  private CustomerRepo repo;
//  private LdapApi ldapApi;

//  @JsonFormat(pattern = "yyyy-MM-dd") // Violates MVC: UI format in Domain Model
//  @JsonIgnore // pollutes the domain
//  @ElementCollection
//  List<String> phones;
  @Id
  @GeneratedValue
  private Long id;
  @Setter(AccessLevel.NONE)
  @Size(min = 5)
  private String name;
  private String email;

  protected Customer() {} // for Hibernate only

  public Customer(String name) {
    this.name = requireNonNull(name);
  }

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
//  private String shippingAddressCity;
//  private String shippingAddressStreet;
//  private String shippingAddressZip;
  @Embedded // grouping the 3 columns in CUSTOMER into a new type
  private ShippingAddress shippingAddress; // move to ShippingCustomer differnt @Entity in a separate module  +5%

  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

  // OOP: keep behavior close to data
  public int getDiscountPercentage() { // not in some obscure CustomerUtil|Helper
    // 1-5-7 lines of reusable logic, without any external coupling
    int discountPercentage = 1;
    if (goldMember) {
      discountPercentage += 3; // if the calculation grows insanely complex, uses external stuff, move it to a stateless Service
    }
    return discountPercentage;
  }




  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }
  @Setter(AccessLevel.NONE)
  private Status status;
  @Setter(AccessLevel.NONE)
  private String validatedBy; // MAKE THIS USELESS: ⚠ Always not-null when status = VALIDATED or later 🤞we hope

  public void validate(String user) { // is AI a friend of a foe?
    if (status != Status.DRAFT) {
      throw new IllegalStateException("Can only validate DRAFT customers");
    }
    status = Status.VALIDATED;
    validatedBy = requireNonNull(user);
  }

  public void activate() {
    if (status != Status.VALIDATED) {
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


//  public victor.training.clean.application.dto.CustomerDto toDto() {
//     shit coming up.....
//  }
}

//region Code in the project might [not] follow the rule
//class CodeFollowingTheRule {
//  public void ok(Customer draftCustomer) {
//    draftCustomer.setStatus(VALIDATED);
//    draftCustomer.setValidatedBy("currentUser"); // from token/session
//  }
//}
//class CodeBreakingTheRule {
//  public void farAway(Customer draftCustomer) {
//    draftCustomer.setStatus(ACTIVE);
//  }
//}
//endregion