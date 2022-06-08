package victor.training.clean.domain.insurance.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.customer.model.Customer;
import victor.training.clean.domain.customer.repo.CustomerRepo;
import victor.training.clean.domain.customer.service.CustomerService;
import victor.training.clean.domain.insurance.model.InsurancePolicy;
import victor.training.clean.domain.insurance.repo.InsurancePolicyRepo;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuotationService {
   private final InsurancePolicyRepo insurancePolicyRepo;
   private final CustomerService customerService;

   public void quoteCustomer(Customer customer) {
      log.debug("Quoting customer (~230 total lines of code, 40 Cyclomatic Complexity): " + customer.getName());
      InsurancePolicy policy = new InsurancePolicy();
      policy.setCustomerId(customer.getId());
      policy.setValueInEur(BigDecimal.ONE);
      insurancePolicyRepo.save(policy);
   }

   public void printPolicy(Long policyId) {
      InsurancePolicy policy = insurancePolicyRepo.findById(policyId).orElseThrow();
      Customer customer = customerService.findById(policy.getCustomerId());
      System.out.println(customer.getName());

   }
}
