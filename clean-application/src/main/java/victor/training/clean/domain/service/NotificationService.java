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
// ATENTIE: intram (cu respect) in cod de domeniu - asta tre sa fie cel mai curat din aplicatie
public class NotificationService {
  private final EmailSender emailSender;
  private final LdapApi ldapApi;

  public void sendWelcomeEmail(Customer customer, String userId) {
    // ⚠️ external DTO directly used inside my core logic
    //  TODO convert it into a new dedicated class - a Value Object (VO)
    User user = loadUserFromLdap(userId);

    // ⚠️ data mapping mixed with my core domain logic TODO pull it earlier
    String fullName = user.fullName();

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", Welcome to our clean app!\n" +
              "Sincerely, " + fullName)
        .build();

    // ⚠️ Null check can be forgotten in other places; TODO return Optional<> from the getter
    if (user.email().isPresent()) {
      // ⚠️ the same logic repeats later TODO extract method in the new VO class
      if (user.isCleanApp()) {
        email.getCc().add(user.email().get());
      }
    }

    emailSender.sendEmail(email);

    // ⚠️ TEMPORAL COUPLING: tests fail if you swap the next 2 lines TODO use immutable VO
//    normalize(user);

    customer.setCreatedByUsername(user.username());
  }

  private User loadUserFromLdap(String userId) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(userId.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for uid='" + userId + "' returned too many results: " + dtoList);
    }

    LdapUserDto dto = dtoList.get(0);

    String username = dto.getUn();
    if (username.startsWith("s")) {// eg s12051 - a system user
      username= "system"; // ⚠️ dirty hack
    }
//    if (dto.getWorkEmail() == null) {
//      throw new IllegalArgumentException();
//    }
    return new User(
        username,
        dto.getFname() + " " + dto.getLname().toUpperCase(),
        Optional.ofNullable(dto.getWorkEmail()
        ));
  }

//  private void normalize(User dto) {
//    if (dto.getUn().startsWith("s")) {// eg s12051 - a system user
//      dto.setUn("system"); // ⚠️ dirty hack
//    }
//  }

  public void sendGoldBenefitsEmail(Customer customer, String userId) {
    User user = loadUserFromLdap(userId);

    int discountPercentage = customer.getDiscountPercentage();

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome to our Gold membership!")
        .body("Please enjoy a special discount of " + discountPercentage + "%\n" +
              "Yours sincerely, " + user.fullName())
        .build();

    if (user.isCleanApp()) {
      email.getCc().add(user.email().get());
    }

    emailSender.sendEmail(email);
  }


}
