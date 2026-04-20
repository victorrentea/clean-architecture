package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.model.User;
import victor.training.clean.domain.repo.UserDirectory;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {
  private final IEmailSender emailSender;
  private final UserDirectory userDirectory;

  // ☮️ Core application logic - should be super clean 😇
  public void sendWelcomeEmail(Customer customer, String usernamePart) {
    User user = userDirectory.findByUsernamePart(usernamePart);

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("""
            Welcome %s!
            Remember: you %s return orders.
            Sincerely,
            %s""".formatted(
            customer.getName(),
            customer.canReturnOrders() ? "can" : "cannot",
            user.fullName()))
        .build();

    user.email().ifPresent(workEmail ->
        email.getCc().add(user.fullName() + " <" + workEmail.toLowerCase() + ">"));

    emailSender.sendEmail(email);

    customer.setCreatedByUsername(user.username());
  }

}
