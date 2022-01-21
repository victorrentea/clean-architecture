package victor.training.clean.quotation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.quotation.entity.Customer;
import victor.training.clean.quotation.entity.InsurancePolicy;
import victor.training.clean.quotation.repo.InsurancePolicyRepo;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuotationService {
   private final InsurancePolicyRepo insurancePolicyRepo;
   private final ICustomerClient customerClient;

   public void requoteCustomer(Customer customer) {
      log.debug("Requoting customer (~230 total lines of code, 40 Cyclomatic Complexity): " + customer.getName());
      InsurancePolicy policy = new InsurancePolicy();
//      policy.setCustomer(customer);
      policy.setValue(BigDecimal.ONE);
      insurancePolicyRepo.save(policy);
   }


   public void method() {
      InsurancePolicy policy = insurancePolicyRepo.findById(13L).get();
      Customer customer = customerClient.retrieveById(policy.getCustomerId());
//      System.out.println(policy.getCustomer().getName());
   }


}
