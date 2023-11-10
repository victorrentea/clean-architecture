package victor.training.clean.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

// received as JSON from a search screen in UI
@Value // @see lombok.config that allows Jackson to unmarshall via constructor into this class
@Builder // used in tests
public class SearchCustomerCriteria {
   @Schema(description = "Part of the name, case-insensitive")
   String name;
   String email;
   Long countryId;
}
