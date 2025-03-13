package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {
  private final IEmailSender IEmailSender;
  private final UserRetriever userRetriever;

  // Core application logic, my Zen garden 🧘☯☮️
  public void sendWelcomeEmail(Customer customer, String usernamePart) {
//    User user = getAndMap(usernamePart);
//    User user = returnZaUser(usernamePart);
//    User user = findUser(usernamePart); // DB 2ms
//    User user = getUser(usernamePart); // prea slab
    User user = userRetriever.retrieveUser(usernamePart);

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", welcome! Sincerely, " + user.fullName())
        .build();


    if (user.email().isPresent()) {
      // ⚠️ Logic repeated in other places TODO move logic to the new class
      String contact = user.fullName() + " <" + user.email().get().toLowerCase() + ">";
      email.getCc().add(contact);
    }

    IEmailSender.sendEmail(email);

    customer.setCreatedByUsername(user.username());
  }
}
