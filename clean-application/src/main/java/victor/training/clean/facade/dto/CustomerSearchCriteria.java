package victor.training.clean.facade.dto;

import lombok.Value;

// received as JSON from search form in Frontend
@Value // see lombok.config for details how Jackson knows to unmarshall this
public class CustomerSearchCriteria {
   String name;
}
