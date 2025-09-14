package victor.training.clean.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

// the model of a row in the search results grid displayed in UI
@Schema(example = """
    {"a"
    """)
public record CustomerSearchResult(long id, String name) {
}
