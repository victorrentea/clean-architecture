package victor.training.clean.customer.door;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.customer.domain.model.Customer;
import victor.training.clean.customer.domain.repo.CustomerRepo;
import victor.training.clean.customer.door.knob.CustomerKnob;

// the internal API through which customer module receives method calls
// tomorrow, this MIGHT become a REST endpoint
@Component
@RequiredArgsConstructor
public class CustomerDoor { //± ApplicationService
  private final CustomerRepo customerRepo;

  public CustomerKnob findCustomerById(long customerId) {
    Customer customer = customerRepo.findById(customerId).orElseThrow();
    return new CustomerKnob(customer.getId(), customer.getName());
  }
}
