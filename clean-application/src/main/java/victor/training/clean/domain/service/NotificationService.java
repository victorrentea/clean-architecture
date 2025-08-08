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
  private final UserFetcherPort userFetcher;

  // Core application logic, my Zen garden 🧘☯☮️
  public void sendWelcomeEmail(Customer customer, String usernamePart) {
    User user = userFetcher.fetchUser(usernamePart);

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

//    if (ldapUserDto.getWorkEmail() != null) {
//      String contact = fullName + " <" + ldapUserDto.getWorkEmail().toLowerCase() + ">";
//      email.getCc().add(contact);
//    }
    user.email().ifPresent(e -> {
      String contact = user.fullName() + " <" + e.toLowerCase() + ">";
      email.getCc().add(contact);
      log.info("Adding CC to email: {}", contact);
    });

    emailSender.sendEmail(email);

    customer.setCreatedByUsername(user.username());
  }
}

