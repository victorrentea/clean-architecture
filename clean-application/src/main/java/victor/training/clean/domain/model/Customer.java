package victor.training.clean.domain.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

import static java.util.Objects.requireNonNull;

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
  @Size(min = 5)
  @NotNull
  private String name;
  private String email;

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
  private String shippingAddressCity;
  private String shippingAddressStreet;
  private Integer shippingAddressZipCode;
  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;
  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

  public int getDiscountPercentage() {
    int discountPercentage = 1;
    if (isGoldMember()) {
      discountPercentage += 3;
    }
    return discountPercentage;
  }
}
