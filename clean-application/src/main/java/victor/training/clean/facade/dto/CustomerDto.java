package victor.training.clean.facade.dto;

import org.hibernate.validator.constraints.Length;
import victor.training.clean.customer.entity.Customer;

import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;

public class CustomerDto {
   public Long id;
   @NotNull
   @Length(min=3)
   public String name;
   public String email;
   public Long siteId;
   public String creationDateStr;

   public CustomerDto() {} // jackson
   public CustomerDto(Customer entity) {
      name = entity.getName();
      email = entity.getEmail();
      creationDateStr = new SimpleDateFormat("yyyy-MM-dd").format(entity.getCreationDate());
      id = entity.getId();
   }

   public CustomerDto(String name, String email) {
      this.name = name;
      this.email = email;
   }

}
