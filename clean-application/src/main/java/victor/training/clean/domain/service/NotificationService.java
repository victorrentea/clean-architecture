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

  // Core application logic, my Zen garden 🧘☯
  public void sendWelcomeEmail(Customer customer, String usernamePart) {
    // ⚠️ Scary, large external DTO TODO extract needed parts into a new dedicated Value Object
    LdapUserDto ldapUserDto = fetchUserFromLdap(usernamePart);

    // ⚠️ Data mapping mixed with core logic TODO pull it earlier
    String fullName = ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase();

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", Welcome to our clean app!\n" +
              "Sincerely, " + fullName)
        .build();


    // ⚠️ Unguarded nullable fields (causing NPE in other places) TODO return Optional<> from getter
    if (ldapUserDto.getWorkEmail() != null) {
      // ⚠️ Logic repeats in other places TODO push logic in my new class
      email.getCc().add(fullName + " <" + ldapUserDto.getWorkEmail() + ">");
    }

    emailSender.sendEmail(email);

    // ⚠️ Swap this line with next one to cause a bug (=TEMPORAL COUPLING) TODO make immutable💚
    normalize(ldapUserDto);

    // ⚠️ 'un' = bad name TODO use my domain names ('username')
    customer.setCreatedByUsername(ldapUserDto.getUn());
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

  public void sendGoldBenefitsEmail(Customer customer, String usernamePart) {
    LdapUserDto userLdapDto = fetchUserFromLdap(usernamePart);

    boolean canReturnOrders = customer.isGoldMember() || customer.getLegalEntityCode() == null;

    String returnOrdersStr = canReturnOrders ? "You are allowed to return orders\n" : "";

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome to our Gold membership!")
        .body(returnOrdersStr +
              "Yours sincerely, " + userLdapDto.getFname() + " " + userLdapDto.getLname().toUpperCase())
        .build();

    email.getCc().add(userLdapDto.getFname() + " " + userLdapDto.getLname().toUpperCase()
                      + " <" + userLdapDto.getWorkEmail() + ">");

    emailSender.sendEmail(email);
  }


}
