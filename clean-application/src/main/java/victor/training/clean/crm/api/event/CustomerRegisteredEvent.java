package victor.training.clean.crm.api.event;

  // Option A: (big)
public record CustomerRegisteredEvent(Long customerId,
                                      String email,
                                      String name) {
  // Option B: (thin)  don't require a call back to the sender for data
//public record CustomerRegisteredEvent(Long customerId) {

}


//