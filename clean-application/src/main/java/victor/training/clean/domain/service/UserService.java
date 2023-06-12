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
    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase();
    User user = new User(dto.getUid(), dto.getWorkEmail(), fullName);

    complexLogic(user);
  }

  private void complexLogic(User user) {
    user.getEmail().ifPresent(this::checkNewUser);

    log.debug("More logic for " + user.getFullName() + " of id " + user.getUsername().toLowerCase()); // ⚠️ 'uid' <- ugly; Users have a 'username' in my domain

    user.getEmailContact().ifPresent(this::sendMailTo);

    // then later, again (⚠️ repeated logic):
    user.getEmailContact().ifPresent(this::sendMailTo);
  }


  private void sendMailTo(String emailContact) { // don't change this <- imagine it's library code
    //... implementation left out
    log.debug("Contact: " + emailContact);
  }

  private void checkNewUser(String email) {
    log.debug("Check this user is not already in my system  " + email);
  }

}
