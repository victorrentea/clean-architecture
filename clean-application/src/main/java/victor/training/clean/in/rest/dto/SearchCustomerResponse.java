package victor.training.clean.in.rest.dto;

import lombok.Value;

// the model of a row in the search results grid displayed in UI
@Value
public class SearchCustomerResponse {
   long id;
   String name;
}
