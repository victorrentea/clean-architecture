package victor.training.clean.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import victor.training.clean.domain.model.Customer;

import javax.validation.constraints.Size;

import static java.time.format.DateTimeFormatter.ofPattern;

@Builder
@Value
@AllArgsConstructor
public class RegisterCustomerRequest { // Dto used to both QUERY and COMMAND use-cases ?
  @Size(min = 5)
  String name; // *
  String email; // *
  Long countryId; // *
  String legalEntityCode; // *

  // THE best solution DACA SI NU MAI DACA modelul tau de API il scrii de mana.
  public RegisterCustomerRequest(Customer customer) {
    name = customer.getName();
    email = customer.getEmail();
    countryId = customer.getCountry().getId();
    legalEntityCode = customer.getLegalEntityCode();
  }
}
