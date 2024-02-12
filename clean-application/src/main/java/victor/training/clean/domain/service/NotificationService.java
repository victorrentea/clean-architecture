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

  public void sendWelcomeEmail(Customer customer, String userId) {
    User user = foo(userId);


    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", Welcome to our clean app!\n" +
              "Sincerely, " + user.fullName())
        .build();


    // ⚠️ Null check can be forgotten in other places; TODO return Optional<> from the getter
    if (user.workEmail().isPresent()) {
      // ⚠️ the same logic repeats later TODO extract method in the new VO class
      if (user.isMyDomain()) {
        email.getCc().add(user.workEmail().get());
      }
    }

    emailSender.sendEmail(email);

//    // ⚠️ TEMPORAL COUPLING: tests fail if you swap the next 2 lines TODO use immutable VO
//    normalize(user);

    // ⚠️ 'un' ?!! <- in my domain a User has a 'username' TODO use domain names in VO
    customer.setCreatedByUsername(user.username());
  }

  private User foo(String userId) {
    // ⚠️ external DTO directly used in my app logic TODO convert it into a new dedicated Value Object
    LdapUserDto ldapUserDto = fetchUserDetailsFromLdap(userId);
    // ⚠️ data mapping mixed with my core domain logic TODO pull it earlier
    String fullName = ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase();
    String username;
    if (ldapUserDto.getUn().startsWith("s")) {// eg 's12051' is a system user
      username = "system"; // ⚠️ dirty hack
    } else {
      username = ldapUserDto.getUn();
    }
    return new User(
        username,
        fullName,
        Optional.ofNullable(ldapUserDto.getWorkEmail()));
  }

  private LdapUserDto fetchUserDetailsFromLdap(String userId) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(userId.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for uid='" + userId + "' returned too many results: " + dtoList);
    }

    return dtoList.get(0);
  }

  public void sendGoldBenefitsEmail(Customer customer, String userId) {
    User user = foo(userId);

    int discountPercentage = customer.discountPercentage();

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome to our Gold membership!")
        .body("Please enjoy a special discount of " + discountPercentage + "%\n" +
              "Yours sincerely, " + user.fullName())
        .build();

    user.workEmail()
        .filter(workEmail -> user.isMyDomain())
        .ifPresent(workEmail -> email.getCc().add(workEmail));

    emailSender.sendEmail(email);
  }


}
