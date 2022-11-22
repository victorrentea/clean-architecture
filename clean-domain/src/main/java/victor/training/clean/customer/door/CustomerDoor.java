package victor.training.clean.customer.door;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.clean.customer.domain.model.Customer;
import victor.training.clean.customer.domain.repo.CustomerRepo;
import victor.training.clean.customer.door.knob.CustomerKnob;

// an internal Api between modules
@RequiredArgsConstructor
@Service
public class CustomerDoor {
  private final CustomerRepo customerRepo;
  public CustomerKnob getCustomer(Long customerId) {
    Customer customer = customerRepo.findById(customerId).orElseThrow();
    return new CustomerKnob(customer.getId(), customer.getName());
  }
}
