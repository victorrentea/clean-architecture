package victor.training.clean.customer.api.event;

import lombok.Data;

@Data
public class CustomerRegisteredEvent {
    //    private final CustomerInternalDto
    private final long customerId;
}