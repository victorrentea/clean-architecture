package victor.training.clean.customer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.clean.customer.entity.Customer;
import victor.training.clean.customer.repo.CustomerRepo;
import victor.training.clean.quotation.service.QuotationService;

@RequiredArgsConstructor
@Service
public class RegisterCustomerService {
   public final CustomerRepo customerRepo;
   public final QuotationService quotationService;

   public void registerCustomer(Customer customer) {
      // Heavy business logic
      // Heavy business logic
      // Heavy business logic

      // Feature Envy code smell
      int discountPercentage = customer.getDiscountPercentage();

      System.out.println("Biz Logic with discount " + discountPercentage);
      // Heavy business logic
      // Heavy business logic
      customerRepo.save(customer);
      // Heavy business logic

//      quotationService.requoteCustomer(customer);
   }
}