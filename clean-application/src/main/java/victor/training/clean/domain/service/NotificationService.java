package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.infra.LdapClient;
import victor.training.clean.infra.LdapUserDto;

@RequiredArgsConstructor
@Slf4j
@Service
//peace, love, empathy, respect, responsibility, ultimate clean code!!
// best of my code, my Zen garden 🧘☯
public class NotificationService {
  private final EmailSender emailSender;
  private final LdapClient ldapClient;

  public void sendWelcomeEmail(Customer customer, String usernamePart) {
    // ⚠️ Scary, large external DTO TODO extract needed parts into a new dedicated Value Object
    User user = ldapClient.fetchUserByUsername(usernamePart);

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", Welcome to our clean app!\n" +
              "Sincerely, " + user.fullName())
        .build();


    // ⚠️ Unguarded nullable fields (causing NPE in other places) TODO return Optional<> from getter
    if (user.email().isPresent()) {
      email.getCc().add(user.fullName() + " <" + user.email().get() + ">");
    }

    emailSender.sendEmail(email);

    //side effect inside causing mysterious TEMPORAL COUPLING

    customer.setCreatedByUsername(user.username());
  }
}

