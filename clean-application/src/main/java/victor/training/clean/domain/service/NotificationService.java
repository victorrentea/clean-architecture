package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.LdapClientImpl;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {
  private final EmailSender emailSender;
  private final LdapClientImpl ldapClient;

  public void sendWelcomeEmail(Customer customer, String usernamePart) {
    User user = ldapClient.search(usernamePart);
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

    // Q1: is it VALID be null? => here: YES
    // Q2: return an Optional
    if (user.workEmail().isPresent()) {
      String contact = fullName + " <" + user.workEmail().get().toLowerCase() + ">";
      email.getCc().add(contact);
    }

    emailSender.sendEmail(email);

    customer.setCreatedByUsername(user.username());
  }


}
