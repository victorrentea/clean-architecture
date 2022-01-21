package victor.training.clean.facade.dto;

import lombok.Data;

@Data

public class CustomerDto {
   public Long id;
   public String name;
   // @Email
   public String email;
   public Long siteId;
   public String creationDateStr;

   public CustomerDto() {
   }

}
