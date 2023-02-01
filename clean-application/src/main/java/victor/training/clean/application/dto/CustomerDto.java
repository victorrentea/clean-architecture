package victor.training.clean.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
@AllArgsConstructor
public class CustomerDto { // Dto used to both QUERY and COMMAND use-cases ?
   Long id;
   @Schema(description = "Name of the customer")
   String name;
   String email;
   Long siteId;
   String creationDateStr;
   boolean gold;
   String goldMemberRemovalReason;
}
