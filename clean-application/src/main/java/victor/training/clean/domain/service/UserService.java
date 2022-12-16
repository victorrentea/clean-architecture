package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService { // DOmain!!!
  private final LdapApi ldapApi;

  public void importUserFromLdap(String username) {
    List<LdapUserDto> list = ldapApi.searchUsingGET(null, null, username.toUpperCase());

    if (list.size() != 1) {
      throw new IllegalArgumentException("There is no single user matching username " + username);
    }

    LdapUserDto dto = list.get(0);

    deepDomainLogic(dto);
  }

  private void deepDomainLogic(LdapUserDto dto) { // ⚠️ useless fields ; i only need 4 not 10
    if (dto.getWorkEmail() != null) { // ⚠️ how about other unguarded places?
      log.debug("Send welcome email to  " + dto.getWorkEmail());
    }

    log.debug("Insert user in my database: " + dto.getUid()); // ⚠️ bad attribute name -> "username"

    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase(); // ⚠️ data mapping mixed with biz logic
    innocentHack(dto);
    log.debug("More " + fullName + " of id " + dto.getUid().toLowerCase()); // ⚠️ pending NullPointerException

    // then, in multiple places:
    sendMailTo(fullName + " <" + dto.getWorkEmail() + ">"); // ⚠️ repeated logic
    sendMailTo(fullName + " <" + dto.getWorkEmail() + ">");
    sendMailTo(fullName + " <" + dto.getWorkEmail() + ">");
    sendMailTo(fullName + " <" + dto.getWorkEmail() + ">");
  }

  private void innocentHack(LdapUserDto dto) {
    if (dto.getUid() == null) {
      dto.setUid("anonymous"); // ⚠️ mutability risks
    }
  }

  private void sendMailTo(String emailContact) {
    System.out.println("Contact: " + emailContact);
    //..implem left out
  }

}
