package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.LdapUserDto;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {
  private final EmailSenderPort emailSender;
  private final UserFetcher userFetcher;

  // Core application logic, my Zen garden 🧘☯☮️
  public void sendWelcomeEmail(Customer customer, String usernamePart) {
    User user = userFetcher.fetchUser(usernamePart);
    LdapUserDto dtoRau;

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


    // ⚠️ Unguarded nullable fields can cause NPE in other places TODO return Optional<> from getter
    if (user.email().isPresent()) { // what if forgotten?
      // ⚠️ Logic repeated in other places TODO move logic to the new class
      String contact = user.fullName() + " <" + user.email().get().toLowerCase() + ">";
      email.getCc().add(contact);
    }

    emailSender.sendEmail(email);

    customer.setCreatedByUsername(user.username());
  }
}
