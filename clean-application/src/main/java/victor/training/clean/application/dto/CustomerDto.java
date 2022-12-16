package victor.training.clean.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import victor.training.clean.domain.model.Customer;

import java.time.format.DateTimeFormatter;

@Builder
@Value
@AllArgsConstructor
public class CustomerDto {
  Long id;
  @Schema(description = "Name of the customer") // Open API documentation
  String name;
  String email;
  Long siteId;
  String creationDateStr;

  public CustomerDto(Customer customer) {

    // perfect ok ca mizeriile (dto) sa depinda de ceel sfinte (Domain)
    id = customer.getId();
    name = customer.getName();
    email = customer.getEmail();
    siteId = customer.getSite().getId();
    creationDateStr = customer.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
  }
}
