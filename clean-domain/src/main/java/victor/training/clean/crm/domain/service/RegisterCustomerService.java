package victor.training.clean.crm.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import victor.training.clean.crm.api.events.CustomerChangedAddressEvent;
import victor.training.clean.crm.domain.model.Address;
import victor.training.clean.crm.domain.model.Customer;
import victor.training.clean.crm.domain.repo.CustomerRepo;

@Service
@RequiredArgsConstructor
public class RegisterCustomerService { // ACTION, verb, nu substantiv
  private final CustomerRepo customerRepo;

  public void register(Customer customer) {
    if (customerRepo.existsByEmail(customer.getEmail())) {
      throw new IllegalArgumentException("Customer email is already registered");
      // throw new CleanException(ErrorCode.DUPLICATED_CUSTOMER_EMAIL);
    }

//    CustomerDto oups;
    
    // Heavy business logic
    // Heavy business logic
    // Heavy business logic
    // Heavy business logic
    // Heavy business logic
    // Heavy business logic
    // Heavy business logic
    // Heavy business logic
    // TODO Where can I move this little logic? (... operating on the state of a single entity)
    int discountPercentage = customer.getDiscountPercentage();

    // enum {REGULAR(3), GOLD(4), BUSINESS(5), PLATINUM(5) }
    System.out.println("Biz Logic with discount " + discountPercentage);
    // Heavy business logic
    // Heavy business logic
    customerRepo.save(customer);
    // Heavy business logic
  }

  @Autowired
  private ApplicationEventPublisher eventPublisher;

  public void changeCustomerAddress(Long customerId, Address newAddress) {
    eventPublisher.publishEvent(new CustomerChangedAddressEvent(customerId, "newAddress"));
  }

  //  public Optional<Customer> findById(long customerId) {
  //    return customerRepo.findById(customerId); // code smell : Middle Man
  //  }
}
