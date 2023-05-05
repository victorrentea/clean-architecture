package victor.training.clean.domain.model;

import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
class ShippingAddress { // value object
  private String city;
  private String street;
  private Integer zipCode;
  protected ShippingAddress() {} // for Hibernate only
  public ShippingAddress(String city, String street, Integer zipCode) {
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
  @NotNull // #1)⭐️ javax.validation => la .save() hibernate arunca ex
//  @NonNull // #2) Lombok => in codul .class adauga un if(==null) throw in setterul generat, si in contructor
  					// mai eager la crapau, dar mai magic
  @Column(nullable = false) // sau NOT NULL pe coloana in tabel DB
  // #4) ca vreun UPDATE SQL in DB direct sa nu puna null ->  ca sa dormi mai bine noaptea
  private String name;  // nu poate niciodata sa fie null
  private String email;

  // 🤔 Hmm... 3 fields with the same prefix TODO ?
//  private String shippingAddressCity;
//  private String shippingAddressStreet;
//  private Integer shippingAddressZipCode;
  @Embedded
  private ShippingAddress shippingAddress;

  private LocalDate creationDate;
  private boolean goldMember;
  @JsonIgnore // poluarea modelului cu concernuri de API/presentation
  private String goldMemberRemovalReason;
//  @ElementCollection
//  private List<String> phones;


//  public String getCreationDateStr() { // presentation bullshit
//    return creationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//  }

  @ManyToOne
  private Site site;

  protected Customer() {} // for Hibernate only

	public Customer(String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		this.name = name;
	}

	public int getDiscountPercentage() {
    int discountPercentage = 3;
    if (goldMember) {
      discountPercentage += 1;
    }
    return discountPercentage;
  }
}
