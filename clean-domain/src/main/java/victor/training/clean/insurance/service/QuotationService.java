package victor.training.clean.insurance.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.clean.customer.api.CustomerApi;
import victor.training.clean.customer.api.dto.CustomerDto;
import victor.training.clean.customer.api.events.CustomerRegisteredEvent;
import victor.training.clean.insurance.entity.InsurancePolicy;
import victor.training.clean.insurance.repo.InsurancePolicyRepo;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuotationService {
   private final InsurancePolicyRepo insurancePolicyRepo;
   private final CustomerApi customerApi;
   @EventListener
   public void onCustomerRegistered(CustomerRegisteredEvent event) {
      quoteCustomer(event.getCustomerId());
   }

   public void quoteCustomer(Long customerId) {
      log.debug("Quoting customer (~230 total lines of code, 40 Cyclomatic Complexity): ");
      InsurancePolicy policy = new InsurancePolicy();
      policy.setCustomerId(customerId);
      policy.setValueInEur(BigDecimal.ONE);
      insurancePolicyRepo.save(policy);
   }

   public void displayPolicy(InsurancePolicy policy) {
      CustomerDto customer = customerApi.getCustomerById(policy.getCustomerId());
      String name = customer.getName();
      System.out.println(name);
   }
}
