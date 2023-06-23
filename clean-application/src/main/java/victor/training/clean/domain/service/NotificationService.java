package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;

@RequiredArgsConstructor
@Service
public class NotificationService {
  private final EmailService emailSender;


  public void sendWelcomeEmail(Customer customer) {
    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Account created for")
        .body("Welcome to our world, " + customer.getName() + ". You'll like it! Sincerely, Team")
        .build();
    emailSender.sendEmail(email);
  }

  public void sendReevaluatePolicy(Customer customer, String reason) {
    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to("reps@cleanapp.com")
        .subject("Customer " + customer.getName() + " policy has to be re-evaluated")
        .body("Please review the policy due to : " + reason)
        .build();
    emailSender.sendEmail(email);
  }

}
