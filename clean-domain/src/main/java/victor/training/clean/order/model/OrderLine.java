package victor.training.clean.order.model;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;

@Entity
//@Embeddable
public class OrderLine {
   @Id
   @GeneratedValue
   private Long id; // no getter!

   @Embedded
   private SupplierId supplierId;
   private String productId; // in the DB while you are on the same DB server, recommended to keep the FK between OrderLine -> Product
   private BigDecimal itemQuantity;

   private BigDecimal itemPrice;

   protected OrderLine() {} // hibernate

   public OrderLine(String productId, BigDecimal itemPrice, BigDecimal itemQuantity) {
      this.productId = requireNonNull(productId);
      this.itemPrice = requireNonNull(itemPrice);
      this.itemQuantity = requireNonNull(itemQuantity);
   }

   public OrderLine supplierId(SupplierId supplierId) {
      this.supplierId = supplierId;
      return this;
   }

   public BigDecimal computePrice() {
      return itemPrice.multiply(itemQuantity);
   }

   public String productId() {
      return productId;
   }

   public BigDecimal itemPrice() {
      return itemPrice;
   }

   public BigDecimal itemQuantity() {
      return itemQuantity;
   }

   public SupplierId supplierId() {
      return supplierId;
   }

}
