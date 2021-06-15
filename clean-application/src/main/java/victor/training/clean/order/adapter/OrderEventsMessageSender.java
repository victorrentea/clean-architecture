package victor.training.clean.order.adapter;

import org.springframework.stereotype.Component;
import victor.training.clean.order.service.OrderEventsSender;

@Component
public class OrderEventsMessageSender implements OrderEventsSender {
   @Override
   public void sendOrderConfirmed(Long orderId) {
      // TODO send message over queue
   }
}
