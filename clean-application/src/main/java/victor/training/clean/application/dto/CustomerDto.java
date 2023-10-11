package victor.training.clean.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import victor.training.clean.domain.model.Country;
import victor.training.clean.domain.model.Customer;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ofPattern;

@Builder
@Value
@AllArgsConstructor
public class CustomerDto { // Dto used to both QUERY and COMMAND use-cases ?
  Long id; // GET (from sequence in DB)

  @Size(min = 5)
  @NotNull
  String name; // *

  String email; // *

  Long countryId; // *

  String shippingAddressCity; // GET (updated via dedicated endpoint)
  String shippingAddressStreet; // GET (updated via dedicated endpoint)
  Integer shippingAddressZipCode; // GET (updated via dedicated endpoint)

  String createdDateStr; // GET (server-side assigned)

  boolean gold; // GET & PUT
  String goldMemberRemovalReason; // GET & PUT if gold=true->false
  int discountPercentage; // GET (server-side computed)

  String legalEntityCode; // *
  boolean discountedVat; // GET (server-side fetched)

//  @AssertTrue
//  public boolean customKungFu() {
//    return a & b &c;
//  }

  public static CustomerDto from(Customer customer) {
   return  CustomerDto.builder()
        .id(customer.getId())
        .name(customer.getName())
        .email(customer.getEmail())
        .countryId(customer.getCountry().getId())
        .createdDateStr(customer.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        .gold(customer.isGoldMember())

        .shippingAddressStreet(customer.getShippingAddressStreet())
        .shippingAddressCity(customer.getShippingAddressCity())
        .shippingAddressZipCode(customer.getShippingAddressZipCode())

        .discountPercentage(customer.getDiscountPercentage())
        .goldMemberRemovalReason(customer.getGoldMemberRemovalReason())
        .legalEntityCode(customer.getLegalEntityCode())
        .discountedVat(customer.isDiscountedVat())
        .build();
  }

  public Customer toEntity() {
    Customer customer = new Customer();
    customer.setEmail(getEmail());
    customer.setName(getName());
    customer.setCreatedDate(LocalDate.now());
    customer.setCountry(new Country().setId(getCountryId()));
    customer.setLegalEntityCode(getLegalEntityCode());
    return customer;
  }

//  public CustomerDto(Customer customer) {
//    id = customer.getId();
//    name = customer.getName();
//    email = customer.getEmail();
//    countryId = customer.getCountry().getId();
//    creationDateStr = customer.getCreationDate().format(ofPattern("yyyy-MM-dd"));
//    gold = customer.isGoldMember();
//    goldMemberRemovalReason = customer.getGoldMemberRemovalReason();
//    legalEntityCode = customer.getLegalEntityCode();
//    discountedVat = customer.isDiscountedVat();
//    shippingAddressStreet = ?
//    shippingAddressCity = ?
//    shippingAddressZipCode = ?
//    discountPercentage = ?
//  }
}
