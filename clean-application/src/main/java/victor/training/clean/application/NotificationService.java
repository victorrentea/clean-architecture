package victor.training.clean.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.infra.EmailSender;

@RequiredArgsConstructor
@Service
public class NotificationService {
  private final EmailSender emailSender;

  public void sendWelcomeEmail(Customer customer) {
    Email email = new Email();
    email.setFrom("noreply@cleanapp.com");
    email.setTo(customer.getEmail());
    email.setSubject("Account created for");
    email.setBody("Welcome to our world, "+ customer.getName()+". You'll like it! Sincerely, Team");
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
}
