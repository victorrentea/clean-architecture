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
    User user = fetchUser(userId);

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", Welcome to our clean app!\n" +
              "Sincerely, " + user.fullName())
        .build();

    emailSender.sendEmail(email);

    // ⚠️ TEMPORAL COUPLING: tests fail if you swap the next 2 lines TODO use immutable VO

//    normalize(ldapUserDto);
    customer.setCreatedByUsername(user.username());
  }

  private User fetchUser(String userId) {
    // ⚠️ external DTO directly used in my app logic TODO convert it into a new dedicated Value Object
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(userId.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for uid='" + userId + "' returned too many results: " + dtoList);
    }

    LdapUserDto ldapUserDto = dtoList.get(0);

    String username = ldapUserDto.getUn();
    if (username.startsWith("s")) {// eg 's12051' is a system user
      username = "system"; // ⚠️ dirty hack
    }

    User user = new User(username,
        ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase(),
        ldapUserDto.getWorkEmail());
    return user;
  }

  private void normalize(LdapUserDto dto) {
    if (dto.getUn().startsWith("s")) {// eg 's12051' is a system user
      dto.setUn("system"); // ⚠️ dirty hack
    }
  }


}
