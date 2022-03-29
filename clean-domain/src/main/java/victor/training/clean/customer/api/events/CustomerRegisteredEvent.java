package victor.training.clean.customer.api.events;

public class CustomerRegisteredEvent {
   private final long customerId; // tomorrow this is a message on a queue

   public CustomerRegisteredEvent(long customerId) {
      this.customerId = customerId;
   }

   public long getCustomerId() {
      return customerId;
   }
}
