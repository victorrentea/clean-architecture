package victor.training.clean.crm.api.event;

  // Option A: (big) - REQUIRES A SCHEMA!! (avro, apiary yaml schema, Pact.io CDC)
public record CustomerRegisteredEvent(Long customerId,
                                      String email,
                                      String name) {
  // Option B: (thin) - require a call back to the sender for data
//public record CustomerRegisteredEvent(Long customerId) {

}


//