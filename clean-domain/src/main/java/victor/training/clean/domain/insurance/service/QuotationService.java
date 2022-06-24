package victor.training.clean.domain.insurance.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.customer.api.CustomerApi;
import victor.training.clean.domain.customer.api.dto.CustomerInternalDto;
import victor.training.clean.domain.customer.api.events.CustomerRegisteredEvent;
import victor.training.clean.domain.customer.entity.Customer;
import victor.training.clean.domain.customer.repo.CustomerRepo;
import victor.training.clean.domain.insurance.entity.InsurancePolicy;
import victor.training.clean.domain.insurance.repo.InsurancePolicyRepo;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuotationService {
   private final InsurancePolicyRepo insurancePolicyRepo;
   private final CustomerApi customerApi;


   // ???
   @EventListener
   public void quoteCustomer(CustomerRegisteredEvent event) {
      log.debug("Quoting customer (~230 total lines of code," +
                " 40 Cyclomatic Complexity): " + event.getCustomerId());
      InsurancePolicy policy = new InsurancePolicy();

      policy.setCustomerId(event.getCustomerId());
      policy.setValueInEur(BigDecimal.ONE);
      insurancePolicyRepo.save(policy);
   }

   public void printPolicy(Long id) {
      InsurancePolicy policy = insurancePolicyRepo.findById(id).orElseThrow();

      CustomerInternalDto customer = customerApi.retrieveById(policy.getCustomerId());
      String customerName = customer.getName();
      System.out.println(customerName);
   }

//   private final CustomerRepo
}
