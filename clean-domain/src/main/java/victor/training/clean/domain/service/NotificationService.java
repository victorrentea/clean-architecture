package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.model.User;

// 1. map the LdapUserDto to a NEW domain data structure in my 'domain.model'
// 2. move the infrastructure logic into the 'infra' package (as a class)
// 3. interface on which the domain to depend
@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {
  private final IEmailSender emailSender;
  private final UserFetcher userFetcher;

  // Core application logic, my Zen garden 🧘☯
  public void sendWelcomeEmail(Customer customer, String usernamePart) {
    User user = userFetcher.fetchUser(usernamePart);

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", Welcome to our clean app!\n" +
              "Sincerely, " + user.fullName())
        .build();


    if (user.email().isPresent()) {
      email.getCc().add(user);
    }

    emailSender.sendEmail(email);

    customer.setCreatedByUsername(user.username()); // TODO BAD PRACTICE:
    // side-effect not expected by the name of this method 'sendWelcomeEmail'
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

   if (user.email().isPresent()) {
      email.getCc().add(user);
    }


    emailSender.sendEmail(email);
  }


}
