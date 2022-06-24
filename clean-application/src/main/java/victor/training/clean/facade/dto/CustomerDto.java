package victor.training.clean.facade.dto;

import lombok.Data;
import victor.training.clean.domain.customer.entity.Customer;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;

@Data
public class CustomerDto {
   public Long id;
   @Size(min = 5) // needs to @Valid on @RestController
   // BIG PRO: for APIs exposed to other applications
   public String name;
    @Email
   public String email;
   public Long siteId;
   public String creationDateStr;

   public CustomerDto(Customer customer) {
      name = customer.getName();
      email = customer.getEmail();
      creationDateStr = new SimpleDateFormat("yyyy-MM-dd").format(customer.getCreationDate());
      id = customer.getId();
   }

}
