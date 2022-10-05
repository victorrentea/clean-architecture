package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.InsurancePolicy;
import victor.training.clean.repo.CustomerRepo;
import victor.training.clean.repo.InsurancePolicyRepo;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuotationService {
   private final InsurancePolicyRepo insurancePolicyRepo;
   private final CustomerRepo customerRepo;
   public void quoteCustomer(Customer customer) {
      log.debug("Quoting customer (~230 total lines of code, 40 Cyclomatic Complexity): " + customer.getId());
      InsurancePolicy policy = new InsurancePolicy();
      policy.setCustomerId(customer.getId());
      policy.setCustomerName(customer.getName());
      policy.setValueInEur(BigDecimal.ONE);
      insurancePolicyRepo.save(policy);
   }

   public void printPolicy(long policyId) {
      InsurancePolicy policy = insurancePolicyRepo.findById(policyId).orElseThrow();
//      String customerName = customerRepo.findById(policy.getCustomerId()).orElseThrow().getName();
      System.out.println("Insurange Policy for " + policy.getCustomerName());

   }
}
