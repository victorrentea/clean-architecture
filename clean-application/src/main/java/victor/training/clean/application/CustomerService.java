package victor.training.clean.application;

import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.domain.service.QuotationService;

@Service
public class CustomerService {
  private final CustomerRepo customerRepo;
  private final QuotationService quotationService;

  public CustomerService(CustomerRepo customerRepo, QuotationService quotationService, QuotationService quotationService1) {
    this.customerRepo = customerRepo;
    this.quotationService = quotationService1;
  }

  public Customer register(Customer customer) {
    // Heavy business logic
    // Heavy business logic
    // Heavy business logic
    // TODO Where can I move this little logic? (... operating on the state of a single entity)
    int discountPercentage = 3;
    if (customer.isGoldMember()) {
      discountPercentage += 1;
    }
    System.out.println("Biz Logic with discount " + discountPercentage);
    // Heavy business logic
    // Heavy business logic
//    customerRepo.save(customer);
    if (customer.getSite() == null) {
      quotationService.quoteCustomer(customer);
    }
    // Heavy business logic
    return customer;
  }
}
