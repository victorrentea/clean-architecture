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
  private final IEmailSender emailSender;
  private final UserFetcher userFetcher;

  // Core application logic, my Zen garden 🧘☯☮️
  public void sendWelcomeEmail(Customer customer, String usernamePart) {
    // getEnrichedUser - misleading name, it's not just getting, it's enriching
    // materializeUser
    // getUser = too "weak"
    // findUser = smells of Sprign Data
    // fetchNotificationUser
    // retrieveUser
    // fetchUser follows conventions already in the code
    // fetchUserFromLdap = "leaky abstraction", I don't care it's LDAP -@vladyslav
    User user = userFetcher.fetchUser(usernamePart);

    Email email = generateEmail(customer, user.fullName());

    user.asContact().ifPresent(contact->email.getCc().add(contact));

    emailSender.sendEmail(email);

    customer.setCreatedByUsername(user.username());
  }

  private static Email generateEmail(Customer customer, String fullName) {
    return Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", welcome! Sincerely, " + fullName)
        .build();
  }


  public void sendGoldBenefitsEmail(Customer customer, String usernamePart) {
    User user = userFetcher.fetchUser(usernamePart);

    String returnOrdersStr = customer.canReturnOrders() ? "You are allowed to return orders\n" : "";

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome to our Gold membership!")
        .body(returnOrdersStr +
              "Yours sincerely, " + user.fullName())
        .build();


    user.asContact().ifPresent(contact->email.getCc().add(contact));

    emailSender.sendEmail(email);
  }


}
