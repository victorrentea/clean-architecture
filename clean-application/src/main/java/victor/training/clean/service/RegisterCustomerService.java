package victor.training.clean.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.clean.entity.Customer;
import victor.training.clean.repo.CustomerRepo;

@RequiredArgsConstructor
@Service
public class RegisterCustomerService { // Customer se anticipeaza ca va avea total in DB 60 de coloane<< e entitatea aia mare a ta
   private final QuotationService quotationService;
   private final CustomerRepo customerRepo;

   public void register(Customer customer) { // VREAU CEVA SA TIPE CAND intra DTOurile in DOMENIU
      // a) code analysis (package deps)
      // b) doua module

      // Heavy business logic
      // Heavy business logic
      // Heavy business logic
      int discountPercentage = customer.getDiscountPercentage();
      System.out.println("Biz Logic with discount " + discountPercentage);
      // Heavy business logic
      // Heavy business logic
      customerRepo.save(customer);
      // Heavy business logic

      quotationService.requoteCustomer(customer);
   }

}
