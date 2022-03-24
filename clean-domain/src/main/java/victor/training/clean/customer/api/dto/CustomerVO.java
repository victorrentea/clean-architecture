package victor.training.clean.customer.api.dto;

import lombok.Data;

@Data
public class CustomerVO {
   private final Long id;
   private final String name;
   private final String email;
}
