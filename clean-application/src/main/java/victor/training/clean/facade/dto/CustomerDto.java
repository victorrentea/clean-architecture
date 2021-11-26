package victor.training.clean.facade.dto;

import lombok.Data;
import victor.training.clean.customer.entity.Customer;

import java.text.SimpleDateFormat;

@Data
public class CustomerDto {
   public Long id;
   public String name;
   // @Email
   public String email;
   public Long siteId;
   public String creationDateStr;

   public CustomerDto() {} // pt jackson/jaxb

   public CustomerDto(Customer customer) {
      name = customer.getName();
      email = customer.getEmail();
      creationDateStr = new SimpleDateFormat("yyyy-MM-dd").format(customer.getCreationDate());
      id = customer.getId();
   }

   public Customer toEntity() {
      Customer customer = new Customer();
      customer.setEmail(email);
      customer.setName(name);
      customer.setSiteId(siteId);
      return customer;
   }
}
