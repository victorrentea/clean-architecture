package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.model.User;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {
  private final EmailSender emailSender;
  private final ExternalUserProvider userProvider; // "Leaking ABSTRACTION" - PR review comment from an architect with a fine nose
  // abstraction = hide details
  // why do I have to know here that the users are coming from LDAP ?!

  public void sendWelcomeEmail(Customer customer, String userId) {
    // ⚠️ external DTO directly used inside my core logic
    //  TODO convert it into a new dedicated class - a Value Object (VO)
    User user = userProvider.loadUserFromLdap(userId);

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", Welcome to our clean app!\n" +
            "Sincerely, " + user.getFullName())
        .build();

    boolean addToCC = user.hasInternalEmail();

    if (addToCC) {
      email.getCc().add(user.getEmail().get());
    }

    emailSender.sendEmail(email);

    // ⚠️ 'un' ?!! <- in my domain a User has a 'username' TODO use domain names in VO
    customer.setCreatedByUsername(user.getUsername());
  }

  public void sendGoldBenefitsEmail(Customer customer, String userId) {
    User user = userProvider.loadUserFromLdap(userId);

    int discountPercentage = customer.getDiscountPercentage();

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome to our Gold membership!")
        .body("Please enjoy a special discount of " + discountPercentage + "%\n" +
            "Yours sincerely, " + user.getFullName())
        .build();

    boolean addToCC = user.hasInternalEmail();

    if (addToCC) {
      email.getCc().add(user.getEmail().get());
    }

    emailSender.sendEmail(email);
  }
}
