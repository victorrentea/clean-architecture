package victor.training.clean.facade.dto;

import victor.training.clean.entity.Customer;

import java.text.SimpleDateFormat;

public class CustomerDto {
   public Long id;
   public String name;
   public String email;
   public Long siteId;
   public String creationDateStr;

   public CustomerDto(Customer entity) { // mesaj care iese din sist tau
      name = entity.getName();
      email = entity.getEmail();
      creationDateStr = new SimpleDateFormat("yyyy-MM-dd").format(entity.getCreationDate());
      id = entity.getId();
   }

   public CustomerDto(String name, String email) {
      this.name = name;
      this.email = email;
   }

   public Customer toEntity() {
      Customer customer = new Customer();
      customer.setEmail(email);
      customer.setName(name);
      return customer;
   }
}
