package victor.training.clean.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import victor.training.clean.customer.domain.model.Customer;

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
   public int customerId
           ;
   public int cuponId;

   public CustomerDto(Customer customer) {
      this(customer.getId(), customer.getName(), customer.getEmail(), customer.getSite().getId(), customer.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
   }
}
