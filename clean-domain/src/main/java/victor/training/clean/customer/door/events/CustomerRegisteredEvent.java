package victor.training.clean.customer.door.events;

import lombok.Value;
import org.springframework.context.ApplicationEvent;

@Value
public class CustomerRegisteredEvent {
  Long customerId;
  String customerName; // events carrying data = fat events : they are part of your API
  // => Event Sourcing
}
