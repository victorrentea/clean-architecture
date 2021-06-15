package victor.training.clean.supplier.model;

import lombok.Data;
import victor.training.clean.product.model.Product;

import java.math.BigDecimal;

@Data
public class ProductWithQuantity {
   private final String product;
   private final BigDecimal quantity;

   public ProductWithQuantity(String product, BigDecimal count) {
      this.product = product;
      this.quantity = count;
   }

   public String product() {
      return product;
   }

   public BigDecimal quantity() {
      return quantity;
   }
}
