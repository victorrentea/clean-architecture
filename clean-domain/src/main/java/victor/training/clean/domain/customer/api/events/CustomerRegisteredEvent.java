package victor.training.clean.domain.customer.api.events;

import lombok.Value;

@Value
public class CustomerRegisteredEvent {
    long customerId;
}
