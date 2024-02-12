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
  private final IEmailSender emailSender;
  private final ExternalUserProvider externalUserProvider;


  public void sendWelcomeEmail(Customer customer, String userId) {
    User user = externalUserProvider.fetchUser(userId);

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", Welcome to our clean app!\n" +
              "Sincerely, " + user.fullName())
        .build();


    // ⚠️ Null check can be forgotten in other places; TODO return Optional<> from the getter
    if (user.workEmail().isPresent()) {
      // ⚠️ the same logic repeats later TODO extract method in the new VO class
      if (user.isMyDomain()) {
        email.getCc().add(user.workEmail().get());
      }
    }

    emailSender.sendEmail(email);

//    // ⚠️ TEMPORAL COUPLING: tests fail if you swap the next 2 lines TODO use immutable VO
//    normalize(user);

    // ⚠️ 'un' ?!! <- in my domain a User has a 'username' TODO use domain names in VO
    customer.setCreatedByUsername(user.username());
  }

  public void sendGoldBenefitsEmail(Customer customer, String userId) {
    User user = externalUserProvider.fetchUser(userId);

    int discountPercentage = customer.discountPercentage();

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome to our Gold membership!")
        .body("Please enjoy a special discount of " + discountPercentage + "%\n" +
              "Yours sincerely, " + user.fullName())
        .build();

    user.workEmail()
        .filter(workEmail -> user.isMyDomain())
        .ifPresent(workEmail -> email.getCc().add(workEmail));

    emailSender.sendEmail(email);
  }


}
