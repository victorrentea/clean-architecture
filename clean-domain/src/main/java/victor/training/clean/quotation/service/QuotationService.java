package victor.training.clean.quotation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.clean.common.event.CustomerRegisteredEvent;
import victor.training.clean.quotation.entity.CustomerVO;
import victor.training.clean.quotation.entity.InsurancePolicy;
import victor.training.clean.quotation.repo.InsurancePolicyRepo;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuotationService {
   private final InsurancePolicyRepo insurancePolicyRepo;
   private final ExternalCustomerProvider customerProvider;

   @EventListener
   public void requoteCustomer(CustomerRegisteredEvent event) {
      CustomerVO customer = customerProvider.getCustomerById(event.getCustomerId());
      log.debug("Requoting customer (~230 total lines of code): " + customer.getName());
      InsurancePolicy policy = new InsurancePolicy();
      policy.setCustomerName(customer.getName());
      policy.setValue(BigDecimal.ONE);
      insurancePolicyRepo.save(policy);
   }
}
