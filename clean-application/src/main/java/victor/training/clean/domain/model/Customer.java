package victor.training.clean.domain.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;
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


  @Embedded
  private ShippingAddress shippingAddress;

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

  public boolean isNaturalPerson() {
    return getLegalEntityCode().isEmpty();
  }

  public boolean canReturnOrders() {
    return isGoldMember() || isNaturalPerson();
  }







  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }

  @Setter(NONE)
  private Status status = Status.DRAFT;
  @Setter(NONE)
  private String validatedBy;

  public Optional<String> getValidatedBy() {
    return Optional.ofNullable(validatedBy);
  }
//  @Valid boolean beforeSave() {...}

//  public void setStatus(Status status) {
//    // validate that validatedBy != null if status = validated or later...
//    this.status = status;
//  }
  public void validate(String user) {
    if (status != Status.DRAFT) throw new IllegalStateException("Can only validate a DRAFT customer, but was " + status);
    status = Status.VALIDATED;
    validatedBy = user;
  }
  public void activate() {
    if (status != Status.VALIDATED) throw new IllegalStateException("Can only activate a VALIDATED customer, but was " + status);
    status = Status.ACTIVE;
  }
  public void delete() {
    if (status == Status.DELETED) throw new IllegalStateException("Customer is already DELETED");
    status = Status.DELETED;
  }
}