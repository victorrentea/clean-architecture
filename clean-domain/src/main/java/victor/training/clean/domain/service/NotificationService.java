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
  private final UserFetcher ldapApiAdapter;

  // Core application logic, my Zen garden 🧘☯
  public void sendWelcomeEmail(Customer customer, String usernamePart) {
    User user = ldapApiAdapter.fetchUserByUsername(usernamePart);

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", Welcome to our clean app!\n" +
              "Sincerely, " + user.fullName())
        .build();

    if (user.email().isPresent()) {
      email.getCc().add(user.fullName() + " <" + user.email() + ">");
    }

    emailSender.sendEmail(email);

    customer.setCreatedByUsername(user.username());
  }

  public void sendGoldBenefitsEmail(Customer customer, String usernamePart) {
    User user = ldapApiAdapter.fetchUserByUsername(usernamePart);

    String returnOrdersStr = customer.canReturnOrders() ? "You are allowed to return orders\n" : "";

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome to our Gold membership!")
        .body(returnOrdersStr +
              "Yours sincerely, " + user.fullName())
        .build();

    if (user.email().isPresent()) {
      email.getCc().add(user.fullName() + " <" + user.email().get() + ">");
    }

    emailSender.sendEmail(email);
  }


}
