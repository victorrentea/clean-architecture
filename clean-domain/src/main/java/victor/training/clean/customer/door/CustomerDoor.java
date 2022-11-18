package victor.training.clean.customer.door;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.customer.domain.model.Customer;
import victor.training.clean.customer.domain.repo.CustomerRepo;
import victor.training.clean.customer.door.knob.CustomerKnob;

@Component
@RequiredArgsConstructor
public class CustomerDoor {
  private final CustomerRepo customerRepo;

  public CustomerKnob getCustomerById(Long customerId) {
    Customer customer = customerRepo.findById(customerId).orElseThrow();
    return new CustomerKnob(customer.getId(), customer.getName());
  }
}
