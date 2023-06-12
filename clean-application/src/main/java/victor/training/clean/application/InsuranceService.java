package victor.training.clean.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.model.InsurancePolicy;
import victor.training.clean.domain.model.PolicyNotification;
import victor.training.clean.domain.repo.InsurancePolicyRepo;
import victor.training.clean.domain.repo.PolicyNotificationRepo;
import victor.training.clean.infra.EmailSender;

@Slf4j
@Service
@RequiredArgsConstructor
public class InsuranceService {
   private final InsurancePolicyRepo insurancePolicyRepo;
   private final PolicyNotificationRepo policyNotificationRepo;
   private final EmailSender emailSender;

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
      emailSender.sendEmail(email);
   }

   public void printPolicy(long policyId) {
      InsurancePolicy policy = insurancePolicyRepo.findById(policyId).orElseThrow();
      String customerName = policy.getCustomer().getName();
      System.out.println("Insurance Policy for " + customerName);
   }
}
