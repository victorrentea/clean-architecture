package victor.training.clean.facade.dto;

import lombok.Data;
import victor.training.clean.domain.model.Customer;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.format.DateTimeFormatter;

@Data
public class CustomerDto {
   public Long id;
//   @ValidCustomerName
   @NotNull
   @Size(min = 5)
   public String name;
    @Email
   public String email;
   public Long siteId;
   public String creationDateStr;

   public CustomerDto(Customer customer) {
     id = customer.getId();
     name = customer.getName();
     email = customer.getEmail();
     creationDateStr = customer.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
     siteId = customer.getSite().getId();
   }

}
