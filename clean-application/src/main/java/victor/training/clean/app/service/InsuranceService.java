package victor.training.clean.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.app.model.Customer;
import victor.training.clean.app.model.Email;
import victor.training.clean.app.model.InsurancePolicy;
import victor.training.clean.app.model.PolicyNotification;
import victor.training.clean.app.repo.InsurancePolicyRepo;
import victor.training.clean.app.repo.PolicyNotificationRepo;

@Slf4j
@Service
@RequiredArgsConstructor
public class InsuranceService {
   private final InsurancePolicyRepo insurancePolicyRepo;
   private final PolicyNotificationRepo policyNotificationRepo;
   private final EmailSenderPort emailSenderPort;

   public void customerDetailsChanged(Customer newCustomer) {
      InsurancePolicy currentPolicy = insurancePolicyRepo.findByCustomerId(newCustomer.getId());
      if (newCustomer.getCountry().getId() != currentPolicy.getCountryId()) {
         // Imagine calculations to see if the policy has to be updated
         sendReevaluatePolicy(newCustomer, "Country changed");
         policyNotificationRepo.save(new PolicyNotification()
             .setTitle("Policy update requested due to country changed")
             .setPolicy(currentPolicy)
         );
      }
   }

   private void sendReevaluatePolicy(Customer customer, String reason) {
      Email email = Email.builder()
          .from("noreply@cleanapp.com")
          .to("reps@cleanapp.com")
          .subject("Customer " + customer.getName() + " policy has to be re-evaluated")
          .body("Please review the policy due to : " + reason)
          .build();
      emailSenderPort.sendEmail(email);
   }

   public void printPolicy(long policyId) {
      InsurancePolicy policy = insurancePolicyRepo.findById(policyId).orElseThrow();
      String customerName = policy.getCustomer().getName();
      System.out.println("Insurance Policy for " + customerName);
   }
}
