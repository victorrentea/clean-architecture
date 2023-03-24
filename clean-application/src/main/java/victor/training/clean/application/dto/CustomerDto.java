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

// POST /customer
class CreateCustomerRequest {}

// GET /customer/13
class GetCustomerResponse {}

// PUT /customer/13/details
class UpdateCustomerDetailsRequest {}

// PUT /customer/13/deactivate
class DeactivateCustomerRequest {}

// PUT /customer/13/activate

@Builder
@Value
@AllArgsConstructor
public class CustomerDto { // TODO kill this
  // Dto used to both QUERY response and COMMAND param use-cases ?
  Long id; // PUT, GET
  @Schema(description = "Name of the customer")
  @Size(min = 5, message = "{customer-name-too-short}")
  @NotNull
  String name;//GET PUT POST
  @Email
  String email;//GET PUT POST
  Long siteId;//GET PUT POST
  String creationDateStr;//GET
  boolean gold;//GET PUT POST
  String goldMemberRemovalReason; //GET PUT(gold: true->false)


  // This rocks 🤘unless: swagger-generated classe or -client.jar
  public CustomerDto(Customer customer) {
    id = customer.getId();
    name = customer.getName();
    email = customer.getEmail();
    siteId = customer.getSite().getId();
    creationDateStr = customer.getCreationDate().format(ofPattern("yyyy-MM-dd"));
    gold = customer.isGoldMember();
    goldMemberRemovalReason = "TODO";
  }

  public Customer toEntity() {
    Customer customer = new Customer(getName());
    customer.setEmail(getEmail());
    customer.setCreationDate(LocalDate.now());
    customer.setSite(new Site().setId(getSiteId()));
    return customer;
  }
}
