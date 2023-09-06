package victor.training.clean.application.dto;

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
public class CreateCustomerRequest { // Dto used to both QUERY and COMMAND use-cases ?
  String name; // *
  String email; // *
  Long countryId; // *
  String legalEntityCode; // *

  public Customer toEntity() {
    Customer customer = new Customer();
    customer.setEmail(email);
    customer.setName(name);
    customer.setCreatedDate(LocalDate.now());
    customer.setCountry(new Country().setId(countryId));
    customer.setLegalEntityCode(legalEntityCode);
    return customer;
  }
}
