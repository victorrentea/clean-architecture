package victor.training.clean.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

import static java.util.Objects.requireNonNull;

#[Embeddable]
class Address { // nu e TABELA, ci aceste campuri sunt stocate in tabela CUSTOMER
  private String shippingAddressCity;
  private String shippingAddressStreet;
  private Integer shippingAddressZipCode;
  public __construct(String city) { // tupeu, nu prea face lumea
    if (strlen(city)<3) throw new IllegalArgumentException();
    this.shippingAddressCity = city;
  }
}
@Entity
@Data // TODO remove:
public class Customer {
  @Id
  @GeneratedValue
  private Long id;
  // minim 3 caractere: "if"
  private String name; //NOT NULL
  @JsonIgnore
  private String email;
//  private String password; // in bcrypt de ajungi in presa
//
  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
//  #ManyToOne // NU FK address
  #[Embedded]
  private Address shippingAddress;
//  private String shippingAddressCity;
//  private String shippingAddressStreet;
//  private Integer shippingAddressZipCode;

  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;
  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

  private Status status = Status.DRAFT;
  private String deletionReason;

  enum Status {ACTIVE,DELETED,DRAFT}

  // cum te poti asigura ca MEREU cand status ajunge DELETED, ai deletionReason nenull
  // primu pas: OrderUtil/Helper <- lumea-l uita.
//  public void setStatus(Status status) { // de sters cu grije si toate
//  magazinele in Stormu cu colegu care are RAM destul
//    this.status = status;
//  }
  public void activate() {
    if (status != Status.DRAFT) { // STATE MACHINE
      throw new IllegalStateException();
    }
    status =Status.ACTIVE;
  }


//  public void setDeletionReason(String deletionReason) {
//    this.deletionReason = deletionReason;
//  }

  public void delete(String reason) { // incapsulezi CHANGEurile in
    // metode mutator care enforseaza reguli de biz ca sa stergi setteri
    // OOP permite sa judeci in izolare clasa asta. pe baza api public te prinzi ce stari poti avea sau nu;
    // + NOT NULL in DB cu grija = siguranta pe corectitudinea datelor.
    if (status != Status.ACTIVE) {
      throw new IllegalStateException();
    }
    status = Status.DELETED;
    deletionReason = reason;
  }

  public boolean isActive() {
    return status == Status.ACTIVE; // super-OK
  }

  public int getDiscountPercentage() { // ok 2-5-7 linii de logica simpla
    // ce opereaza DOAR pe campurile entitatii asteia.
    int discountPercentage = 1;
    if (this.goldMember) {
      discountPercentage += 3;
    }
    return discountPercentage;
  }

  public int getDiscountPercentageNOT(ConfiRepo repo) { // NOT ok
    int discountPercentage = 1;
    if (this.goldMember) {
      discountPercentage += repo.getExtraForGold();
    }
    return discountPercentage;
  }
  public int getDiscountPercentage(ConfigRepo repo) { // NOT ok cuplez la DB
    int discountPercentage = 1;
    if (this.goldMember) {
//      repo.update("Doamne fereste");
      discountPercentage += repo.getExtraForGold();
    }
    return discountPercentage;
  }
}
class Voucher {
  private int value;
  private Type type;

  public void apply(Cart cart20campuri) { // NOT OK. saracu voucher e coupled la un montru de structura
// coupling e rau pt ca:
    // 1) complexitate
    // 2) mai greu de testat
    // 3) change rippling
  }
}