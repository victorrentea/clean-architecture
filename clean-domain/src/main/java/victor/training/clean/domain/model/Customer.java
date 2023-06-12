package victor.training.clean.domain.model;

import lombok.Data;

import javax.persistence.*;
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
  private String name;
  private String email;

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
  @Embedded
  private ShippingAddress shippingAddress = new ShippingAddress();

  @ManyToOne
  private Country country;

  private LocalDate creationDate;
  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

  // 5-10 linii de logica de biz (nu de formatari/parsari)
  // care depinde DOAR de starea entitatii asteia
  public int getDiscountPercentage() {
    int discountPercentage = 1;
    if (goldMember) {
      discountPercentage += 3;
    }
    return discountPercentage;
  }




//  enum Status {
//    DRAFT,ACTIVE,DELETED
//  }
//  @Setter(NONE)
//  private Status status = Status.DRAFT;
//  @Setter(NONE)
//  private String deletedBy; // userur
//  public void delete(String user) {
//    if (status != Status.ACTIVE) {
//      throw new IllegalStateException();
//    }
//    status = Status.DELETED;
//    deletedBy = requireNonNull(user);
//  }
}
