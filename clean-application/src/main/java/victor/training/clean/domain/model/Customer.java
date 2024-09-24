package victor.training.clean.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;

import static lombok.AccessLevel.NONE;

//region Reasons to avoid @Data on Domain Model
// Avoid @Data on Domain Model because:
// 1) hashCode uses @Id⚠️
// 2) toString might trigger lazy-loading⚠️
// 3) all setters/getters = no encapsulation⚠️
//endregion

// VALUE OBJECT = small immutable (data class with val inside)
// that does not have an identity (PK/SSN..)
record ShippingAddress(
    String city,
    String street,
    String zip) {}
// more example of VO
// record Money(BigDecimal amount, Currency currency) {}
// record Email(String value) {}
// record PhoneNumber(String countryPrefix, String value) {}


// This class is part of your Domain Model, the backbone of your core complexity.
@Data // = @Getter @Setter @ToString @EqualsAndHashCode (1)
@Entity // ORM/JPA (2)
// MY PRIVATE INTERNAL STRUCTURE THAT IF I CHANGE
// I DON'T BREAK ANYONE ELSE'S CODE
// my DOMAIN MODEL: the backbone of my core complexity
// my representation of the business concepts
public class Customer {
  @Id
  @GeneratedValue
  private Long id;
  @NotBlank
  @Size(min = 3, max = 100)
  private String name;
  @Email
//  @Pattern(regexp = ".+@.+\\..+")
  private String email;

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
  private String shippingAddressCity;
  private String shippingAddressStreet;
  private String shippingAddressZip;
//  private ShippingAddress shippingAddress;

  @ManyToOne
  private Country country;
//@PastOrPresent
  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

  // helper methods / properties
  // in kt these can also be extension functions if you can't modify the class
  //   (grpc generated classes)
  public boolean canReturnOrders() {
    return goldMember || isIndividual();
  }

  //  val canReturnOrders get() = goldMember || isIndividual();

  private boolean isIndividual() {
    return legalEntityCode == null;
  }

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }
  //var in kt
  @Setter(NONE)
  private Status status=Status.DRAFT;
  @Setter(NONE)
  private String validatedBy; // ⚠ Always not-null🤞 when status = VALIDATED or later

  // Kotlin
  // sealed interface Status {
  //  object Draft : Status
  //  object Validated(val validatedBy:string) : Status
  //  object Active(validated: Validated) : Status
  //  object Deleted : Status

//  var status: Status = Status.DRAFT

//  public void setStatus(Status newStatus, String who) {
//  }

  public void validate(String who) { // guards my consistency rule = OOP
    if (status != Status.DRAFT) {
      throw new IllegalStateException("Can't validate a non-draft customer");
    }
    status = Status.VALIDATED;
    validatedBy = who;
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



// comments don't get maintained. becomes irrelevant
// comments make up for bad naming/code

//region Code in the project might [not] follow the rule
class CodeFollowingTheRule {
  public void ok(Customer draftCustomer) {
//    draftCustomer.setValidatedBy("currentUser"); // from token/session..
//    draftCustomer.setStatus(Customer.Status.VALIDATED);
    draftCustomer.validate("currentUser");
  }
}

class CodeBreakingTheRule {
  public void farAway(Customer draftCustomer) {
//    draftCustomer.setStatus(Customer.Status.VALIDATED);
    draftCustomer.validate("currentUser");
  }
}
//endregion