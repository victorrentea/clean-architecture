package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.port.EmailSender;
import victor.training.clean.domain.port.UserDirectory;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {
  private final EmailSender emailSender;
  private final UserDirectory userDirectory;

  public void sendWelcomeEmail(Customer customer, String usernamePart) {
    var user = userDirectory.findSingleByUsernamePart(usernamePart);
    String fullName = user.fullName();
//new Customer().setEmail("a").setCountry("a");
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


    user.workEmail().ifPresent(workEmail -> {
      String contact = fullName + " <" + workEmail + ">";
      email.getCc().add(contact);
    });

    emailSender.sendEmail(email);

    customer.setCreatedByUsername(user.username());
  }

}
