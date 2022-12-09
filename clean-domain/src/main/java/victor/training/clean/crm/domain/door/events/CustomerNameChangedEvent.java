package victor.training.clean.crm.domain.door.events;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerNameChangedEvent {
  private final Long customerId;
  private final String customerName;

  public Long getCustomerId() {
    return customerId;
  }

  public String getCustomerName() {
      return customerName;
  }
}
