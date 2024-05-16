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
@Service
public class NotificationService {
  private final EmailSender emailSender;
  private final LdapApi ldapApi;

  // Core application logic, my Zen garden 🧘☯
  public void sendWelcomeEmail(Customer customer, String usernamePart) {
    User user = fetchUser(usernamePart);

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", Welcome to our clean app!\n" +
              "Sincerely, " + user.fullName())
        .build();

    if (user.email().isPresent()) {
      email.getCc().add(user.fullName() + " <" + user.email().get() + ">");
    }

    emailSender.sendEmail(email);

    normalize(user);
    customer.setCreatedByUsername(user.username());
  }

  private User fetchUser(String usernamePart) {
    LdapUserDto ldapUserDto = fetchUserFromLdap(usernamePart);

    String fullName = ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase();
    if (ldapUserDto.getUn().startsWith("s")) {
      ldapUserDto.setUn("system"); // ⚠️ dirty hack: replace any system user with 'system'
    }

    User user = new User(ldapUserDto.getUn(), fullName, Optional.ofNullable(ldapUserDto.getWorkEmail()));
    return user;
  }

  private LdapUserDto fetchUserFromLdap(String usernamePart) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(usernamePart.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for username='" + usernamePart + "' did not return a single result: " + dtoList);
    }

    return dtoList.get(0);
  }

  private void normalize(User user) {

  }

  public void sendGoldBenefitsEmail(Customer customer, String usernamePart) {
    LdapUserDto userLdapDto = fetchUserFromLdap(usernamePart);

//    boolean canReturnOrders = customer.isGoldMember() || customer.getLegalEntityCode() == null;

    String returnOrdersStr = customer.canReturnOrders() ? "You are allowed to return orders\n" : "";

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome to our Gold membership!")
        .body(returnOrdersStr +
              "Yours sincerely, " + userLdapDto.getFname() + " " + userLdapDto.getLname().toUpperCase())
        .build();

    email.getCc().add(userLdapDto.getFname() + " " + userLdapDto.getLname().toUpperCase()
                      + " <" + userLdapDto.getWorkEmail() + ">");

    emailSender.sendEmail(email);
  }


}
