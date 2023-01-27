package victor.training.clean.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Site;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Builder
@Value
@AllArgsConstructor
public class CustomerRegistrationRequest { // is this used to both QUERY and COMMAND use-cases ?
  String name; // *
  String emailAddress; // *
  Long siteId; // *

  public Customer toCustomer() {
    // nu e straniu ca un DTO sa nasca un Entity (o vedeta) ?
    Customer customer = new Customer(getName());
    customer.setEmail(getEmailAddress());
    customer.setCreationDate(LocalDate.now());
    customer.setSite(new Site().setId(siteId));
    return customer;
  }

}
