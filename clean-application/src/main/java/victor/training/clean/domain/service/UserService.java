package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
  private final LdapApi ldapApi;

  // PR reject: class is doing things at a different level of abstraction: adica...
  //  faci [domain logic] sfant si [mapari/validari/apicalls] murdare.
  public void importUserFromLdap(String username) {
    List<LdapUserDto> list = ldapApi.searchUsingGET(null, null, username.toUpperCase());

    if (list.size() != 1) {
      throw new IllegalArgumentException("There is no single user matching username " + username);
    }

    LdapUserDto dto = list.get(0);
    String un = dto.getUid() == null ? "anonymous" : dto.getUid().toUpperCase();
    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase(); // ⚠️ data mapping mixed with biz logic

    User user = new User(un, fullName, dto.getWorkEmail());
    deepDomainLogic(user);
  }

  private void deepDomainLogic(User user) { // ⚠️ useless fields -> i only need 3 not 7 fields
    if (user.getEmail().isPresent()) { // ⚠️ how about other unguarded places?
      log.debug("Send welcome email to  " + user.getEmail().get());
    }

    log.debug("Insert user in my database: " + user.getUsername()); // ⚠️ bad attribute name

    log.debug("More " + user.getFullName() + " of id " + user.getUsername().toLowerCase()); // ⚠️ pending NullPointerException

    // then, in multiple places:
    user.asEmailContact().ifPresent(c->sendMailTo(c)); // ⚠️ repeated logic
    user.asEmailContact().ifPresent(c->sendMailTo(c));
    user.asEmailContact().ifPresent(c->sendMailTo(c));
    user.asEmailContact().ifPresent(c->sendMailTo(c));
  }

  //  private void innocentHack(User dto) {
//    if (dto.getUid() == null) {
//      dto.setUid("anonymous"); // ⚠️ mutability risks
//    }
//  }

  private void sendMailTo(String emailContact) {
    System.out.println("Contact: " + emailContact);
    //..implem left out
  }

}
