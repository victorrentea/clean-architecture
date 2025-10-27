package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {
  private final EmailSender emailSender;
  private final LdapApiAdapter ldapApiAdapter;

  // ☮️ Core application logic - should be super clean 😇
  public void sendWelcomeEmail(Customer customer, String usernamePart) {
    User user = ldapApiAdapter.fetchUserByName(usernamePart);

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


    if (user.email() != null) {
      String contact = user.fullName() + " <" + user.email().toLowerCase() + ">";
      email.getCc().add(contact);
    }

    emailSender.sendEmail(email);

    customer.setCreatedByUsername(user.username());
  }


}
