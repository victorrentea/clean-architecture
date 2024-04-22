package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.model.User;
import victor.training.clean.domain.model.Username;
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
    // ⚠️ Scary, large external DTO TODO extract needed parts into a new dedicated Value Object
    LdapUserDto ldapUserDto = fetchUserFromLdap(usernamePart);

    // is there any relevant domain difference between "" and null email ? NO: never see an "" in your core logic.
    if (ldapUserDto.getWorkEmail()!=null && ldapUserDto.getWorkEmail().equals("")) {
      ldapUserDto.setWorkEmail(null);
    }

    String username = ldapUserDto.getUn();
    if ("".equals(username)) {
      username = null;
    } else if (username != null) {
      if (username.startsWith("s")) {
        username="system";
      }
    }

    User user = new User(
        new Username(username),
        ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase(),
        Optional.ofNullable(ldapUserDto.getWorkEmail()));


    // ---------------- below this line, there should be no sign of Ldap garbage




    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", Welcome to our clean app!\n" +
              "Sincerely, " + user.fullName())
        .build();


    if (user.email().isPresent()) {
      // ⚠️ Logic repeats in other places TODO push logic in my new class
      email.getCc().add(user.fullName() + " <" + user.email().get() + ">");
    }

    emailSender.sendEmail(email);


    customer.setCreatedByUsername(user.username());
  }

  private LdapUserDto fetchUserFromLdap(String usernamePart) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(usernamePart.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for username='" + usernamePart + "' did not return a single result: " + dtoList);
    }

    return dtoList.get(0);
  }

  private void normalize(LdapUserDto dto) {
    if (dto.getUn().startsWith("s")) {
      dto.setUn("system"); // ⚠️ dirty hack: replace any system user with 'system'
    }
  }




}
