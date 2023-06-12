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
public class UserService {
  private final LdapApi ldapApi;

  public void importUserFromLdap(String targetUsername) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(null, null, targetUsername.toUpperCase());

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Expected single user to match username " + targetUsername + ", got: " + dtoList);
    }

    LdapUserDto dto = dtoList.get(0);

    complexLogic(dto);
  }

  private void complexLogic(LdapUserDto dto) { // ⚠️ many extra useless fields
    if (dto.getWorkEmail() != null) { // ⚠️ NPE in other unguarded places?
      checkNewUser(dto.getWorkEmail());
    }

    // ⚠️ data mapping mixed with my core domain logic (imagine)
    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase();

    fixUser(dto); // ⚠️ temporal coupling with the next line
    log.debug("More logic for " + fullName + " of id " + dto.getUid().toLowerCase()); // ⚠️ 'uid' <- ugly; Users have a 'username' in my domain

    sendMailTo(fullName + " <" + dto.getWorkEmail() + ">"); // should this run if the user has no email ?

    // then later, again (⚠️ repeated logic):
    sendMailTo(fullName + " <" + dto.getWorkEmail() + ">");
  }

  private void fixUser(LdapUserDto dto) {
    if (dto.getUid() == null) {
      dto.setUid("anonymous"); // ⚠️ mutability risks
    }
  }

  private void sendMailTo(String emailContact) { // don't change this <- imagine it's library code
    //... implementation left out
    log.debug("Contact: " + emailContact);
  }

  private void checkNewUser(String email) {
    log.debug("Check this user is not already in my system  " + email);
  }

}
