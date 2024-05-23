package victor.training.clean.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.NONE;
import static victor.training.clean.domain.model.Customer.Status.VALIDATED;

//region Reasons to avoid @Data on Domain Model
// Avoid @Data on Domain Model because:
// 1) hashCode uses @Id⚠️
// 2) toString might trigger lazy-loading⚠️
// 3) all setters/getters = no encapsulation⚠️
//endregion

// This class is part of your Domain Model, the backbone of your core complexity.
//@Data // = @Getter @Setter @ToString @EqualsAndHashCode (1)
@Getter
@Setter
@Entity // ORM/JPA (2)
public class Customer {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String email;

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
  @Embedded
  private ShippingAddress shippingAddress;

  // SPECULATIVE GENERALITY!!
//  private Address shippingAddress; // gresit
  // ca poate o folosim si ca adresa de facturare
  // da nu va merge ca trebe sa aiba si CUI!

//  @ManyToOne
//  private Country country;

  private Long countryId; // PK in DB

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

  public boolean isPf() {
    return legalEntityCode == null;
  }

  public boolean canReturnOrders() {
    return goldMember || isPf();
  }
  //depinde ....
  // E O IDEE PROASTA SA PUI LOGICA IN DOMAIN MODEL CAND:
  // 1. LOGICA NU E LEGATA DE ENTITATEA RESPECTIVA
  // f(CustomerRepo)
  // f(MaiClient)
  // f(Account30Campuri)
  // f() { 20 linii de cod }

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }
  @Setter(NONE)
  private Status status;
  @Setter(NONE)
  private String validatedBy;
  // ⚠ Always not-null when status = VALIDATED or later

  public void validate(String validatedBy) {
    if (status != Status.DRAFT) {
      throw new IllegalStateException("Can't validate a non-draft customer");
    }
    status = VALIDATED;
    this.validatedBy = requireNonNull(validatedBy);
  }
  public void activate() {
    if (status != VALIDATED) {
      throw new IllegalStateException("Can't activate a non-validated customer");
    }
    status = Status.ACTIVE;
  }
  public void delete() {
    if (status != Status.ACTIVE && status != Status.DRAFT) {
      throw new IllegalStateException("Can't delete a non-active customer");
    }
    status = Status.DELETED;
  }
}





//region Code in the project might [not] follow the rule
class CodeFollowingTheRule {
  public void ok(Customer draftCustomer) {
//    draftCustomer.setStatus(VALIDATED);
//    draftCustomer.setValidatedBy("currentUser"); // from token/session..
    draftCustomer.validate("currentUser");
  }
}

class CodeBreakingTheRule {
  public void farAway(Customer draftCustomer) {
//    draftCustomer.setStatus(VALIDATED);
    draftCustomer.validate("<NULL>"); // 2023-01 TODO fix luni
  }
}
//endregion