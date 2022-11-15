package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import victor.training.clean.DDD.DomainService;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;


@DomainService
@RequiredArgsConstructor
public class RegisterCustomerService { // action not a noun! FEATURE. pure behavior.
  // removing the "Service" suffix hurts code navigability
  private final CustomerRepo customerRepo;
  public void register(Customer customer) {
//    CustomerDto // my API
    // Heavy business logic
    // Heavy business logic
    // Heavy business logic
    // Heavy business logic
    // Heavy business logic
    // Heavy business logic
    // Heavy business logic
//    new CustomerHistory().setCustomer(customer);

    // Heavy business logic
    // Heavy business logic
    // Heavy business logic
    // Heavy business logic
    // TODO Where can I move this little logic? (... operating on the state of a single entity)
    int discountPercentage = customer.getDiscountPercentage();
    System.out.println("Biz Logic with discount " + discountPercentage);
    // Heavy business logic
    // Heavy business logic

//    customerHistoryRepo.save(customer);
    customerRepo.save(customer);

    Long newId = customer.getId();
  }
}
