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
    // ⚠️ external DTO directly used inside my core logic
    //  TODO convert it into a new dedicated class - a Value Object (VO)
//    LdapUserDto userDto = loadUserFromLdap(userId);
    User userDto = loadUserFromLdap(userId);

    // ⚠️ data mapping mixed with my core domain logic TODO pull it earlier

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", Welcome to our clean app!\n" +
              "Sincerely, " + userDto.getFullName())
        .build();


    // ⚠️ Null check can be forgotten in other places; TODO return Optional<> from the getter
    if (userDto.getWorkEmail() != null) {
      // ⚠️ the same logic repeats later TODO extract method in the new VO class
      if (isaBoolean(userDto)) {
        email.getCc().add(userDto.getWorkEmail());
      }
    }

    emailSender.sendEmail(email);

    // ⚠️ TEMPORAL COUPLING: tests fail if you swap the next 2 lines TODO use immutable VO
    normalize(userDto);

    // ⚠️ 'un' ?!! <- in my domain a User has a 'username' TODO use domain names in VO
    customer.setCreatedByUsername(userDto.getUn());
  }

  private boolean isaBoolean(LdapUserDto userDto) {
    return userDto.getWorkEmail().toLowerCase().endsWith("@cleanapp.com");
  }

  private User loadUserFromLdap(String userId) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(userId.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for uid='" + userId + "' returned too many results: " + dtoList);
    }

    // al apiului pe care-l chem
    LdapUserDto dto = dtoList.get(0);
    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase();

    // al meu
    User user = new User(dto.getUn(), fullName, dto.getWorkEmail());

    return user;
  }

  private void normalize(LdapUserDto dto) {
    if (dto.getUn().startsWith("s")) {// eg s12051 - a system user
      dto.setUn("system"); // ⚠️ dirty hack
    }
  }

  public void sendGoldBenefitsEmail(Customer customer, String userId) {
    LdapUserDto userDto = loadUserFromLdap(userId);

    int discountPercentage = 1;
    if (customer.isGoldMember()) {
      discountPercentage += 3;
    }

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome to our Gold membership!")
        .body("Please enjoy a special discount of " + discountPercentage + "%\n" +
              "Yours sincerely, " + userDto.getFname() + " " + userDto.getLname().toUpperCase())
        .build();

    if (isaBoolean(userDto)) {
      email.getCc().add(userDto.getWorkEmail());
    }

    emailSender.sendEmail(email);
  }



}
