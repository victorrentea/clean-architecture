package victor.training.clean.domain.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.Objects;

  class CompanyShippingAddress { // nu 5 ??
  // - pt ca e posibil sa devina f diferite cu timp
  // - lizibilitate (sa nu alerg prin 3 fisiere dupa campuri)
  // - vreau sa cuplez intre ele Customer cu Company ?
  private String city;
  private String street;
  private Integer zipCode;
  private String inPlus;

}
//class CustomerShippingAddress  extends ShippingAddress{ // da extends 8
//  // - daca vreodata vreau sa procesez polimorfic ShippingAddress. ie. Daca nicaieri in logica mea nu refer ShippingAddress
//  private String etaj;
//
//  CustomerShippingAddress() {
//    super(city, street, zipCode);
//  }
//  // functie(ShippingAddress) < e un motiv sa am extends sau implements o interfata cu getteri
//  //   interfetele-s mai bune decat extends
//}


// acum in modelul meu am doar Customer,
// dar de fapt el poate reprezenta ProspectCustomer(3 required) sau ActiveCustomer(10 camp required)
// DDD intreaba: "ce inseamna Customer?" -> PANICA "depinde de campul boolean active"
// e complex si unul si altul? DA
// ==> daca ambele zone (marketign si fulfillment) sunt complexe => 2 Bounded Contexte diferite
// Bounded Context = "zona in care se aplica un termen/model"
// => 2 entitati persistate separat

//

class FullName {
  private final String first;
  private final String middle;
  private final String last;

  public FullName(String first, String last) {
    this(first, null, last);
  }

  public FullName(String first, String middle, String last) {
    if (first == null && last == null) {
      throw new IllegalArgumentException();
    }
    this.first = first;
    this.middle = middle;
    this.last = last;
  }

  public FullName withMiddle(String middle) { // "with"eri
    return new FullName(first, middle, last);
  }

  public String getFirst() {
    return first;
  }

  public String getLast() {
    return last;
  }

  public String getMiddle() {
    return middle;
  }
}

//class CustomerId {
//  private final long id;
//
//  CustomerId(long id) {
//    this.id = id;
//  }
//
//  public long getId() {
//    return id;
//  }
//}
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
  //  private CustomerId id;
  private String name;
  private String email;

//  boolean active;
//  private String camp;
//  private String camp;
//  private String camp;
//  private String camp;
//  private String camp;
//  private String camp;
//  private String camp;
//  private String camp;


  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
//  private String shippingAddressCity;
//  private String shippingAddressStreet;
//  private Integer shippingAddressZipCode;
  private ShippingAddress shippingAddress; // scrise in acelasi tabel SQL, "persistenta e un detaliu" - Uncle Bob
  @ManyToOne
  private Country country;

  private LocalDate creationDate;
//  private Status status;
  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

  // OOP: keep behavior next to state
  public int getDiscountPercentage() { // MULT MAI BINE DECAT UN CustomerUtil
    // Pui logica in Domain Model daca:
    // - DA pt ca asa promovezi reuseul : e mai usor de gasit logica aici decat intr-un CustomerUtil
       // - (NU pt ca cupleaza logica de existenta unei instante de Customer)
    // - NU implica alte dependinte (repo/api client/ alt Service)
    // - NU daca nu e logica de business ci de prezentare/infrastructura (nu te cuplezi la UI sau ale API-uri pe care le chemi)
    // - NU daca-s >10-20 linii -> mai bine intr-un Service. ca dupa o sa vrei s-o mockui. NICIODATA NU MOCKUIESTI STRUCTURI DE DATE!!
    //

    int discountPercentage = 1;
    if (goldMember) {
      discountPercentage += 3;
    }
    return discountPercentage;
  }

//  public void method() {
//    return String.format("%s %d %d %.4f") // doamne fereste - e presentation logica
//  }
}
