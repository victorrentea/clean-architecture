package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.infra.UserFetcherService;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {
  private final EmailSender emailSender;
  private final UserFetcherService userFetcherService;

  // Core application logic, my Zen garden 🧘☯
  public void sendWelcomeEmail(Customer customer, String usernamePart) {
    User user = userFetcherService.fetchUser(customer, usernamePart);

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", Welcome to our clean app!\n" +
              "Sincerely, " + user.fullName())
        .build();


//    if (ldapUserDto.getWorkEmail() != null) {
    if (user.email().isPresent()) {
      email.getCc().add(user.fullName() + " <" + user.email().get() + ">");
    }

    emailSender.sendEmail(email);

    customer.setCreatedByUsername(user.username());
  }





}
