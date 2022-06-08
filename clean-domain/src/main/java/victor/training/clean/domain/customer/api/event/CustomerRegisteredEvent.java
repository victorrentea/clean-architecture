package victor.training.clean.domain.customer.api.event;

import lombok.Value;

@Value
public class CustomerRegisteredEvent {
    long customerId;
    // should we include state (other than ID) into our events/mq messages ?
//    String name; // Event-Carried State Transfer (~REST)
    // sounds like a good ideaL: less network calls


    //BUT: 2 years later, there are 60 fields in this event and I'm starting to talk
    // about event version 7
}
