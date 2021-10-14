package victor.training.clean.facade.dto;

import victor.training.clean.entity.Customer;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
public class CustomerDto {
   public Long id;
   @NotNull
   @Size(min = 5)
   public String name;
   public String email;
   public Long siteId;
   public String creationDateStr;

   public CustomerDto() {}
   public CustomerDto(Customer entity) {
      name = entity.getName();
      email = entity.getEmail();
      creationDateStr = new SimpleDateFormat("yyyy-MM-dd").format(entity.getCreationDate());
      id = entity.getId();
   }

   public Customer toEntity() {
      Customer customer = new Customer();
      customer.setEmail(email);
      customer.setName(name);
      return customer;
   }

   public CustomerDto(String name, String email) {
      this.name = name;
      this.email = email;
   }

}
