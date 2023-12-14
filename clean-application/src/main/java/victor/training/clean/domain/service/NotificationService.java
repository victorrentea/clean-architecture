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
    // ⚠️ external DTO directly used in my app logic TODO convert it into a new dedicated Value Object
    User ldapUserDto = fetchUserDetailsFromLdap(userId);

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", Welcome to our clean app!\n" +
              "Sincerely, " + ldapUserDto.fullName())
        .build();


    // ⚠️ Null check can be forgotten in other places; TODO return Optional<> from the getter
    if (ldapUserDto.email().isPresent()) { // 🤞 I hope everywhere I check for null
      // ⚠️ the same logic repeats later TODO extract method in the new VO class
      if (ldapUserDto.email().get().toLowerCase().endsWith("@cleanapp.com")) {
        email.getCc().add(ldapUserDto.email().get());
      }
    }

    emailSender.sendEmail(email);


    // ⚠️ 'un' ?!! <- in my domain a User has a 'username' TODO use domain names in VO
    customer.setCreatedByUsername(ldapUserDto.username());
  }

  private User fetchUserDetailsFromLdap(String userId) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(userId.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for uid='" + userId + "' returned too many results: " + dtoList);
    }


    String un = dtoList.get(0).getUn();
    if (un.startsWith("s")) {// eg 's12051' is a system user
      un="system"; // ⚠️ dirty hack 21:00 Fri
   }


    String fullName = dtoList.get(0).getFname() + " " + dtoList.get(0).getLname();
    User user = new User(un,
        fullName,
        Optional.ofNullable(dtoList.get(0).getWorkEmail()));
    return  user;
  }


  public void sendGoldBenefitsEmail(Customer customer, String userId) {
    User userDto = fetchUserDetailsFromLdap(userId);

    int discountPercentage = customer.getDiscountPercentage();

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome to our Gold membership!")
        .body("Please enjoy a special discount of " + discountPercentage + "%\n" +
              "Yours sincerely, " + userDto.fullName())
        .build();

    if (userDto.email().isPresent()) {
      if (userDto.email().get().toLowerCase().endsWith("@cleanapp.com")) {
        email.getCc().add(userDto.email().get());
      }
    }

    emailSender.sendEmail(email);
  }



}
