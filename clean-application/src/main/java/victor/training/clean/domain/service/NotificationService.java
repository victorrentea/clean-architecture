package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.infra.UserFetcher;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {
  private final EmailSender emailSender;
  private final UserFetcher userFetcher;

  public void sendWelcomeEmail(Customer customer, String usernamePart) {
    User user = userFetcher.fetchUserFromLdap(usernamePart);

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", welcome! Sincerely, " + user.fullName())
        .build();

    user.email().ifPresent(c-> email.getCc().add(c));

    emailSender.sendEmail(email);
    customer.setCreatedByUsername(user.username());
  }

  public void sendGoldBenefitsEmail(Customer customer, String usernamePart) {
    User user = userFetcher.fetchUserFromLdap(usernamePart);

    String returnOrdersStr = customer.canReturnOrders() ? "You are allowed to return orders\n" : "";

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome to our Gold membership!")
        .body(returnOrdersStr +
              "Yours sincerely, " + user.fullName())
        .build();

    user.email().ifPresent(c-> email.getCc().add(c));

    emailSender.sendEmail(email);
  }

}
