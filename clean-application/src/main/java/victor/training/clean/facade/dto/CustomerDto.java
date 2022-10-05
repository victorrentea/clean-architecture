package victor.training.clean.facade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import victor.training.clean.domain.customer.model.Customer;

import javax.validation.constraints.Size;
import java.time.format.DateTimeFormatter;

@Builder
@Value
@AllArgsConstructor
public class CustomerDto {
   Long id;
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
              customer.getSite().getId(), customer.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
   }


   // DO THE DTOS YOU RETURN TO YOUR CLIENTS HAVE SETTERS ?

}
