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

  public void sendWelcomeEmail(Customer customer, String username) {
    // ⚠️ external DTO directly used in my app logic TODO convert it into a new dedicated Value Object
    User user = fetchUser(username);

    // ⚠️ data mapping mixed with my core domain logic TODO pull it earlier

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

//    normalize(ldapUserDto);
    customer.setCreatedByUsername(user.username());
  }

  private User fetchUser(String username) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(username.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for uid='" + username + "' returned too many results: " + dtoList);
    }

    LdapUserDto ldapUserDto = dtoList.get(0);

    return new User(
        ldapUserDto.getUn().startsWith("s")? "system" : ldapUserDto.getUn(),
        Optional.ofNullable(ldapUserDto.getWorkEmail()),
        ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase());
  }

}
