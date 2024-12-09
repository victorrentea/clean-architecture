package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
@Service
public class NotificationService {
  private final EmailSender emailSender;
  private final LdapApi ldapApi;

  // Core application logic, my Zen garden 🧘☯☮️
  public void sendWelcomeEmail(Customer customer, String usernamePart) {
    // getEnrichedUser - misleading name, it's not just getting, it's enriching
    // materializeUser
    // getUser = too "weak"
    // findUser = smells of Sprign Data
    // fetchNotificationUser
    // retrieveUser
    // fetchUser follows conventions already in the code
    // fetchUserFromLdap = "leaky abstraction", I don't care it's LDAP -@vladyslav
    User user = fetchUser(usernamePart);

    Email email = generateEmail(customer, user.fullName());

    user.asContact().ifPresent(contact->email.getCc().add(contact));

    emailSender.sendEmail(email);

    customer.setCreatedByUsername(user.username());
  }

  private static Email generateEmail(Customer customer, String fullName) {
    return Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", welcome! Sincerely, " + fullName)
        .build();
  }

  // this method's complexity is more about mapping. shouldn't it be called mapUser()
  // every time you name SOMETHING, think of that name from its caller's perspective
  private User fetchUser(String usernamePart) {
    LdapUserDto ldapUserDto = fetchUserFromLdap(usernamePart);
    return convert(ldapUserDto);
  }

  private User convert(LdapUserDto ldapUserDto) {
    String fullName = ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase();
    normalize(ldapUserDto); // Temporal Coupling
    return new User(
        ldapUserDto.getUn(),
        fullName,
        Optional.ofNullable(ldapUserDto.getWorkEmail()).filter(StringUtils::isNoneBlank));
  }

  private LdapUserDto fetchUserFromLdap(String usernamePart) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(usernamePart.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for username='" + usernamePart + "' did not return a single result: " + dtoList);
    }

    return dtoList.get(0);
  }

  private void normalize(LdapUserDto ldapUserDto) {
    if (ldapUserDto.getUn().startsWith("s")) {
      ldapUserDto.setUn("system"); // ⚠️ dirty hack: replace any system user with 'system'
    }
  }

  public void sendGoldBenefitsEmail(Customer customer, String usernamePart) {
    User user = fetchUser(usernamePart);

    String returnOrdersStr = customer.canReturnOrders() ? "You are allowed to return orders\n" : "";

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome to our Gold membership!")
        .body(returnOrdersStr +
              "Yours sincerely, " + user.fullName())
        .build();


    user.asContact().ifPresent(contact->email.getCc().add(contact));

    emailSender.sendEmail(email);
  }


}
