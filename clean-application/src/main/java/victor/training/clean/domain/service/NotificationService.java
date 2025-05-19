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

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {
  private final EmailSender emailSender;
  private final LdapApi ldapApi;

  //
  // + class UserApi {
  //    getUser(username):Optional<domain.model.User{strictly what we need}>?
  // }
  // - 🔐narrow down the name of this class to "EmailService" since it's only doing emails
  //  to discourage my colleagues (and me) to add MORE to it. it's 120 lines already.
  // - implementing a new interface perhaps IN THE FUTURE ... <OVER ENGINEERING?>

  // Core application logic, my Zen garden 🧘☯☮️
  public void sendWelcomeEmail(Customer customer, String usernamePart) {
    // ⚠️ Scary, large external DTO TODO extract needed parts into a new dedicated Value Object
    LdapUserDto ldapUserDto = fetchUserFromLdap(usernamePart);

    // ⚠️ Data mapping mixed with core logic TODO pull it earlier
    String fullName = ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase();

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", welcome! Sincerely, " + fullName)
        .build();


    // ⚠️ Unguarded nullable fields can cause NPE in other places TODO return Optional<> from getter
    if (ldapUserDto.getWorkEmail() != null) {
      // ⚠️ Logic repeated in other places TODO move logic to the new class
      String contact = fullName + " <" + ldapUserDto.getWorkEmail().toLowerCase() + ">";
      email.getCc().add(contact);
    }

    emailSender.sendEmail(email);

    // ⚠️ Swap this line with next one to cause a bug (=TEMPORAL COUPLING) TODO make immutable💚
    normalize(ldapUserDto);

    // ⚠️ 'un' = bad name TODO in my ubiquitous language 'un' means 'username'
    customer.setCreatedByUsername(ldapUserDto.getUn());
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
    LdapUserDto userLdapDto = fetchUserFromLdap(usernamePart);

    String returnOrdersStr = customer.canReturnOrders() ? "You are allowed to return orders\n" : "";

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome to our Gold membership!")
        .body(returnOrdersStr +
              "Yours sincerely, " + userLdapDto.getFname() + " " + userLdapDto.getLname().toUpperCase())
        .build();

    String contact = userLdapDto.getFname() + " " + userLdapDto.getLname().toUpperCase()
               + " <" + userLdapDto.getWorkEmail().toLowerCase() + ">";
    email.getCc().add(contact);

    emailSender.sendEmail(email);
  }


}
