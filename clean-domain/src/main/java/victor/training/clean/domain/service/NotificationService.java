package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.repo.UserRepo;
import victor.training.clean.domain.model.User;

@RequiredArgsConstructor
@Slf4j
//@Service
// @DomainService -your own annot + kung-fu to make Spring scan these
public class NotificationService {
  private final EmailSender emailSender;
  private final UserRepo userRepo;

  public void sendWelcomeEmail(Customer customer, String usernamePart) {
    User user = userRepo.search(usernamePart);

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

    // Q1: is it VALID be null? => here: YES
    // Q2: return an Optional
    if (user.workEmail().isPresent()) {
      String contact = user.fullName() + " <" + user.workEmail().get().toLowerCase() + ">";
      email.getCc().add(contact);
    }

    emailSender.sendEmail(email);

    customer.setCreatedByUsername(user.username());
  }


}
