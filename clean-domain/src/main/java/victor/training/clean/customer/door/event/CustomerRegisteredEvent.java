package victor.training.clean.customer.door.event;


// fat event = aka "Event-Carried State Trasnsfer"
// listen to more:  https://www.youtube.com/watch?v=STKCRSUsyP0
public class CustomerRegisteredEvent {
  private final long customerId;
  private final String customerName;

  public CustomerRegisteredEvent(long customerId, String customerName) {
    this.customerId = customerId;
    this.customerName = customerName;
  }

  public long getCustomerId() {
    return customerId;
  }

  public String getCustomerName() {
    return customerName;
  }
}
