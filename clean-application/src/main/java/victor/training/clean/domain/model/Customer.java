package victor.training.clean.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

import static java.util.Objects.requireNonNull;

//(imuutable❤️)
//record in Hib 6.2
@Embeddable
@Getter
class ShippingAddress {
  private String city;
  private String street;
  private Integer zipCode;

  protected ShippingAddress() {} // for Hibernate only

  public ShippingAddress(String city, String street, Integer zipCode) {
    this.city = city;
    this.street = street;
    this.zipCode = zipCode;
  }
}

@Entity
@Data // BAD because:
// 1) hashCode on @Id,
// 2) toString can trigger lazy-loading,
// 3) all setters = no encapsulation, de fapt programezi can C++ cu struct
// instead: use @Getter on class + @Setter pe unele campuri
public class Customer {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String email;

  // 🤔 Hmm... 3 fields with the same prefix. What TODO ?
//  private String shippingAddressCity;
//  private String shippingAddressStreet;
//  private Integer shippingAddressZipCode;
  @Embedded // cele 3 coloane RAMAN in tabela CUSTOMER
  private ShippingAddress shippingAddress;

  @ManyToOne
  private Country country;

  private LocalDate createdDate;
  private String createdByUsername;
  private boolean goldMember;
  private String goldMemberRemovalReason;

  private String legalEntityCode;
  private boolean discountedVat;

}
