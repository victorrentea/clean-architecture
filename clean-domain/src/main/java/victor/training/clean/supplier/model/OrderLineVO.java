package victor.training.clean.supplier.model;

import java.math.BigDecimal;
import java.util.Objects;

public class OrderLineVO {
   private final BigDecimal itemQuantity;
   private final String supplierId;

   public OrderLineVO(BigDecimal itemQuantity, String supplierId) {
      this.itemQuantity = Objects.requireNonNull(itemQuantity);
      // TODO quantity positive/
      this.supplierId = Objects.requireNonNull(supplierId);
   }

   public BigDecimal itemQuantity() {
      return itemQuantity;
   }

   public String supplierId() {
      return supplierId;
   }
}
