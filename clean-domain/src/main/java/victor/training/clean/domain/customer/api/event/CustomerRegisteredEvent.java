package victor.training.clean.domain.customer.api.event;

import lombok.Value;

// Events:
@Value
public class CustomerRegisteredEvent {
    long customerId;// "Event Notification" thin
    //

    // rich  + 20 fields, just in case. all . take that,
    // "Event-Carried State Transfer" - carry state via events. (B)
        // v2.0

    // REpresentational State Transfer (REST)  (A)
        // v2.0
}
