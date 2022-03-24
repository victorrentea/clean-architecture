package victor.training.clean.customer.api.event;

public class CustomerRegistrationCreatedEvent {
   private final long customerId;
   private final String name;

   public CustomerRegistrationCreatedEvent(long customerId, String name) {
      this.customerId = customerId;
      this.name = name;
   }

   public String getName() {
      return name;
   }

   public long getCustomerId() {
      return customerId;
   }
}
