package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service // imagine this is core logic
public class NotificationService {
  private final EmailSender emailSender;
  private final LdapApi ldapApi;

  public void sendWelcomeEmail(Customer customer, String userId) {
    // ⚠️ external DTO directly used in my app logic TODO convert it into a new dedicated Value Object
    User user = fetchUserDetailsFromLdap(userId);

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

  private User fetchUserDetailsFromLdap(String userId) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(userId.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for uid='" + userId + "' returned too many results: " + dtoList);
    }

    // mapping logic ----
    LdapUserDto dto = dtoList.get(0);
    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase();
    String username = dto.getUn();
    if (username.startsWith("s")) {// eg 's12051' is a system user
      username = "system"; // ⚠️ dirty hack
    }
//    String email = dto.getWorkEmail() != null ? dto.getWorkEmail() : "";
    User user = new User(username, fullName, Optional.ofNullable(dto.getWorkEmail()));
    // DO I allow in my app to receive a User with a null workemail?
    // NO=> throw here!
    // YES=> default to "" (A) = Null Object Pattern (GoF)
    // YES=> wrap it into an Optional<> (B) < 90% choose this
    return user;
  }

  public void sendGoldBenefitsEmail(Customer customer, String userId) {
    User userDto = fetchUserDetailsFromLdap(userId);

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
