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
public class CustomerDto { // is this used to both QUERY and COMMAND use-cases ?
  Long id;
  @Schema(description = "Name of the customer") // Open API doc
  String name;
  //   @VisibleFor("user-scope1")
  String emailAddress;
  //   @VisibleFor("user-scope1") + @Aspect pus pe metode @within(RestController)
  Long siteId;
  String creationDateStr;
  boolean gold;
  String goldMemberRemovalReason;

  public CustomerDto(Customer customer) {
    id = customer.getId();
    name = customer.getName();
    emailAddress = customer.getEmail();
    siteId = customer.getSite().getId();
    creationDateStr = customer.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    gold=true;
    goldMemberRemovalReason = null;
  }

  public Customer toCustomer() {
    // nu e straniu ca un DTO sa nasca un Entity (o vedeta) ?
    Customer customer = new Customer(getName());
    customer.setEmail(getEmailAddress());
    customer.setCreationDate(LocalDate.now());
    customer.setSite(new Site().setId(siteId));
    return customer;
  }
  //   Customer customer; // NU
}
