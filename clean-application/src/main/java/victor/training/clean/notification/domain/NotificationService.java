package victor.training.clean.notification.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import victor.training.clean.crm.api.event.CustomerRegisteredEvent;
import victor.training.clean.crm.domain.model.Customer;
import victor.training.clean.crm.domain.model.Email;
import victor.training.clean.infra.EmailSender;

@RequiredArgsConstructor
@Service
public class NotificationService {
  private final EmailSender emailSender;
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

  public void sendGoldBenefitsEmail(Customer customer) {
    Email email = new Email();
    email.setFrom("noreply@cleanapp.com");
    email.setTo(customer.getEmail());
    email.setSubject("Welcome to the Gold membership!");
    int discountPercentage = customer.getDiscountPercentage();
    email.setBody("Here are your perks: ... Enjoy your special discount of " + discountPercentage + "%");
    emailSender.sendEmail(email);
  }

  public void sendReevaluatePolicy(Customer customer, String reason) {
    Email email = new Email();
    email.setFrom("noreply@cleanapp.com");
    email.setTo("reps@cleanapp.com");
    email.setSubject("Customer " + customer.getName() + " policy has to be re-evaluated");
    email.setBody("Please review the policy due to : " + reason);
      emailSender.sendEmail(email);
  }

}
