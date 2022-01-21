package victor.training.clean.customer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.clean.customer.entity.Customer;
import victor.training.clean.repo.CustomerRepo;
import victor.training.clean.quotation.service.QuotationService;

@RequiredArgsConstructor
@Service
public class CustomerService {
   private final CustomerRepo customerRepo;
   private final QuotationService quotationService;

   public void registerCustomer(Customer customer) {
      // Heavy business logic
      // Heavy business logic
      // Heavy business logic
      int discountPercentage = 3;
      if (customer.isGoldMember()) {
         discountPercentage += 1;
      }
      System.out.println("Biz Logic with discount " + discountPercentage);
      // Heavy business logic
      // Heavy business logic
      customerRepo.save(customer);
      // Heavy business logic

      quotationService.requoteCustomer(customer);
   }
}