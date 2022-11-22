package victor.training.clean.shared.domain.service;

import org.springframework.stereotype.Service;
import victor.training.clean.shared.domain.model.Customer;
import victor.training.clean.shared.domain.repo.CustomerRepo;

@Service

// promoted into the Domain, into a better world, only using MY DOMAIN MODEL.
public class RegisterCustomerService {// action, not noun. it's a piece of logic.
  private final CustomerRepo customerRepo;
  private final QuotationService quotationService;

  public RegisterCustomerService(CustomerRepo customerRepo, QuotationService quotationService, QuotationService quotationService1) {
    this.customerRepo = customerRepo;
    this.quotationService = quotationService1;
  }

  public Customer register(Customer customer, int couponId, int customerId) {
    // Heavy business logic
    // Heavy business logic
    // Heavy business logic


    // TODO Where can I move this little logic?
    //    (... operating on the state of a single entity)
    int discountPercentage = customer.getDiscountPercentage();
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
