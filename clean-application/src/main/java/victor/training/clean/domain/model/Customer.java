package victor.training.clean.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import victor.training.clean.domain.model.Customer.Status;

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
  @Embedded // asta nu modifica schema DB, table ramane ne-ALTER-ata
  private ShippingAddress shippingAddress;


  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

  public boolean isPhysicalPerson() {
    return legalEntityCode == null;
  }
  public boolean isLegalEntity() {
    return legalEntityCode != null;
  }

  public boolean canReturnOrders() {
    return goldMember || isPhysicalPerson();
  }
  //cand sa NU pui logica in DOMAIN
  // f(int x, String s  ) {✅
  // f(ShoppingCart50Campuri param) {❌
  // f(Un@Service param) {❌
  // f(unRepo param) {❌
  // f(unApiClient param) {❌

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }
  @Setter(NONE)
  private Status status = Status.DRAFT;
  @Setter(NONE)
  private String validatedBy; // ⚠ Always not-null🤞 when status = VALIDATED or later
   // SOC: Obiecte cu pretentii despre integritatea datelor lor
  // changeuri controlate de date
  public void validate(String validatedBy) {
    if (status != Status.DRAFT) {
      throw new IllegalStateException("Can't validate a non-draft customer");
    }
    status = Status.VALIDATED;
    this.validatedBy = Objects.requireNonNull(validatedBy);
  }
  public void activate() {
    if (status != Status.VALIDATED) {
      throw new IllegalStateException("Can't activate a non-validated customer");
    }
    status = Status.ACTIVE;
  }
  public void delete() {
    if (status != Status.ACTIVE) {
      throw new IllegalStateException("Can't delete a non-active customer");
    }
    status = Status.DELETED;
  }

}

//region Code in the project might [not] follow the rule
class CodeFollowingTheRule {
  public void ok(Customer draftCustomer) {
//    draftCustomer.setStatus(Customer.Status.VALIDATED, "curru");
//    draftCustomer.setValidatedBy("currentUser"); // from token/session..
    draftCustomer.validate("currentUser");
  }
}

class CodeBreakingTheRule {
  public void farAway(Customer draftCustomer) {
    draftCustomer.validate("currentUser");
//    draftCustomer.setStatus(Customer.Status.VALIDATED, );
  }
}
//endregion