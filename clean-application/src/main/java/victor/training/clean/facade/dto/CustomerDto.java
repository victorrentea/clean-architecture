package victor.training.clean.facade.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import victor.training.clean.domain.model.Customer;

import java.time.format.DateTimeFormatter;

@Builder
@Value
public class CustomerDto {
   Long id;
   @Schema(description = "Name of the customer")
   String name;
   String email;
   Long siteId;
   String creationDateStr;

   public static CustomerDto fromEntity(Customer customer) {
      return CustomerDto.builder()
              .id(customer.getId())
              .name(customer.getName())
              .email(customer.getEmail())
              .siteId(customer.getSite().getId())
              .creationDateStr(customer.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
              .build();
   }
}
