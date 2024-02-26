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

  public void sendWelcomeEmail(Customer customer, String userId) {
    // ⚠️ external DTO directly used in my app logic TODO convert it into a new dedicated Value Object
    LdapUserDto ldapUserDto = fetchUserDetailsFromLdap(userId);

    // ⚠️ data mapping mixed with my core domain logic TODO pull it earlier
    String fullName = ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase();

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", Welcome to our clean app!\n" +
              "Sincerely, " + fullName)
        .build();


    // ⚠️ Null check can be forgotten in other places; TODO return Optional<> from the getter
    if (ldapUserDto.getWorkEmail() != null) {
      // ⚠️ the same logic repeats later TODO extract method in the new VO class
      if (ldapUserDto.getWorkEmail().toLowerCase().endsWith("@cleanapp.com")) {
        email.getCc().add(ldapUserDto.getWorkEmail());
      }
    }

    emailSender.sendEmail(email);

    // ⚠️ TEMPORAL COUPLING: tests fail if you swap the next 2 lines TODO use immutable VO
    normalize(ldapUserDto);

    // ⚠️ 'un' ?!! <- in my domain a User has a 'username' TODO use domain names in VO
    customer.setCreatedByUsername(ldapUserDto.getUn());
  }

  private LdapUserDto fetchUserDetailsFromLdap(String userId) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(userId.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for uid='" + userId + "' returned too many results: " + dtoList);
    }

    return dtoList.get(0);
  }

  private void normalize(LdapUserDto dto) {
    if (dto.getUn().startsWith("s")) {// eg 's12051' is a system user
      dto.setUn("system"); // ⚠️ dirty hack
    }
  }

  public void sendGoldBenefitsEmail(Customer customer, String userId) {
    LdapUserDto userDto = fetchUserDetailsFromLdap(userId);

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome to our Gold membership!")
        .body("Please enjoy a special discount of " + customer.discountPercentage() + "%\n" +
              "Yours sincerely, " + userDto.getFname() + " " + userDto.getLname().toUpperCase())
        .build();

    if (userDto.getWorkEmail().toLowerCase().endsWith("@cleanapp.com")) {
      email.getCc().add(userDto.getWorkEmail());
    }

    emailSender.sendEmail(email);
  }



}
