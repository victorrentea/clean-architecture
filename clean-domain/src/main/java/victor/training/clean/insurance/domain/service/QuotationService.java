package victor.training.clean.insurance.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.crm.domain.model.Customer;
import victor.training.clean.insurance.domain.model.InsurancePolicy;
import victor.training.clean.insurance.domain.repo.InsurancePolicyRepo;
import victor.training.clean.shared.api.customer.CustomerDdo;
import victor.training.clean.shared.api.customer.ICustomerInternalApi;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuotationService {
   private final InsurancePolicyRepo insurancePolicyRepo;
   private final ICustomerInternalApi customerInternalApi;

   public void quoteCustomer(Long customerId) {
      log.debug("Quoting customer (~230 total lines of code, 40 Cyclomatic Complexity): " + customerId);
      InsurancePolicy policy = new InsurancePolicy();
      policy.setCustomerId(customerId);
      policy.setValueInEur(BigDecimal.ONE);
      insurancePolicyRepo.save(policy);
   }

   public void printPolicy(long policyId) {
      InsurancePolicy policy = insurancePolicyRepo.findById(policyId).orElseThrow();
      CustomerDdo ddo = customerInternalApi.getCustomer(policy.getCustomerId());
      String customerName = ddo.getName();
      System.out.println("Insurange Policy for " + customerName);
   }
}
