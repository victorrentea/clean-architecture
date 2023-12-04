package victor.training.clean.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

// received as JSON from a search screen in UI
@Builder // for tests
public record SearchCustomerCriteria(
   @Schema(description = "Part of the names, case-insensitive")
   String name,
   String email,
   Long countryId
) {
}
