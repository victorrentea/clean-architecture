package victor.training.clean.facade.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CustomerDto {
   public Long id;
   @NotNull
   public String name;
   // @Email
   public String email;
   public Long siteId;
   public String creationDateStr;

   public CustomerDto() {
   }

}
