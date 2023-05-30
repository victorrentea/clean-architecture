package victor.training.clean.insurance.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.notification.domain.NotificationService;
import victor.training.clean.crm.domain.model.Customer;
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
   private final NotificationService notificationService;
//   private final EmailSender emailSender;

   public void customerDetailsChanged(Customer newCustomer) {
      InsurancePolicy currentPolicy = insurancePolicyRepo.findByCustomerId(newCustomer.getId());
      if (newCustomer.getCountry().getId() != currentPolicy.getCountry().getId()) {
         // Imagine: 💭 calculations to see if the policy has to be updated
         notificationService.sendReevaluatePolicy(newCustomer, "Country changed");
         policyNotificationRepo.save(new PolicyNotification()
             .setTitle("Policy update requested due to country changed")
             .setPolicy(currentPolicy)
         );
      }
   }

   public void createPolicy(String customerName) {
      // TODO saya check the customer name is unique when creating an insurance policy:
      //  there must be no 2 policies for the same name;
   }

   public void printPolicy(long policyId) {
      InsurancePolicy policy = insurancePolicyRepo.findById(policyId).orElseThrow();
      String customerName = policy.getCustomer().getName();
      System.out.println("Insurance Policy for " + customerName);
   }
}
