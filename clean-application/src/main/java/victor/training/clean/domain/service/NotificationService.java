package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.model.User;

@RequiredArgsConstructor
@Slf4j
@Service // imagine this is core logic
public class NotificationService {
  private final EmailSender emailSender;
  private final UserProvider userProvider;

  public void sendWelcomeEmail(Customer customer, String userId) {
    // ⚠️ external DTO directly used in my app logic TODO convert it into a new dedicated Value Object
    User user = userProvider.fetchUser(userId);

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", Welcome to our clean app!\n" +
              "Sincerely, " + user.fullName())
        .build();


    // ⚠️ Null check can be forgotten in other places; TODO return Optional<> from the getter
    if (user.email().isPresent()) {
      // ⚠️ the same logic repeats later TODO extract method in the new VO class
      if (user.email().get().toLowerCase().endsWith("@cleanapp.com")) {
        email.getCc().add(user.email().get());
      }
    }

    emailSender.sendEmail(email);


    customer.setCreatedByUsername(user.username());
  }

  public void sendGoldBenefitsEmail(Customer customer, String userId) {
    User userDto = userProvider.fetchUser(userId);

    int discountPercentage = 1;
    if (customer.isGoldMember()) {
      discountPercentage += 3;
    }

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome to our Gold membership!")
        .body("Please enjoy a special discount of " + discountPercentage + "%\n" +
              "Yours sincerely, " + userDto.fullName())
        .build();

    if (userDto.email().isPresent())
      if (userDto.email().get().toLowerCase().endsWith("@cleanapp.com")) {
        email.getCc().add(userDto.email().get());
      }

    emailSender.sendEmail(email);
  }

}

