package victor.training.clean.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

// received as JSON from a search screen in UI
@Builder // for tests
public record CustomerSearchCriteria(
   @Schema(description = "Part of the name, case-insensitive")
   String name,
   String email,
   Long country
) {
}
