package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.model.User;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {
  private final EmailSender emailSender;
  private final UserFetcher userFetcher;

  // Core application logic, my Zen garden 🧘☯
  public void sendWelcomeEmail(Customer customer, String usernamePart) {
    User user = userFetcher.fetch(usernamePart);
    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", Welcome to our clean app!\n" +
              "Sincerely, " + user.fullName())
        .build();


    if (user.email().isPresent()) {
      email.getCc().add(user.fullName() + " <" + user.email().get() + ">");
    }

    emailSender.sendEmail(email);

    customer.setCreatedByUsername(user.username());
  }



  public void sendGoldBenefitsEmail(Customer customer, String usernamePart) {
    User userLdapDto = userFetcher.fetch(usernamePart);

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome to our Gold membership!")
        .body("Please enjoy a special discount of " + customer.getDiscountPercentage() + "%\n" +
              "Yours sincerely, " + userLdapDto.fullName())
        .build();

    if (userLdapDto.email().isPresent()) {
      email.getCc().add(userLdapDto.fullName() + " <" + userLdapDto.email().get() + ">");
    }

    emailSender.sendEmail(email);
  }


}
