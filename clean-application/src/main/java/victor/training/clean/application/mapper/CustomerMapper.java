package victor.training.clean.application.mapper;

import victor.training.clean.application.dto.RegisterCustomerRequest;
import victor.training.clean.domain.model.Country;
import victor.training.clean.domain.model.Customer;

import java.time.LocalDate;

public class CustomerMapper {
  public static Customer asEntity(RegisterCustomerRequest dto) {
    Customer customer = new Customer();
    customer.setEmail(dto.getEmail());
    customer.setName(dto.getName());
    customer.setCreationDate(LocalDate.now());
    customer.setCountry(new Country().setId(dto.getCountryId()));
    customer.setLegalEntityCode(dto.getLegalEntityCode());
    return customer;
  }
}
