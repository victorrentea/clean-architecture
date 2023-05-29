package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.LdapUser;
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

    complexLogic(fromUserLdapDto(dto));
  }

  private void complexLogic(LdapUser ldapUser) { // ⚠️ many useless fields
    if (ldapUser.getEmail() != null) { // ⚠️ NPE in other unguarded places?
      checkNewUser(ldapUser.getEmail().toLowerCase());
    }

    // ⚠️ 'uid' <- ugly attribute name; I'd prefer to see 'username', my domain term
    log.debug("Insert user in my database: " + ldapUser.getUserName());

    // // ⚠️ data mapping mixed with biz logic (pretend)
    // String fullName = dto.getFname() + " " + dto.getLname().toUpperCase();

    // fixUser(dto); // ⚠️ temporal coupling with the next line
    log.debug("More logic for " + ldapUser.getFullName() + " of id " + ldapUser.getUserName().toLowerCase());

    sendMailTo(ldapUser.getFullName() + " <" + ldapUser.getEmail().toLowerCase() + ">");

    // then later, again (⚠️ repeated logic):
    sendMailTo(ldapUser.getFullName() + " <" + ldapUser.getFullName().toLowerCase() + ">");
  }

  // private void fixUser(LdapUserDto dto) {
  //   if (dto.getUid() == null) {
  //     dto.setUid("anonymous"); // ⚠️ mutability risks
  //   }
  // }

  private void sendMailTo(String emailContact) { // don't change this <- imagine it's library code
    //... implementation left out
    log.debug("Contact: " + emailContact);
  }

  public void checkNewUser(String email) {
    log.debug("Check this user is not already in my system  " + email);
  }

  private LdapUser fromUserLdapDto(LdapUserDto dto) {
    return new LdapUser(
        dto.getUid() == null ? "anonymous" : dto.getUid(),
        dto.getFname() + " " + dto.getLname().toUpperCase(),
        dto.getWorkEmail()
    );
  }

}
