package victor.training.clean.application.dto;

import lombok.Value;

// the model of a row in the search results grid displayed in UI
@Value
public class CustomerSearchResult {
   long id;
   String name;
}
