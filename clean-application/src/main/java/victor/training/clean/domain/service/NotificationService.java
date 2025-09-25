package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.infra.LdapClient;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {
  private final EmailSender emailSender;
  private final LdapClient ldapClient;

  // Core application logic, my Zen garden 🧘☯☮️
  public void sendWelcomeEmail(Customer customer, String usernamePart) {
    User user = ldapClient.fetchUser(usernamePart);
    // TODO map external DTO to a Value Object 'User' of mine => in domain.model
    // 💩 infrastructure (external complexity)
    // ----------- architecture is the art of drawing lines
    // ✌️ domain core logic kept clean

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

    if (user.email().isPresent()) { // what if forgotten?
      String contact = user.fullName() + " <" + user.email().get().toLowerCase() + ">";
      email.getCc().add(contact);
    }

    emailSender.sendEmail(email);

    customer.setCreatedByUsername(user.username());
  }


}
