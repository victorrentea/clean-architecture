package victor.training.clean.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import victor.training.clean.domain.model.Customer;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.format.DateTimeFormatter;

@Builder
@Value
@AllArgsConstructor
public class CustomerDto {
   Long id;
   @Schema(description = "Name of the customer") // Open API documentation
   @Size(min = 4)
  @NotBlank
   String name;
   @Email
   String email;
   @NotNull
   Long siteId;
   String creationDateStr;
   public CustomerDto(Customer customer) {
      id = customer.getId();
      name = customer.getName();
      email = customer.getEmail();
      siteId = customer.getSite().getId();
      creationDateStr = customer.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
   }
}
