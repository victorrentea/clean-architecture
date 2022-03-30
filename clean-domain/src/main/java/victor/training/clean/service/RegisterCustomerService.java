package victor.training.clean.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.clean.entity.Customer;
import victor.training.clean.repo.CustomerRepo;

@Service
@RequiredArgsConstructor
public class RegisterCustomerService {
private final CustomerRepo customerRepo;
private final QuotationService quotationService;
   public void register(Customer customer) {
      // Heavy business logic
      // Heavy business logic
      // Heavy business logic
      // Heavy business logic
      // Heavy business logic
      // Heavy business logic
      // Heavy business logic
      // Where can I move this? (a bit of domain logic operating on the state of a single entity)
      int discountPercentage = customer.getDiscountPercentage();
      System.out.println("Biz Logic with discount " + discountPercentage);
      // Heavy business logic

      // Heavy business logic
      customerRepo.save(customer);
      // Heavy business logic
      quotationService.quoteCustomer(customer);
   }

}
