package victor.training.clean.customer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.customer.entity.Customer;
import victor.training.clean.customer.entity.InsurancePolicy;
import victor.training.clean.customer.repo.InsurancePolicyRepo;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuotationService {
   private final InsurancePolicyRepo insurancePolicyRepo;

   public void requoteCustomer(Customer customer) {
      log.debug("Requoting customer (~230 total lines of code): " + customer.getName());
      InsurancePolicy policy = new InsurancePolicy();
      policy.setCustomer(customer);
      policy.setValue(BigDecimal.ONE);
      insurancePolicyRepo.save(policy);
   }
}
