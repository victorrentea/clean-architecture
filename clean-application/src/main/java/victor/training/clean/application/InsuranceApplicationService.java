package victor.training.clean.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.InsurancePolicy;
import victor.training.clean.domain.model.PolicyNotification;
import victor.training.clean.domain.repo.InsurancePolicyRepo;
import victor.training.clean.domain.repo.PolicyNotificationRepo;
import victor.training.clean.domain.service.NotificationService;

@Slf4j
@Service
@RequiredArgsConstructor
public class InsuranceApplicationService {
   private final InsurancePolicyRepo insurancePolicyRepo;
   private final PolicyNotificationRepo policyNotificationRepo;
   private final NotificationService notificationService;

   public void customerDetailsChanged(Customer newCustomer) {
      InsurancePolicy currentPolicy = insurancePolicyRepo.findByCustomerId(newCustomer.getId());
      if (newCustomer.getCountry().getId() != currentPolicy.getCountryId()) {
         // Imagine calculations to see if the policy has to be updated
         notificationService.sendReevaluatePolicy(newCustomer, "Country changed");
         policyNotificationRepo.save(new PolicyNotification()
             .setTitle("Policy update requested due to country changed")
             .setPolicy(currentPolicy)
         );
      }
   }


   public void printPolicy(long policyId) {
      InsurancePolicy policy = insurancePolicyRepo.findById(policyId).orElseThrow();
      String customerName = policy.getCustomer().getName();
      System.out.println("Insurance Policy for " + customerName);
   }
}
