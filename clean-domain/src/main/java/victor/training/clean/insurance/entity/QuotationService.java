package victor.training.clean.insurance.entity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.clean.common.events.RequoteCustomerEvent;
import victor.training.clean.insurance.repo.InsurancePolicyRepo;
import victor.training.clean.insurance.service.InsurancePolicy;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuotationService {
   private final InsurancePolicyRepo insurancePolicyRepo;

   @EventListener
   public void requoteCustomer(RequoteCustomerEvent event) {
//      log.debug("Requoting customer (~230 total lines of code, 40 Cyclomatic Complexity): " + customer.getName());
      InsurancePolicy policy = new InsurancePolicy();
      policy.setCustomerId(event.getCustomerId());
      policy.setValue(BigDecimal.ONE);
      insurancePolicyRepo.save(policy);
   }
}
