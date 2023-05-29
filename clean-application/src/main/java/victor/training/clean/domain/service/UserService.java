package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;
import java.util.Optional;

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

    User user = fromDto(dto);
    complexLogic(user);
  }

  private User fromDto(LdapUserDto dto) {
    String userName = Optional.ofNullable(dto.getUid()).orElse("anonymous");
    String email = dto.getWorkEmail();
    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase();
    return new User(userName, email, fullName);
  }

  private void complexLogic(User user) { // ⚠️ many useless fields
    if (user.getEmail() != null) { // ⚠️ NPE in other unguarded places?
      checkNewUser(user.getEmail().toLowerCase());
    }

    // ⚠️ 'uid' <- ugly attribute name; I'd prefer to see 'username', my domain term
    log.debug("Insert user in my database: " + user.getUserName());

    log.debug("More logic for " + user.getFullName() + " of id " + user.getUserName().toLowerCase());

    sendMailTo(user.getEmailContact());

    // then later, again (⚠️ repeated logic):
    sendMailTo(user.getEmailContact());
  }

  private void sendMailTo(String emailContact) { // don't change this <- imagine it's library code
    //... implementation left out
    log.debug("Contact: " + emailContact);
  }

  public void checkNewUser(String email) {
    log.debug("Check this user is not already in my system  " + email);
  }

}
