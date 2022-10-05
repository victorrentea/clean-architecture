package victor.training.clean.domain.insurance.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import victor.training.clean.domain.customer.api.CustomerApi;
import victor.training.clean.domain.customer.api.dto.CustomerDto;
import victor.training.clean.domain.customer.api.event.CustomerRegisteredEvent;
import victor.training.clean.domain.customer.model.Customer;
import victor.training.clean.domain.insurance.model.InsurancePolicy;
import victor.training.clean.domain.customer.repo.CustomerRepo;
import victor.training.clean.domain.insurance.repo.InsurancePolicyRepo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuotationService {
   private final InsurancePolicyRepo insurancePolicyRepo;
   private final CustomerApi customerApi;

   @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
   public void onCustmerRegistede(CustomerRegisteredEvent event) {
      List<Long> customerIds = List.of();


//      customerApi.getByIdList(customerIds)
      List<CustomerDto> all = customerIds.stream().map(customerApi::getCustomerById).collect(Collectors.toList());


      CustomerDto dto = customerApi.getCustomerById(event.getCustomerId());
      quoteCustomer(event.getCustomerId(), dto.getCustomerName());
   }


   public void quoteCustomer(Long customerId, String customerName) {
      log.debug("Quoting customer (~230 total lines of code, 40 Cyclomatic Complexity): " + customerId);
      InsurancePolicy policy = new InsurancePolicy();
      policy.setCustomerId(customerId);
      policy.setCustomerName(customerName);
      policy.setValueInEur(BigDecimal.ONE);
      log.info("Persist the policy");
      insurancePolicyRepo.save(policy);
   }

   public void printPolicy(long policyId) {
      InsurancePolicy policy = insurancePolicyRepo.findById(policyId).orElseThrow();
//      String customerName = customerRepo.findById(policy.getCustomerId()).orElseThrow().getName();
      System.out.println("Insurange Policy for " + policy.getCustomerName());

   }
}
