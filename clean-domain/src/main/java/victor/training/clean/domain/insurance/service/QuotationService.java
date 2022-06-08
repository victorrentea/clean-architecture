package victor.training.clean.domain.insurance.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.customer.model.Customer;
import victor.training.clean.domain.insurance.model.InsurancePolicy;
import victor.training.clean.domain.insurance.repo.InsurancePolicyRepo;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuotationService {
   private final InsurancePolicyRepo insurancePolicyRepo;

   public void quoteCustomer(Customer customer) {
      log.debug("Quoting customer (~230 total lines of code, 40 Cyclomatic Complexity): " + customer.getName());
      InsurancePolicy policy = new InsurancePolicy();
      policy.setCustomer(customer);
      policy.setValueInEur(BigDecimal.ONE);
      insurancePolicyRepo.save(policy);
   }
}
