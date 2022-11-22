package victor.training.clean.site.application.dto;

import lombok.Value;

// received as JSON (eg from search form in Frontend)
@Value // @see lombok.config that  allows Jackson to unmarshall this
public class CustomerSearchCriteria {
   String name;
   String phone;
   Long siteId;
}
