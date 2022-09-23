package victor.training.clean.facade.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import javax.validation.constraints.Size;

@Builder
@Value
public class CustomerDto {
   Long id;
   String name;
   // @Email
   String email;
   Long siteId;
   String creationDateStr;

}
