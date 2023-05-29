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

//    String username = dto.getUid();
//    if (username == null) {
//      username = "anonymous";
//    }

//    String username = Optional.ofNullable(dto.getUid()).orElse("anonymous");

    User user = fromDto(dto);
    complexLogic(user);
  }

  private static User fromDto(LdapUserDto dto) {
    String username = dto.getUid() != null ? dto.getUid() : "anonymous";

    String fullName = dto.getFname() + " " + dto.getLname().toUpperCase();
    String workEmail = dto.getWorkEmail() != null ? dto.getWorkEmail().toLowerCase():null;
    User user = new User(username, fullName, workEmail);
    return user;
  }

  private void complexLogic(User user) {
    user.getEmail().ifPresent(this::checkNewUser);

    log.debug("Insert user in my database: " + user.getUsername());

    log.debug("More logic for " + user.getFullName() + " of id " + user.getUsername().toLowerCase());

    user.asEmailContact().ifPresent(this::sendMailTo);

    user.asEmailContact().ifPresent(this::sendMailTo);
  }


  private void sendMailTo(String emailContact) { // don't change this <- imagine it's library code
    //... implementation left out
    log.debug("Contact: " + emailContact);
  }

  public void checkNewUser(String email) {
    log.debug("Check this user is not already in my system  " + email);
  }

}
