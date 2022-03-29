package victor.training.clean.domain.service;

import org.springframework.stereotype.Service;
import victor.training.clean.domain.entity.Customer;
import victor.training.clean.domain.repo.CustomerRepo;

@Service
public class RegisterCustomerService {
   private final CustomerRepo customerRepo;
   private final QuotationService quotationService;

   public RegisterCustomerService(CustomerRepo customerRepo, QuotationService quotationService) {
      this.customerRepo = customerRepo;
      this.quotationService = quotationService;
   }

   public void register(Customer customer) {

      // Heavy business logic
      // Heavy business logic
      // Heavy business logic
      // Heavy business logic
      // Heavy business logic
      // Heavy business logic
      int discountPercentage = customer.getDiscountPercentage();
      System.out.println("Biz Logic with discount " + discountPercentage);
      // Heavy business logic
      // Heavy business logic
      // Heavy business logic
      // Heavy business logic
      // Heavy business logic
      // Heavy business logic

      customerRepo.save(customer);
      // Heavy business logic
      quotationService.quoteCustomer(customer);
   }
}
