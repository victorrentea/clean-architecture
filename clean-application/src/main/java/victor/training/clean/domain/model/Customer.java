package victor.training.clean.domain.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

//class CustomerShippingAddress { // e mai cohesive, mai putin refolosibil, dar apare coupling,
  //  si maine produsu vrea inca 3 attr da' doar pt customer shipping nu si pt Company.shippingAddress
//class Address {// prea vag
//@Value // Java
class ShippingAddress {// reusable : Customer.shippingAddress dar si Company.shippingAddress
  private final String city;
  private final String street;
  private final Integer zipCode;
// Value Object (design pattern)
  // - obiect fara identitate persistenta (continuity of change), spre deosebire de un Entity
    // - equals (hashCode) se implem pe toate campurile
  // - imutabil (readonly/final)
    // - de obicei mic

  ShippingAddress(String city, String street, Integer zipCode) {
    this.city = city;
    this.street = street;
    this.zipCode = zipCode;
  }

  public Integer getZipCode() {
    return zipCode;
  }

  public String getCity() {
    return city;
  }

  public String getStreet() {
    return street;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ShippingAddress that = (ShippingAddress) o;
    return Objects.equals(city, that.city) && Objects.equals(street, that.street) && Objects.equals(zipCode, that.zipCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(city, street, zipCode);
  }
}
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
  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

}
