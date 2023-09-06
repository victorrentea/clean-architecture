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

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {
  private final EmailSender emailSender;
  private final LdapApi ldapApi;

  public void sendWelcomeEmail(Customer customer, String userId) {
    // ⚠️ external DTO directly used inside my core logic
    //  TODO convert it into a new dedicated class - a Value Object (VO)
    User user = loadUserFromLdap(userId);

    // todo transform from LdapUserDto to User and use User below this line ------


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
    User user = loadUserFromLdap(userId);

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


// under this line, behold: SHIT !!!
  private static User convert(LdapUserDto userDto) {
    String fullName = userDto.getFname() + " " + userDto.getLname().toUpperCase();
    String username = userDto.getUn();
    if (username.startsWith("s")) username = "system";
    return new User(username, fullName, userDto.getWorkEmail());
  }

  private User loadUserFromLdap(String userId) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(userId.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for uid='" + userId + "' returned too many results: " + dtoList);
    }
    User user = convert(dtoList.get(0));
    return user;
  }

}
