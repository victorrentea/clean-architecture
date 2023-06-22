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
      throw new IllegalArgumentException("Expected single user to match username "
                                         + targetUsername + ", got: " + dtoList);
    }

    LdapUserDto dto = dtoList.get(0);

    complexLogic(dto);
  }

  private void complexLogic(LdapUserDto dto) { // ⚠️ many extra useless fields
    if (dto.getWorkEmail() != null) { // ⚠️ NPE in other unguarded places?
      checkNewUser(dto.getWorkEmail());
    }

    // ⚠️ data mapping mixed with my core domain logic
    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase(); // Victor RENTEA

    normalize(dto); // ⚠️ temporal coupling with the next line

    // ⚠️ 'uid' <- ugly name: in my domain a User has a 'username'
    log.debug("More logic for " + fullName + " of id " + dto.getUid());

    // avoid calling this if the user has no email
    sendMailTo(asEmailContact(dto, fullName));

    // ⚠️ the same logic repeats later
    sendMailTo(asEmailContact(dto, fullName));
  }

  private static String asEmailContact(LdapUserDto dto, String fullName) {
    return fullName + " <" + dto.getWorkEmail().toLowerCase() + ">";
  }

  private void normalize(LdapUserDto dto) {
    if (dto.getUid() == null) {
      dto.setUid("anonymous"); // ⚠️ dirty hack
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
