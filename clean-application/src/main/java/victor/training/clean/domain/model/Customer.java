package victor.training.clean.domain.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.NONE;

@Entity
@Data // TODO remove:
// Avoid lombok @Entity on ORM Domain @Entity
// 1) hashCode on @Id [ORM]
// 2) toString lazy-loading collections [ORM]
// 3) setters for everything = lack of encapsulation
public class Customer {
  @Id
  @GeneratedValue
  private Long id;
  @NotNull // auto-validated by hibernate at INSERT/UPDATE (repo.save, auto-flush changes) + on DTOs also
  private String name;
  private String email;

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
  @Embedded
  private ShippingAddress shippingAddress; // NO CHANGE TO DB schema was caused.
//  private String shippingAddressCity;
//  private String shippingAddressStreet;
//  private Integer shippingAddressZipCode;
  @ManyToOne
  private Country country;

  @Setter(NONE)
  private LocalDate activateDate;
  @Setter(NONE)
  private String activateByUsername;

  // mutator guarding the Biz rule: when activated, a Customer has date and author.
  public void activate(String user) {
    activateDate = LocalDate.now();
    activateByUsername = user;
  }

  private LocalDate createdDate;
  private String createdByUsername;
  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

  public int getDiscountPercentage() {
    int discountPercentage = 1;
    if (goldMember) {
      discountPercentage += 3;
    }
    return discountPercentage;
  }
}
