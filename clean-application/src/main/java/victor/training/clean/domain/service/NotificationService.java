package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;
import java.util.Optional;

record User(String username, String name, Optional<String> email) {
  public static final String MY_DOMAIN = "cleanapp.com";

  public boolean isInternalEmail() { // ubiquituous language. biz can understand this 😇
    return email.map(e -> e.toLowerCase().endsWith("@" + MY_DOMAIN)).orElse(false);
  }
}

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {
  private final EmailSender emailSender;
  private final LdapApi ldapApi;

  public void sendWelcomeEmail(Customer customer, String userId) {
    // ⚠️ external DTO directly used inside my core logic
    //  TODO convert it into a new dedicated class - a Value Object (VO)
    User user = loadUserFromLdap________2(userId);

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", Welcome to our clean app!\n" +
              "Sincerely, " + user.name())
        .build();


    // ⚠️ Null check can be forgotten in other places; TODO return Optional<> from the getter
    if (user.email().isEmpty()) {
      // ⚠️ the same logic repeats later TODO extract method in the new VO class
      if (user.isInternalEmail()) {
        email.getCc().add(user.email().orElseThrow());
      }
    }

    emailSender.sendEmail(email);

    customer.setCreatedByUsername(user.username());
  }

  private User loadUserFromLdap________2(String userId) {
    LdapUserDto dto = loadUserFromLdap(userId);
    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase();
    if (dto.getUn().startsWith("s")) {// eg s12051 - a system user
      dto.setUn("system"); // ⚠️ dirty hack
    }
    User user = new User(dto.getUn(), fullName, Optional.ofNullable(dto.getWorkEmail()));
    return user;
  }

  private LdapUserDto loadUserFromLdap(String userId) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(userId.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for uid='" + userId + "' returned too many results: " + dtoList);
    }

    return dtoList.get(0);
  }

  private void normalize(User dto) {

  }

  public void sendGoldBenefitsEmail(Customer customer, String userId) {
    LdapUserDto userDto = loadUserFromLdap(userId);

    int discountPercentage = 1;
    if (customer.isGoldMember()) {
      discountPercentage += 3;
    }

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome to our Gold membership!")
        .body("Please enjoy a special discount of " + discountPercentage + "%\n" +
              "Yours sincerely, " + userDto.getFname() + " " + userDto.getLname().toUpperCase())
        .build();

    if (userDto.getWorkEmail().toLowerCase().endsWith("@cleanapp.com")) {
      email.getCc().add(userDto.getWorkEmail());
    }

    emailSender.sendEmail(email);
  }


}
