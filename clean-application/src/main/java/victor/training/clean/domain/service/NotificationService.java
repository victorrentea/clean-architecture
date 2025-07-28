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
  private final EmailSenderInterface emailSenderInterface;
  private final UserFetcher userFetcher;

  // Core application logic, my Zen garden 🧘☯☮️
  public void sendWelcomeEmail(Customer customer, String usernamePart) {
    User user = userFetcher.fetchUser(usernamePart);

    boolean canReturnOrders = customer.isGoldMember() || customer.getLegalEntityCode().isEmpty();

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
            canReturnOrders ? "can" : "cannot",
            user.fullName()))
        .build();


    if (user.email().isPresent()) { // what if forgotten?
      String contact = user.fullName() + " <" + user.email().get().toLowerCase() + ">";
      email.getCc().add(contact);
    }

    emailSenderInterface.sendEmail(email);

    customer.setCreatedByUsername(user.username());
  }
}

