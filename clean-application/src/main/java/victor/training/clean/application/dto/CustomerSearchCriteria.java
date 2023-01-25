package victor.training.clean.application.dto;

import lombok.Value;

// received as JSON from a search screen in Frontend
@Value // @see lombok.config that allows Jackson to unmarshall via constructor into this class
public class CustomerSearchCriteria {
   String name;
   String phone;
   Long siteId;
}
