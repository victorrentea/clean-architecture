package victor.training.clean.facade.dto;

import lombok.*;
import victor.training.clean.domain.entity.Customer;

import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;

@Value
@AllArgsConstructor
public class CustomerDto {
   Long id;
   @With
  @Size(min = 5)
   String name;
   // @Email
   String email;
   Long siteId;
   String creationDateStr;

   public CustomerDto(Customer customer) {
      this(customer.getId(),
          customer.getName(),
          customer.getEmail(),
          customer.getSite().getId(),
           new SimpleDateFormat("yyyy-MM-dd").format(customer.getCreationDate()));
   }
}
