package victor.training.clean.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Site;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ofPattern;

@Builder
@Value
@AllArgsConstructor
public class CustomerDto { // Dto used to both QUERY and COMMAND use-cases ?
  Long id; // NU la POST
  @Schema(description = "Name of the customer")
  @Size(min = 5, message = "{customer-name-too-short}")
  String name;
  @Email
  @NotNull
  String email;
  Long siteId;
  String creationDateStr; // NU la POST
  boolean gold; // NU la POST
  String goldMemberRemovalReason; // NU la POST

  public CustomerDto(Customer customer) { // de ce nu!?
    id = customer.getId();
    name = customer.getName();
    email = customer.getEmail();
    siteId = customer.getSite().getId();
    creationDateStr = customer.getCreationDate().format(ofPattern("yyyy-MM-dd"));
    gold = customer.isGoldMember();
    goldMemberRemovalReason = "TODO";
  }

  public Customer toEntity() {
    Customer customer = new Customer(name);
    customer.setEmail(email);
    customer.setCreationDate(LocalDate.now());
    customer.setSite(new Site().setId(siteId));
    return customer;
  }
}
