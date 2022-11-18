package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

@Service
@RequiredArgsConstructor
public class CustomerService {
  private final CustomerRepo customerRepo;
//  private final QuotationService quotationService; // coupling between domain services

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

  }

}
