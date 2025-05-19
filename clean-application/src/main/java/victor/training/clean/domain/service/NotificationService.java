package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.infra.RetrieveUserService;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {
  private final EmailSender emailSender;
  private final RetrieveUserService retrieveUserService;

  // + class UserApi {
  //    getUser(username):Optional<domain.model.User{strictly what we need}>?
  // }
  // - 🔐narrow down the name of this class to "EmailService" since it's only doing emails
  //  to discourage my colleagues (and me) to add MORE to it. it's 120 lines already.
  // - implementing a new interface perhaps IN THE FUTURE ... <OVER ENGINEERING?>


  // Core application logic, my Zen garden 🧘☯☮️
  public void sendWelcomeEmail(Customer customer, String creatorUsername) {
    User user = retrieveUserService.retrieve(creatorUsername);

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

    emailSender.sendEmail(email);

    customer.setCreatedByUsername(user.username());
  }

}

// 🧘 domain = my core complexity
//  ===========
// 💩 infra details my app just HAS to do

