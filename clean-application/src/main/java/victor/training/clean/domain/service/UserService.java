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

  public void importUserFromLdap(String targetUsername) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(null, null, targetUsername.toUpperCase());

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Expected single user to match username " + targetUsername + ", got: " + dtoList);
    }

    LdapUserDto dto = dtoList.get(0);
    String userRct = dto.getUid() !=null ? dto.getUid() : "anonymous";
    // ⚠️ data mapping mixed with biz logic
    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase();
    User user = new User(userRct, dto.getWorkEmail(), fullName);
    complexLogic(user);
  }

  private void complexLogic(User user) { // ⚠️ many useless fields
    if (user.getEmail() != null) { // ⚠️ NPE in other unguarded places?
      checkNewUser(user.getEmail().toLowerCase());
    }

    // ⚠️ 'uid' <- ugly attribute name; I'd prefer to see 'username', my domain term
    log.debug("Insert user in my database: " + user.getUserRct());


//    fixUser(user); // ⚠️ temporal coupling with the next line
    log.debug("More logic for " + user.getFullName() + " of id " + user.getUserRct().toLowerCase());

    sendMailTo(user.getFullName() + " <" + user.getEmail().toLowerCase() + ">");
    // then later, again (⚠️ repeated logic):
    sendMailTo(user.getFullName() + " <" + user.getEmail().toLowerCase() + ">");
  }

//  private void fixUser(User user) {
//    if (user.getUserRct() == null) {
//      user.setUid("anonymous"); // ⚠️ mutability risks
//    }
//  }

  private void sendMailTo(String emailContact) { // don't change this <- it's library code
    //... implementation left out
    log.debug("Contact: " + emailContact);
  }

  public void checkNewUser(String email) {
    log.debug("Check this user is not already in my system  " + email);
  }

}
