package victor.training.clean.application.dto;

import lombok.Value;

// sent as JSON
@Value
public class CustomerSearchResult {
   long id;
   String name;
   boolean gold;
}
