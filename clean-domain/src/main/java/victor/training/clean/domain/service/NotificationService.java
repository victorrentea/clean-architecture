package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.service.UserLookupService.User;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {
  private final EmailService emailService;
  private final UserLookupService userLookupService; // IoC : go and instantiate and retrieve yourself, but the framework gives you back. Technically, that is equal to dependency injection.

  // ☮️ Core application logic - should be super clean 😇
  public void sendWelcomeEmail(Customer customer, String usernamePart) {
    User user = userLookupService.lookupUser(usernamePart);

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

    if (user.workEmail() != null) {
      String cc = user.fullName() + " <" + user.workEmail() + ">";
      email.getCc().add(cc);
    }

    emailService.sendEmail(email);

    customer.setCreatedByUsername(user.username());
  }
}
