package victor.training.clean.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import victor.training.clean.domain.model.Country;
import victor.training.clean.domain.model.Customer;

import javax.validation.constraints.Size;
import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ofPattern;

@Builder
@Value
@AllArgsConstructor
public class CustomerDto { // Dto used to both QUERY and COMMAND use-cases ?
  Long id; // GET (from sequence in DB)

  @Size(min = 5) // payload validation
  String name; // *

  String email; // *

  Long countryId; // *

  String shippingAddressCity; // GET (updated via dedicated endpoint)
  String shippingAddressStreet; // GET (updated via dedicated endpoint)
  Integer shippingAddressZipCode; // GET (updated via dedicated endpoint)

  String creationDateStr; // GET (server-side assigned)

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
    creationDateStr = customer.getCreationDate().format(ofPattern("yyyy-MM-dd"));
    gold = customer.isGoldMember();
    goldMemberRemovalReason = customer.getGoldMemberRemovalReason();
    legalEntityCode = customer.getLegalEntityCode();
    discountedVat = customer.isDiscountedVat();
    discountPercentage = customer.getDiscountPercentage();
    shippingAddressCity = customer.getShippingAddress().getCity();
    shippingAddressStreet = customer.getShippingAddress().getStreet();
    shippingAddressZipCode = customer.getShippingAddress().getZipCode();
  }

  public Customer toEntity() {
      Customer customer = new Customer();
      customer.setEmail(getEmail());
      customer.setName(getName());
      customer.setCreationDate(LocalDate.now());
      customer.setCountry(new Country().setId(getCountryId()));
      customer.setLegalEntityCode(getLegalEntityCode());
      return customer;
  }
}
