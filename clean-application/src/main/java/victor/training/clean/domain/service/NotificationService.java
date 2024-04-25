package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.IEmailSender;
import victor.training.clean.domain.ILdapUserAdapter;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.model.User;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {
  private final IEmailSender IEmailSender;
  private final ILdapUserAdapter adapter;

  // Core application logic, my Zen garden 🧘☯
  public void sendWelcomeEmail(Customer customer, String usernamePart) {
    // ⚠️ Scary, large external DTO TODO extract needed parts into a new dedicated Value Object
    User user = adapter.fetchUserFromLdap(usernamePart);

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", Welcome to our clean app!\n" +
              "Sincerely, " + user.fullName())
        .build();


    user.email()
        .map(emailAddress -> user.fullName() + " <" + emailAddress + ">")
        .ifPresent(email.getCc()::add);

    IEmailSender.sendEmail(email);

    customer.setCreatedByUsername(user.username());
  }


}
