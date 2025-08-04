package victor.training.clean.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.core.model.Customer;
import victor.training.clean.core.model.Email;
import victor.training.clean.core.model.User;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {
  private final EmailSenderPort emailSenderPort;
  private final UserInventoryPort userInventoryPort;

  // Core application logic, my Zen garden 🧘☯☮️
  public void sendWelcomeEmail(Customer customer, String usernamePart) {
    User user = userInventoryPort.fetchUser(usernamePart);

    // 🧘 my business logic

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


    user.asContact().ifPresent(email.getCc()::add);

    emailSenderPort.sendEmail(email);

    customer.setCreatedByUsername(user.username());
  }
}

