package victor.training.clean.insurance.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.clean.crm.api.CrmApi;
import victor.training.clean.crm.api.event.CustomerDetailsChangedEvent;
import victor.training.clean.crm.api.knob.CustomerKnob;
import victor.training.clean.insurance.api.event.PolicyRequiresReevalutionEvent;
import victor.training.clean.insurance.domain.model.InsurancePolicy;
import victor.training.clean.insurance.domain.model.PolicyNotification;
import victor.training.clean.insurance.domain.repo.InsurancePolicyRepo;
import victor.training.clean.insurance.domain.repo.PolicyNotificationRepo;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuotationService {
   private final InsurancePolicyRepo insurancePolicyRepo;
   private final PolicyNotificationRepo policyNotificationRepo;
//   private final NotificationService notificationService;
//   private final EmailSender emailSender;
   private final CrmApi crmApi;

   @EventListener
   public void customerDetailsChanged(CustomerDetailsChangedEvent event) {
      InsurancePolicy currentPolicy = insurancePolicyRepo.findByCustomerId(event.customerId());
      CustomerKnob customer = crmApi.getCustomerById(event.customerId());
      if (event.customerId() != currentPolicy.getCountryId()) {
         // Imagine: 💭 calculations to see if the policy has to be updated
//         notificationService.sendReevaluatePolicy(newCustomer, "Country changed");
         eventPublisher.publishEvent(new PolicyRequiresReevalutionEvent(currentPolicy.getCustomerName(), "Country changed"));
         policyNotificationRepo.save(new PolicyNotification()
             .setTitle("Policy update requested due to country changed")
             .setPolicy(currentPolicy)
         );
      }
   }

   private final ApplicationEventPublisher eventPublisher;

   public void createPolicy(String customerName) {
      // TODO saya check the customer name is unique when creating an insurance policy:

      //  there must be no 2 policies for the same name;
      if (insurancePolicyRepo.existsByCustomerName(customerName)) {
         throw new IllegalStateException("Policy with this customer name already exists");
      }
//      Long customerId = crmApi.findByName(customerName); // alternative
   }

   public void printPolicy(long policyId) {
      InsurancePolicy policy = insurancePolicyRepo.findById(policyId).orElseThrow();
      String customerName = policy.getCustomerName();
      System.out.println("Insurance Policy for " + customerName);
   }
}
