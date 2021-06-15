package victor.training.clean.order.facade.dto;

import victor.training.clean.order.model.OrderLine;
import victor.training.clean.order.model.SupplierId;

import java.math.BigDecimal;

public class OrderLineDto {
   public String supplierId;
   public String productId;
   public BigDecimal itemPrice;
   public BigDecimal itemQuantity;

   public OrderLineDto() {}
   public OrderLineDto(OrderLine entity) {
      supplierId = entity.supplierId().value;
      productId = entity.productId();
      itemPrice = entity.itemPrice();
      itemQuantity = entity.itemQuantity();
   }
   public OrderLine toEntity() {
      OrderLine orderLine = new OrderLine(productId, itemPrice, itemQuantity);
      orderLine.supplierId(new SupplierId(supplierId));
      return orderLine;
   }
}
