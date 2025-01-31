package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.model.User;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {
  private final IEmailSender IEmailSender;
  private final UserFetcher UserFetcher;

  public void sendWelcomeEmail(Customer customer, String usernamePart) {
    User user = UserFetcher.fetchUser(usernamePart);

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", welcome! Sincerely, " + user.name())
        .build();

    user.asContact().ifPresent(email.getCc()::add);

    IEmailSender.sendEmail(email);

    // ⚠️ Swap this line with next one to cause a bug (=TEMPORAL COUPLING) TODO make immutable💚

    customer.setCreatedByUsername(user.username());
  }

  // 💖
  public void sendGoldBenefitsEmail(Customer customer, String usernamePart) {
    User user = UserFetcher.fetchUser(usernamePart);

    String s = "Abc";

//    String s2 = StringUtils.capitalize(s);

    String returnOrdersStr = customer.canReturnOrders() ? "You are allowed to return orders\n" : "";

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome to our Gold membership!")
        .body(returnOrdersStr +
              "Yours sincerely, " + user.name())
        .build();

    user.asContact().ifPresent(email.getCc()::add);

    IEmailSender.sendEmail(email);
  }
}

