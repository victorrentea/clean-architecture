package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.infra.LdapAdapter;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {
  private final EmailSender emailSender;
  private final LdapAdapter ldapAdapter;

  public void sendWelcomeEmail(Customer customer, String userId) {
    User user = ldapAdapter.lookupUser(userId);

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", Welcome to our clean app!\n" +
              "Sincerely, " + user.fullName())
        .build();


    if (user.email().isPresent()) {
      // ⚠️ the same logic repeats later TODO extract method in the new VO class
      if (user.hasInternalEmail()) {
        email.getCc().add(user.email().get());
      }
    }

    emailSender.sendEmail(email);

    customer.setCreatedByUsername(user.username());
  }


}
