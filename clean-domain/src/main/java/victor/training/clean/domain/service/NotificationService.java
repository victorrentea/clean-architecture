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
  private final UserClient ldapUserApiAdapter;

  public void sendWelcomeEmail(Customer customer, String userId) {
    // ⚠️ external DTO directly used in my app logic TODO convert it into a new dedicated Value Object
    User ldapUserDto = ldapUserApiAdapter.fetchUser(userId);

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", Welcome to our clean app!\n" +
              "Sincerely, " + ldapUserDto.fullName())
        .build();


    // ⚠️ Null check can be forgotten in other places; TODO return Optional<> from the getter
    if (ldapUserDto.email().isPresent()) { // 🤞 I hope everywhere I check for null
      // ⚠️ the same logic repeats later TODO extract method in the new VO class
      if (ldapUserDto.email().get().toLowerCase().endsWith("@cleanapp.com")) {
        email.getCc().add(ldapUserDto.email().get());
      }
    }

    emailSender.sendEmail(email);


    // ⚠️ 'un' ?!! <- in my domain a User has a 'username' TODO use domain names in VO
    customer.setCreatedByUsername(ldapUserDto.username());
  }


  public void sendGoldBenefitsEmail(Customer customer, String userId) {
    User userDto = ldapUserApiAdapter.fetchUser(userId);

    int discountPercentage = customer.getDiscountPercentage();

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome to our Gold membership!")
        .body("Please enjoy a special discount of " + discountPercentage + "%\n" +
              "Yours sincerely, " + userDto.fullName())
        .build();

    if (userDto.email().isPresent()) {
      if (userDto.email().get().toLowerCase().endsWith("@cleanapp.com")) {
        email.getCc().add(userDto.email().get());
      }
    }

    emailSender.sendEmail(email);
  }

}
