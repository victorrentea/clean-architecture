package victor.training.clean.order.service;

public interface OrderEventsSender {
   void sendOrderConfirmed(Long orderId);
}
