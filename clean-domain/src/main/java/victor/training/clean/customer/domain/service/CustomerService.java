package victor.training.clean.customer.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import victor.training.clean.customer.domain.model.Customer;
import victor.training.clean.customer.domain.repo.CustomerRepo;
import victor.training.clean.customer.door.events.CustomerRegisteredEvent;

@Service
@RequiredArgsConstructor
public class CustomerService {
  private final CustomerRepo customerRepo;
//  private final QuotationService quotationService; // coupling between domain services
  private final ApplicationEventPublisher eventPublisher;

  public void register(Customer customer) {
    // Heavy business logic
    // Heavy business logic
    // Heavy business logic
    // TODO Where can I move this little logic? (..
    //  . operating on the state of a single entity)
    int discountPercentage = customer.getDiscountPercentage();
    System.out.println("Biz Logic with discount " + discountPercentage);
    // Heavy business logic
    // Heavy business logic
    customerRepo.save(customer);
    // Heavy business logic
//    quotationService.quoteCustomer(customer);
    eventPublisher.publishEvent(new CustomerRegisteredEvent(customer.getId(), customer.getName()));
    System.out.println("AFTER the handler in insurance ran");
  }

  // the problem of using events within the same spring app instead of calls:
  // - hard to trace (not displayed Call-Hierarchy)
  // - cannot tell which of the listeners runs first (if they are many)

}
