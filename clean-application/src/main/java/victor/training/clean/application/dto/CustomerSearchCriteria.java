package victor.training.clean.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

// received as JSON from a search screen in Frontend
@Value // @see lombok.config that allows Jackson to unmarshall via constructor into this class
@Builder // for tests
public class CustomerSearchCriteria {
   @Schema(description = "Part of the name, case-insensitive")
   String name;
   String email;
   Long siteId;
}
