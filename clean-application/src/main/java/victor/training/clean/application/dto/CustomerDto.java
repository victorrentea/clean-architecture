package victor.training.clean.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import victor.training.clean.domain.model.Country;
import victor.training.clean.domain.model.Customer;

import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ofPattern;

@Builder
@Value
@AllArgsConstructor
public class CustomerDto { // Dto used to both QUERY and COMMAND use-cases ?
  Long id; // GET (from sequence in DB)

  @NotNull
  @Size(min = 5)
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

  public CustomerDto(Customer customer) {
    id = customer.getId();
    name = customer.getName();
    email = customer.getEmail();
    countryId = customer.getCountry().getId();
    createdDateStr = customer.getCreatedDate().format(ofPattern("yyyy-MM-dd"));
    gold = customer.isGoldMember();
    goldMemberRemovalReason = customer.getGoldMemberRemovalReason();
    legalEntityCode = customer.getLegalEntityCode();
    discountedVat = customer.isDiscountedVat();
    shippingAddressStreet = customer.getShippingAddress().getStreet();
    shippingAddressCity = customer.getShippingAddress().getCity();
    shippingAddressZipCode =  customer.getShippingAddress().getZipCode();
    discountPercentage =  customer.getDiscountPercentage();
  }

  public String getServus() {
    return "Salut";
  }

  public Customer toEntity() {
    Customer customer = new Customer(getName());
    customer.setEmail(getEmail());
    customer.setCreatedDate(LocalDate.now());
    customer.setCountry(new Country().setId(getCountryId()));
    customer.setLegalEntityCode(getLegalEntityCode());
    return customer;
  }
}
