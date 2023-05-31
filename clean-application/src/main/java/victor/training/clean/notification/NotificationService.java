package victor.training.clean.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.clean.crm.api.CrmApi;
import victor.training.clean.crm.api.event.CustomerRegisteredEvent;
import victor.training.clean.crm.api.event.CustomerUpgradedToGoldEvent;
import victor.training.clean.insurance.api.event.PolicyRequiresReevalutionEvent;

@RequiredArgsConstructor
@Service
public class NotificationService {
  private final EmailSender emailSender;
  private final CrmApi crmApi;
//  private final CrmApi crmApi;

//  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    //    dont unless you want this to run ONLY if the tx of the sender COMMITS ok
//  @Async
    //    dont want to wait for this long running event handler. Good luck with race conditions !
  @EventListener // 💖💖💖💖
  public void sendWelcomeEmail(CustomerRegisteredEvent event) {
    Email email = new Email();
    email.setFrom("noreply@cleanapp.com");
    email.setTo(event.email());
    email.setSubject("Account created for");
    email.setBody("Welcome to our world, " + event.name() + ". You'll like it! Sincerely, Team");
    emailSender.sendEmail(email);
  }

  @EventListener
  public void sendGoldBenefitsEmail(CustomerUpgradedToGoldEvent event) {
    var customer = crmApi.getCustomerById(event.customerId());
    Email email = new Email();
    email.setFrom("noreply@cleanapp.com");
    email.setTo(customer.email());
    email.setSubject("Welcome to the Gold membership!");
    int discountPercentage = customer.discountPercentage();
    email.setBody("Here are your perks: ... Enjoy your special discount of " + discountPercentage + "%");
    emailSender.sendEmail(email);
  }

  @EventListener
  public void sendReevaluatePolicy(PolicyRequiresReevalutionEvent event) {
    Email email = new Email();
    email.setFrom("noreply@cleanapp.com");
    email.setTo("reps@cleanapp.com");
    email.setSubject("Customer " + event.customerName() + " policy has to be re-evaluated");
    email.setBody("Please review the policy due to : " + event.customerName());
      emailSender.sendEmail(email);
  }

}
