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

  // ☮️ Core application logic - should be super clean 😇
  public void sendWelcomeEmail(Customer customer, String usernamePart) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(usernamePart.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for username='" + usernamePart + "' did not return a single result: " + dtoList);
    }

    LdapUserDto ldapUserDto = dtoList.get(0);

    String fullName = ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase();

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("""
            Welcome %s!
            Remember: you %s return orders.
            Sincerely,
            %s""".formatted(
            customer.getName(),
            customer.canReturnOrders() ? "can" : "cannot",
            fullName))
        .build();


    if (ldapUserDto.getWorkEmail() != null) { // what if forgotten?
      String contact = fullName + " <" + ldapUserDto.getWorkEmail().toLowerCase() + ">";
      email.getCc().add(contact);
    }

    emailSender.sendEmail(email);

    normalize(ldapUserDto);

    customer.setCreatedByUsername(ldapUserDto.getUn());
  }

  private void normalize(LdapUserDto ldapUserDto) {
    if (ldapUserDto.getUn().startsWith("s")) {
      ldapUserDto.setUn("system"); // ⚠️ dirty hack: replace any system user with 'system'
    }
  }

}
