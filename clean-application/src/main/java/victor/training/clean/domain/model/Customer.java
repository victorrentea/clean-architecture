package victor.training.clean.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;

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

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
//  private String shippingAddressCity;
//  private String shippingAddressStreet;
//  private String shippingAddressZip;
  @Embedded // NU modifica tabela din spate.
  private ShippingAddress shippingAddress;

//  private BillingAddress shippingAddress; // +VAT code

  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

//  public boolean canReturnOrders(Repo, @Service, -> ) { RAU: coupling la behavior
//  public boolean canReturnOrders(AltaEntitateCu20Campuri ) { RAU: coupling la alte date
  // ==> mut logica in stateless @Service
// public boolean canReturnOrders() { 30 linii de cod, 7 ifuri < de ob pt ca Entitea asta e huge
//      => extragi VO sau spargi @Entity in 2-3

  //  public boolean canReturnOrders(String ) { Merge
//  public boolean canReturnOrders(String,int ) { :/
//  public boolean canReturnOrders(ShippingAddress ) { VO mic 2-5 campuri ~~ merge
  public boolean canReturnOrders() { //90% asa, fara param, pe baza datelor MELE,
    // ideal PURE FUNCTIONS: produc rezultate din ce am eu
    return goldMember || isPhysicalPerson();
  }

  private boolean isPhysicalPerson() {
    return legalEntityCode == null;
  }

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }

  @Setter(NONE)
  private Status status = Status.DRAFT;
  @Setter(NONE)
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later

  public void validate(String validatedBy) {
    if (status!=Status.DRAFT) {
      throw new IllegalStateException("Can't change status from " + status + " to VALIDATED");
    }
    status = Status.VALIDATED;
    this.validatedBy = Objects.requireNonNull(validatedBy);
  }
  public void activate() {
    if (status!=Status.VALIDATED) {
      throw new IllegalStateException("Can't change status from " + status + " to ACTIVE");
    }
    status = Status.ACTIVE;
  }
  public void delete() {
    if (status!=Status.ACTIVE) {
      throw new IllegalStateException("Can't change status from " + status + " to DELETED");
    }
    status = Status.DELETED;
  }

  // @Builder care la .build() sa valideze.
  // @Entity de ORM niciodata sa nu o faci IMUTABILA. Daca insa domain model entity
  // nue ORM ci @Document sau stocat in redis/cassandra, le poti face imutabile => @Builder va chema constructorul
  // care va valida regula
//  public Customer() { ideal pt imutabilitate
//    if (status == Status.VALIDATED || status == Status.ACTIVE || status == Status.DELETED) {
//      if (validatedBy == null) {
//        throw new IllegalStateException("validatedBy is mandatory when status is VALIDATED");
//      }
//     }
//  }
}

//region Code in the project might [not] follow the rule
class CodeFollowingTheRule {
  public void ok(Customer draftCustomer) {
    draftCustomer.validate("currentUser"); // from token/session..
  }
}

class CodeBreakingTheRule {
  public void farAway(Customer draftCustomer) {
    draftCustomer.validate("user");
  }
}
//endregion