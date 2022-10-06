package victor.training.clean.facade.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CustomerDto {
   Long id;
   String name;
   String email;
   Long siteId;
   String creationDateStr;

}
