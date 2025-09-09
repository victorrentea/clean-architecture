package victor.training.clean.domain.model;

import jakarta.persistence.*;
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

@Entity // ORM (2)
@Data // = @Getter + @Setter + @ToString + @EqualsAndHashCode (1)
// 💙 Domain Model Entity - backbone of your core complexity
public class Customer {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String email;

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
  @Embedded
  private ShippingAddress shippingAddress;

  public boolean canReturnOrders() {
    return goldMember || isNaturalPerson();
  }

  public boolean isNaturalPerson() {
    return legalEntityCode == null;
  }

  @Embeddable
  // "Value Object" pattern = imutabil, fara PK
//  public record CustomerShippingAddress( // specific Extreme
//  public record Address(// generic Extreme
  // + poate o folosim si ca adresa de facturare MAINE = Premature Abstraction
  //    firma: nume firma, CUI, adress:String
  public record ShippingAddress(
      String city,
      String street,
      String zip
  ) {}

  // Dar daca as avea un **abstract** class BaseAddress cu 2 subclase
  // Q:Vei folosi vreodata polimorfic ShippingAddress si/sau InvoiceAddress ca doar un "Address"

  // tl;dr tine designul umil si specific pana cand ai nevoie sa-l generalizezi
  // + #trust ca vei stii sa faci refactoring si tu si colegii cand o fi cazul./
  // oricum azi esti mai prost ca maine.

  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

  public Optional<String> getLegalEntityCode() {
    return Optional.ofNullable(legalEntityCode);
  }


  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }

  @Setter(NONE)
  private Status status = Status.DRAFT;
  @Setter(NONE)
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later

  public void validate(String username) {
    if (status != Status.DRAFT) {
      throw new IllegalStateException("Haina sifonata!");
    }
    status = Status.VALIDATED;
    validatedBy = Objects.requireNonNull(username);
  }

  public void activate() {
    if (status != Status.VALIDATED) {
      throw new IllegalStateException("Haina sifonata!");
    }
    status = Status.ACTIVE;
  }

  public void delete() {
    if (status == Status.DELETED) {
      throw new IllegalStateException("Haina deja aruncata!");
    }
    status = Status.DELETED;
  }

}

//region Code in the project might [not] follow the rule
class SomeCode {
  public void correct(Customer draftCustomer) {
//    draftCustomer.setStatus(Customer.Status.VALIDATED);
//    draftCustomer.setValidatedBy("currentUser"); // from token/session..
    draftCustomer.validate("currentUser");
  }

  public void incorrect(Customer draftCustomer) {
    draftCustomer.validate(null);
    // oups!
  }

  public void activate(Customer draftCustomer) {
    draftCustomer.activate();
  }
}
//endregion