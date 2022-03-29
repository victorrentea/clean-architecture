package victor.training.clean.facade.dto;

import lombok.Data;
import victor.training.clean.customer.entity.Customer;

import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;

@Data
public class CustomerDto {
   public Long id;
   @Size(min = 5)
   public String name;
   // @Email
   public String email;
   public Long siteId;
   public String creationDateStr;

   public CustomerDto() {}
   // possible only if the DTOs in your api are not generated from an (eg) OpenAPI/swagger/wsdl
   public CustomerDto(Customer customer) {
      name = customer.getName();
      email = customer.getEmail();
      creationDateStr = new SimpleDateFormat("yyyy-MM-dd").format(customer.getCreationDate());
      id = customer.getId();
   }

}
