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
  private final IEmailSender emailSender;
  private final UserProvider userProvider; // leaking abstraction

  public void sendWelcomeEmail(Customer customer, String userId) {
    User user = userProvider.retrieveUser(userId);

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", Welcome to our clean app!\n" +
              "Sincerely, " + user.name())
        .build();

    if (user.email().isPresent()) {
      if (user.isInternalEmail()) {
        email.getCc().add(user.email().orElseThrow());
      }
    }

    emailSender.sendEmail(email);

    customer.setCreatedByUsername(user.username());
  }


  private void normalize(User dto) {

  }

  public void sendGoldBenefitsEmail(Customer customer, String userId) {
  }


}
