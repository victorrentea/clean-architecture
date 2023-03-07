package victor.training.clean.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import victor.training.clean.domain.model.Customer;

import javax.validation.constraints.Size;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ofPattern;

@Builder
@Value
@AllArgsConstructor
public class CustomerDto { // Dto used to both QUERY and COMMAND use-cases ?
  Long id;
  @Schema(description = "Name of the customer")
  @Size(min = 5, message = "{customer-name-too-short}")
  String name;
  String email;
  Long siteId;
  String creationDateStr;
  boolean gold;
  String goldMemberRemovalReason;

  public CustomerDto(Customer customer) {
    id = customer.getId();
    name = customer.getName();
    email = customer.getEmail();
    siteId = customer.getSite().getId();
    creationDateStr = customer.getCreationDate().format(ofPattern("yyyy-MM-dd"));
    gold = customer.isGoldMember();
    goldMemberRemovalReason = "TODO";
  }
}
