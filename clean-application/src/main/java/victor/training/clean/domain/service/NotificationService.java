package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.infra.LdapApi;


@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {
  private final EmailSender emailSender;
  private final LdapApi ldapApi;
  private final LdapUserApiAdapter ldapUserApiAdapter;

  // Core application logic, my Zen garden 🧘☯☮️
  public void sendWelcomeEmail(Customer customer, String usernamePart) {
    var user = ldapUserApiAdapter.fetchUser(usernamePart);
    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", welcome! Sincerely, " + user.fullName())
        .build();

    if (user.email().isPresent()) {
      String contact = user.fullName() + " <" + user.email().get().toLowerCase() + ">";
      email.getCc().add(contact);
    }

    emailSender.sendEmail(email);

    customer.setCreatedByUsername(user.username());
  }

}

