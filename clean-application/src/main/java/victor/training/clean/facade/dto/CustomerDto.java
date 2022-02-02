package victor.training.clean.facade.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import victor.training.clean.entity.Customer;

import java.text.SimpleDateFormat;

@Data
public class CustomerDto {
   public Long id;
   @Length(min = 5)
   public String name;
   // @Email
   public String email;
   public Long siteId;
   public String creationDateStr;

   public CustomerDto() {
   }

   public CustomerDto(Customer customer) {
      name = customer.getName();
      email = customer.getEmail();
      creationDateStr = new SimpleDateFormat("yyyy-MM-dd").format(customer.getCreationDate());
      id = customer.getId();
   }
}
