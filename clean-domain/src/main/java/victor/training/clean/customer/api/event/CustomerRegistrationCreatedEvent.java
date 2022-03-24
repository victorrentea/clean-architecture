package victor.training.clean.customer.api.event;

public class CustomerRegistrationCreatedEvent {
   private final long customerId;

   public CustomerRegistrationCreatedEvent(long customerId) {
      this.customerId = customerId;
   }

   public long getCustomerId() {
      return customerId;
   }
}
