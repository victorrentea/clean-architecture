package victor.training.clean.crm.api.events;

import lombok.Value;

@Value
public class CustomerChangedAddressEvent {
  long customerId;
  String newAddress;
}
