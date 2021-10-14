package victor.training.clean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.clean.entity.Customer;
import victor.training.clean.repo.CustomerRepo;
import victor.training.clean.service.QuotationService;

@Service //#respect
@RequiredArgsConstructor
public class CustomerService {
   private final CustomerRepo customerRepo;
   private final QuotationService quotationService;

   public void register(Customer customer) {
      // Heavy business logic
      // Heavy business logic
      // Heavy business logic
      // Heavy business logic
      int discountPercentage = customer.getDiscountPercentage();
      System.out.println("Biz Logic with discount " + discountPercentage);
      // Heavy business logic
      // Heavy business logic
      customer.activate();

      customerRepo.save(customer);
      // Heavy business logic

      quotationService.requoteCustomer(customer);
   }
}
