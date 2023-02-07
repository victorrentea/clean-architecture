package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import victor.training.clean.common.DomainService;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@DomainService // ce-ai tu mai sfant in app, aici e
public class UserService {
  private final LdapApi ldapApi;

  public void importUserFromLdap(String username) {
    List<LdapUserDto> list = ldapApi.searchUsingGET(null, null, username.toUpperCase());

    if (list.size() != 1) {
      throw new IllegalArgumentException("There is no single user matching username " + username);
    }

    LdapUserDto dto = list.get(0);

    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase(); // ⚠️ data mapping mixed with biz logic
    String uname = dto.getUid() != null ? dto.getUid() : "anonymous";
    User user = new User(uname, dto.getWorkEmail(), fullName);
    deepDomainLogic(user);
  }
  // Gata!

  private void deepDomainLogic(User user) {
    if (user.getEmail().isPresent()) {
      log.debug("Send welcome email to  " + user.getEmail().get());
    }

    log.debug("Insert user in my database: " + user.getUsername());

    log.debug("More " + user.getFullName() + " of id " + user.getUsername().toLowerCase()); // ⚠️ pending NullPointerException

    // then, in multiple places:
    user.asEmailContact().ifPresent(this::sendMailTo);

    user.asEmailContact().ifPresent(this::sendMailTo);
    user.asEmailContact().ifPresent(this::sendMailTo);
    user.asEmailContact().ifPresent(this::sendMailTo);
  }


  private void sendMailTo(String emailContact) {
    System.out.println("Contact: " + emailContact);
    //..implem left out
  }

}
