package victor.training.clean.domain.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

//@Configurable
@Entity
// BAD: 1) hashCode uses @Id, 2) toString can trigger ORM lazy-loading, 3) setters for all fields = no encapsulation
public class Customer {

  //  @Autowired
//  it works@!!!
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String email;

  @Embedded
  private Address shippingAddress;

  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;

  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

  public Customer() {
  }

  //  public int discountPercentage(CustomerRepo repo) {
//  public int discountPercentage(AnafApiClient repo) {
//  public int discountPercentage(Large25FieldsObject) {
  public int discountPercentage() { // pure function just producing a value based on the state of the object
    int discountPercentage = 1;
    if (goldMember) {
      discountPercentage += 3;
    }
    return discountPercentage;
  }

  public Long getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public String getEmail() {
    return this.email;
  }

  public Address getShippingAddress() {
    return this.shippingAddress;
  }

  public Country getCountry() {
    return this.country;
  }

  public LocalDate getCreatedDate() {
    return this.createdDate;
  }

  public String getCreatedByUsername() {
    return this.createdByUsername;
  }

  public boolean isGoldMember() {
    return this.goldMember;
  }

  public String getGoldMemberRemovalReason() {
    return this.goldMemberRemovalReason;
  }

  public String getLegalEntityCode() {
    return this.legalEntityCode;
  }

  public boolean isDiscountedVat() {
    return this.discountedVat;
  }

  public Status getStatus() {
    return this.status;
  }

  public Optional<String> getValidatedBy() {
    return Optional.ofNullable(this.validatedBy);
  }

  public Customer setId(Long id) {
    this.id = id;
    return this;
  }

  public Customer setName(String name) {
    this.name = name;
    return this;
  }

  public Customer setEmail(String email) {
    this.email = email;
    return this;
  }

  public Customer setShippingAddress(Address shippingAddress) {
    this.shippingAddress = shippingAddress;
    return this;
  }

  public Customer setCountry(Country country) {
    this.country = country;
    return this;
  }

  public Customer setCreatedDate(LocalDate createdDate) {
    this.createdDate = createdDate;
    return this;
  }

  public Customer setCreatedByUsername(String createdByUsername) {
    this.createdByUsername = createdByUsername;
    return this;
  }

  public Customer setGoldMember(boolean goldMember) {
    this.goldMember = goldMember;
    return this;
  }

  public Customer setGoldMemberRemovalReason(String goldMemberRemovalReason) {
    this.goldMemberRemovalReason = goldMemberRemovalReason;
    return this;
  }

  public Customer setLegalEntityCode(String legalEntityCode) {
    this.legalEntityCode = legalEntityCode;
    return this;
  }

  public Customer setDiscountedVat(boolean discountedVat) {
    this.discountedVat = discountedVat;
    return this;
  }




  public String toString() {
    return "Customer(id=" + this.getId() + ", name=" + this.getName() + ", email=" + this.getEmail() + ", shippingAddress=" + this.getShippingAddress() + ", country=" + this.getCountry() + ", createdDate=" + this.getCreatedDate() + ", createdByUsername=" + this.getCreatedByUsername() + ", goldMember=" + this.isGoldMember() + ", goldMemberRemovalReason=" + this.getGoldMemberRemovalReason() + ", legalEntityCode=" + this.getLegalEntityCode() + ", discountedVat=" + this.isDiscountedVat() + ", status=" + this.getStatus() + ", validatedBy=" + this.getValidatedBy() + ")";
  }

  public enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }

  private Status status = Status.DRAFT;
  private String validatedBy; // ⚠ Always not-null when status = VALIDATED or later: HDD = Hope-Driven Development

  public void validate(String user) {
    // small constrainted mutation allows to reason about the state of the object in isolation.
    // (burn the setters)
    if (status != Status.DRAFT) {
      throw new IllegalStateException("Cannot validate a non-draft customer");
    }
    status = Status.VALIDATED;
    validatedBy = Objects.requireNonNull(user);
  }
  public void activate() {
    if (status != Status.VALIDATED) {
      throw new IllegalStateException("Cannot activate a non-validated customer");
    }
    status = Status.ACTIVE;
  }
  public void delete() {
    if (status == Status.DELETED) {
      throw new IllegalStateException("Cannot delete a deleted customer");
    }
    status = Status.DELETED;
  }

}

//region Code in the project might [not] follow the rule
class CodeFollowingTheRule {
  public void ok(Customer draftCustomer) {
    draftCustomer.validate("currentUser"); // from token/session
  }
}
class CodeBreakingTheRule {
  public void farAway(Customer draftCustomer) {
//    draftCustomer.setStatus(Customer.Status.VALIDATED);
    draftCustomer.validate("currentUser"); // from token/session
  }
}
//endregion