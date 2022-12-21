package victor.training.clean.crm.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.crm.domain.model.Customer;
import victor.training.clean.crm.domain.repo.CustomerRepo;
import victor.training.clean.crm.api.ddo.CustomerDdo;

@RequiredArgsConstructor
@Component
public class CustomerInternalApi {
  private final CustomerRepo customerRepo;

  public CustomerDdo getCustomer(long customerId) {
    Customer customer = customerRepo.findById(customerId).orElseThrow();
    return new CustomerDdo(customer.getName());
  }
}
