package victor.training.clean.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.NONE;

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
  @NotNull
  @Size(min = 5)
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


  protected Customer() {} // for Hibernate only

  public Customer(String name) {
    this.name = requireNonNull(name);
  }

  // + mai usor de gasit logica
  // + mai simplu de implem, natural sa ai operatii pe Obiecte
  // - "Asta nu se face" - colegii, ca-l considera un POJO (o structura "anemica" de date)
    //     multi arhitecti/sr/batrani cu PTSD au prins JSP/Swing (UI facut cu Java) si mereu poluai sf. model cu persentation shit
  //      M [VC] -- au plecat n JS/TS
  // - NU pui logica prea grea(>10 linii) aici, ca altfel ajungi sa ai nevoie sa o @Mock -NU SE FACE
  // - NU daca logica are nevoie de dependinte (eg Repo/Api)
  // - NU daca are nevoie de structuri huge de date parametrii, de cuplare. eg ClientPortoflio45DeCampuri
  // - NU daca nu e domain logic  public void toXml() { return "<tag>"+name+"</tag>" }
  // ? state changes
  public int getDiscountPercentage() { // OOP keep behavior next to state
    int discountPercentage = 1;
    if (goldMember) {
      discountPercentage += 3;
    }
    return discountPercentage;
  }

//  public victor.training.clean.application.dto.CustomerDto toDto() {
  //  ar cupla sf. entitatea de Domain la ceva marunt, un DTO de-al meu
//  }


  enum Status {
    DRAFT, VALIDATED, ACTIVE, DELETED
  }
  @Setter(NONE)
  private Status status;
  @Setter(NONE)
  private String validatedByUser; // nenull pt orice customer cu state > VALDIATED

  public void setStatus(Status status) {
    this.status = status;
  }

  public void validate(String user) {
    if (status != Status.DRAFT) {
      throw new IllegalStateException();
    }
    status = Status.VALIDATED;
    validatedByUser = user;
  }
  public void activate() {
    if (status != Status.VALIDATED) {
      throw new IllegalStateException();
    }
    status = Status.ACTIVE;
  }
}

// draftCustomer.setStatus(ACTIVE) // illegal transition

// daca la DRAFT ai 3 campuri mandatori
// daca la VALIDATED ai inca 4 mandatory + link la prima/copiate primele 3 campuri/@Embedded
/// sa nici nu poti crea
//  new ValidatedCustomer(draftCustomer, camp1,camp2,camp3, camp4) decat daca ai cele 4 campruri in plus si un DraftCustomer)