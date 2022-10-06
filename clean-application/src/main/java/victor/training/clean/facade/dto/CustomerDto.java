package victor.training.clean.facade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import victor.training.clean.domain.model.Customer;

import javax.validation.constraints.Size;
import java.time.format.DateTimeFormatter;

@Builder
@Value
@AllArgsConstructor
public class CustomerDto {
   Long id;
   String name;
   // @Email
   String email;
   Long siteId;
   String creationDateStr;
//   public CustomerDto(Customer customer) {
//      CustomerDto.builder()
//              .id(customer.getId())
//              .name(customer.getName())
//              .email(customer.getEmail())
//              .siteId(customer.getSite().getId())
//              .creationDateStr(customer.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
//              .build()
//   }

}
