package victor.training.clean.customer.door.event;


public class CustomerRegisteredEvent {
  private final long customerId;

  public CustomerRegisteredEvent(long customerId) {
    this.customerId = customerId;
  }

  public long getCustomerId() {
    return customerId;
  }
}
