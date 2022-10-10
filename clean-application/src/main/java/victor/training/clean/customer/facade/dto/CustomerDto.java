package victor.training.clean.customer.facade.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Size;

@Builder
@Value
public class CustomerDto {
   Long id;
   @Size(min = 5)
   String name;
   String email;
   Long siteId;
   String creationDateStr;

}
