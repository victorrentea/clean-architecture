package victor.training.clean.customer.door.event;


import lombok.Value;

@Value
public class CustomerRegisteredEvent {
  long customerId;
  String customerName;
}
