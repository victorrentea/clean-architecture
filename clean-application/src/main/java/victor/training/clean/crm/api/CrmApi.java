package victor.training.clean.crm.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.crm.api.knob.CustomerKnob;
import victor.training.clean.crm.domain.model.Customer;
import victor.training.clean.crm.domain.repo.CustomerRepo;

@RequiredArgsConstructor
@Component
public class CrmApi {
  private final CustomerRepo customerRepo;

  public CustomerKnob getCustomerById(long customerId) {
    Customer customer = customerRepo.findById(customerId).orElseThrow();
    return new CustomerKnob(customer.getId(),
        customer.getName(),
        customer.getEmail(),
        customer.getCountryId(),
        customer.getDiscountPercentage());
  }
}
