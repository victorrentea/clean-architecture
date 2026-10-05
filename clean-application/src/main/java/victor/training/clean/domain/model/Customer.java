package victor.training.clean.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

//region Reasons to avoid @Data on Domain Model
// Avoid @Data on Domain Model because:
// 1) hashCode uses @Id⚠️
// 2) toString might trigger lazy-loading⚠️
// 3) all setters/getters = no encapsulation⚠️
//endregion

@Entity // ORM (2)
@Getter
@Setter
//+@Data = 😈 // = @Getter + @Setter + @ToString +
// a) @EqualsAndHashCode ia si idul in calcul, ca se modifica la repo.save() (1)
// b) @ToString triggeruieste eager load pe toate colectiile, QUERYURI MULTE
// c) @Setter = uneori ne-necesar
// 💙 Domain Model Entity - backbone of your core complexity
public class Customer {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String email;

  // ✅ The 3 fields with the same prefix became a Value Object
  @Embedded // null when the customer has no shipping address yet
  // nu-i nevoie de ALTER TABLE
  private ShippingAddress shippingAddress;

  @ManyToOne
  private Country country;
//  private Long countryId; // daca ar fi fost agfregate atunci nu aveai voie să ții relație la alt agregat care nu era parte din tine ca identitate.
//
//Și atunci ce făceai? Puneai doar ID-ul

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode; // 📖 = CUI (fiscal code). Absent => the customer is a natural person
  private boolean discountedVat;

  public Optional<String> getLegalEntityCode() {
    return Optional.ofNullable(legalEntityCode);
  }

  // 📖 Ubiquitous Language (confirmed with Bizu, the domain expert):
  // a customer with a CUI (legalEntityCode) is a company; without it, a natural person.
  public boolean isCompany() {
    return getLegalEntityCode().isPresent();
  }

  public boolean isNaturalPerson() {
    return !isCompany();
  }

  // 💚 domain logic over the state of this entity belongs here, not in the callers
  public boolean canReturnOrders() {
    return goldMember || isNaturalPerson();
  }

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }

  // 🔒 no setters: changed only by the methods below
  @Setter(AccessLevel.NONE)
  private Status status = Status.DRAFT;
  @Setter(AccessLevel.NONE)
  private String validatedBy; // Always not-null when status = VALIDATED or later

  public void validate(String validatedByUsername) {
    if (status != Status.DRAFT) throw new IllegalStateException("Can only validate a DRAFT, was " + status);
    status = Status.VALIDATED;
    validatedBy = requireNonNull(validatedByUsername);
  }

  public void activate() {
    if (status != Status.VALIDATED) throw new IllegalStateException("Can only activate a VALIDATED, was " + status);
    status = Status.ACTIVE;
  }

  public void delete() { // allowed from any state
    status = Status.DELETED;
  }
}

//region Code in the project can no longer break the rule
class SomeCode {
  public void correct(Customer draftCustomer) {
    draftCustomer.validate("currentUser"); // from token/session..
  }
  public void incorrect(Customer draftCustomer) {
    // draftCustomer.validate(null); // 💥 NPE: you cannot forget WHO validated it
    // draftCustomer.setStatus(VALIDATED); // 💥 does not compile anymore: no setter
  }
  public void activate(Customer draftCustomer) {
    draftCustomer.activate(); // 💥 IllegalStateException if it was not VALIDATED first
  }
}
//endregion