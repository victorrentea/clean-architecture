package victor.training.clean.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
@AllArgsConstructor
public class CustomerDto { // is this used to both QUERY and COMMAND use-cases ?
   Long id;
   @Schema(description = "Name of the customer") // Open API doc
   String name;
//   @VisibleFor("user-scope1")
   String emailAddress;
//   @VisibleFor("user-scope1") + @Aspect pus pe metode @within(RestController)
   Long siteId;
   String creationDateStr;
   boolean gold;
   String goldMemberRemovalReason;
}
