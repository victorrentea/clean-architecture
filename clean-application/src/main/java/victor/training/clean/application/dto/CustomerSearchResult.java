package victor.training.clean.application.dto;

import lombok.Value;

// sent as JSON to a search results grid in UI
@Value
public class CustomerSearchResult {
   long id;
   String name;
}
