package victor.training.clean.supplier.model;

import java.util.List;

public class OrderVO {
   private final String clientId;
   private final List<OrderLineVO> orderLines;

   public OrderVO(String clientId, List<OrderLineVO> orderLines) {
      this.clientId = clientId;
      this.orderLines = orderLines;
   }

   public List<OrderLineVO> orderLines() {
      return orderLines;
   }

   public String clientId() {
      return clientId;
   }
}
