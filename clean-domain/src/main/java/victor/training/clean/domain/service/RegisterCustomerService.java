package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.entity.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

@Service
@RequiredArgsConstructor
@Slf4j

public class RegisterCustomerService {
   private final CustomerRepo customerRepo;
   private final QuotationService quotationService;

   // some people consider this a code smell - boilerplate.
//   public Customer findById(long customerId) {
//      return customerRepo.findById(customerId).orElseThrow();
//   }

   public void register(Customer customer) {
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

   // colegu
   // colega
   // frasu
   // colegu
}
