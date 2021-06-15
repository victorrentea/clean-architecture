package victor.training.clean.supplier.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import victor.training.clean.supplier.service.OrderConfirmedEventHandler;

@Slf4j
@Component
public class SupplierMessageListener {
   @Autowired
   private OrderConfirmedEventHandler orderConfirmedEventHandler;

   //@ServiceActivator(inputChannel = "ordersConfirmedIn")
   public void method(String orderId) {
      log.info("Received message on queue + " +orderId);

   }
}
