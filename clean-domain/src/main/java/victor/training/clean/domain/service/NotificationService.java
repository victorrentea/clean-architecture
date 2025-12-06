package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.User;
import victor.training.clean.domain.port.EmailSender;
import victor.training.clean.domain.repo.UserRepository;
import victor.training.clean.domain.model.Email;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService implements NotificationServiceInterface {
  private final EmailSender emailSender;
  private final UserRepository userRepository;

  // ☮️ Core application logic - should be super clean 😇
  @Override
  public void sendWelcomeEmail(Customer customer, String usernamePart) {
    User user = userRepository.findSingleUserByUsernamePart(usernamePart);
    String fullName = user.fullName();

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
            fullName))
        .build();

    if (user.workEmail().isPresent()) { // Opt
      String contact = fullName + " <" + user.workEmail().get().toLowerCase() + ">";
      email.getCc().add(contact);
    }

    emailSender.sendEmail(email);

    customer.setCreatedByUsername(user.username());
  }

}
