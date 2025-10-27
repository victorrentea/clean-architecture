package victor.training.clean.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import victor.training.clean.domain.repo.CustomerRepo;

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
  @Embedded // no ALTER TABLE required
  private ShippingAddress shippingAddress;

  public boolean canReturnOrders() { // reuse
    return goldMember || isPrivatePerson();
  }

  private boolean isPrivatePerson() { // explaining the meaning of data in my domain model
    return getLegalEntityCode().isEmpty();
  }

  // "Value Object" Design Pattern= small immutable type
  @Embeddable
  public record ShippingAddress( // A: go humbly from specific -> generic
                                 // !=
//  record Address(
                                 // B: you can can reuse it tomorrow for eg> (YAGNI,KIS,Stupid violation)
                                 // can be a "premature abstraction" = (wrong) speculation
                                 // BillingAddress(String name, String address, String vatCode)
                                 String city,
                                 String street,
                                 String zip
  ) {}

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
  private String validatedBy; // WARNING: Always not-null when status = VALIDATED or later

//  public void bad(CustomerRepo) {
//  public void bad(AnafApiClient) {
//  public void bad(Order{32 field}) {


  public void validate(String user) {
    if (status != Status.DRAFT) {
      throw new IllegalStateException();
    }// BAD if repeate 7x
    this.validatedBy = Objects.requireNonNull(user); // defensive programming
    status = Status.VALIDATED;
  }

  public void activate() {
    if (status != Status.VALIDATED) {
      throw new IllegalStateException();
    }
    status = Status.ACTIVE;
  }

  public void delete() {
    if (status == Status.DELETED) {
      throw new IllegalStateException();
    }
    status = Status.DELETED;
  }

}
//region Code in the project might [not] follow the rule
class SomeCode {
  public void correct(Customer draftCustomer) {
    draftCustomer.validate("currentUser"); // from token/session..
  }

  public void incorrect(Customer draftCustomer) {
    draftCustomer.validate("null"); // you can't defend against bad will
  }

  public void activate(Customer draftCustomer) {
    draftCustomer.activate();
  }
}
//endregion