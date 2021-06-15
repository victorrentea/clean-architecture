package victor.training.clean.order.adapter.dto;

import java.math.BigDecimal;

public class ExternalProductDto { // matching your other Microservice API
   // NEXT level: generate this from swagger.yaml
   public String id;
   public String name;

   public BigDecimal priceForRomania;
   public BigDecimal priceForItaly;
   public BigDecimal priceForSerbia;

   public String productGroup; // not mapped in my Bounded Conext

}
