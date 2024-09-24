package victor.training.clean.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Email;
import lombok.Data;

import java.time.LocalDate;

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

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }
  private Status status;
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later
}

//region Code in the project might [not] follow the rule
//class CodeFollowingTheRule {
//  public void ok(Customer draftCustomer) {
//    draftCustomer.setStatus(Customer.Status.VALIDATED);
//    draftCustomer.setValidatedBy("currentUser"); // from token/session..
//  }
//}

//class CodeBreakingTheRule {
//  public void farAway(Customer draftCustomer) {
//    draftCustomer.setStatus(Customer.Status.VALIDATED);
//  }
//}
//endregion