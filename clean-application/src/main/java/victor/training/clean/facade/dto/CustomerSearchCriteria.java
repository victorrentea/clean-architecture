package victor.training.clean.facade.dto;

import lombok.Value;

// received as JSON from search form in Frontend
@Value
public class CustomerSearchCriteria {
   String name;
}
