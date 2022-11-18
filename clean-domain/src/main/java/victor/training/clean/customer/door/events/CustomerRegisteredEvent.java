package victor.training.clean.customer.door.events;

import lombok.Value;
import org.springframework.context.ApplicationEvent;

@Value
public class CustomerRegisteredEvent extends ApplicationEvent {
  Long customerId;
}
