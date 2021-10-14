package victor.training.clean.facade.dto;

import victor.training.clean.entity.Customer;

import java.text.SimpleDateFormat;

public class CustomerDto {
   public Long id;
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
      Customer entity = new Customer();
      entity.setId(id);
      entity.setName(name);
      //
      return entity;
   }

   public CustomerDto(String name, String email) {
      this.name = name;
      this.email = email;
   }

}
