package victor.training.clean.crm.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.clean.crm.domain.model.Customer;
import victor.training.clean.crm.domain.repo.CustomerRepo;
import victor.training.clean.shared.api.customer.CustomerDdo;
import victor.training.clean.shared.api.customer.ICustomerInternalApi;
@RequiredArgsConstructor
@Component
public class CustomerInternalApi implements ICustomerInternalApi {
  private final CustomerRepo customerRepo;
  @Override
  public CustomerDdo getCustomer(long customerId) {
    Customer customer = customerRepo.findById(customerId).orElseThrow();
    return new CustomerDdo(customer.getName());
  }
}
