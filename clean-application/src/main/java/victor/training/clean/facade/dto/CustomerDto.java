package victor.training.clean.facade.dto;

import org.hibernate.validator.constraints.Length;
import victor.training.clean.customer.entity.Customer;

import javax.validation.constraints.Email;
import java.text.SimpleDateFormat;

public class CustomerDto {
   public Long id;
   @Length(min = 3)
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

   public CustomerDto(String name, String email) {
      this.name = name;
      this.email = email;
   }

}
