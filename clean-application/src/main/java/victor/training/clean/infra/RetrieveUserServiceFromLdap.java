package victor.training.clean.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.User;

import java.util.List;
import java.util.Optional;

// there are only two things hard in programming: cache invalidation and naming things
@Slf4j
@RequiredArgsConstructor
@Service
//public class  UserService { //
//public class RetrieveUserService {
//public class UserAccessService { // "access"=convention to mean read-only. if  followed everywhere = VERY NICE FOR 🧠
// colliding with AWS IAM. Access"
//public class UserAccess { // "noun"
//public class AccessUser { // stateless piece of BEHAVIOR = verb! action!
//public class RetrieveUser { // no conventions. close to the business terms!
public class RetrieveUserServiceFromLdap implements victor.training.clean.domain.service.RetrieveUserService { // for code navigability (Ctrl-O/Shift x 2)
  // takeawy: narrow action + Service
  private final LdapApi ldapApi;

  @Override
  public User retrieve(String usernamePart) {
    // ⚠️ Scary, large external DTO TODO extract needed parts into a new dedicated Value Object
    LdapUserDto ldapUserDto = fetchUserFromLdap(usernamePart);

    return map(ldapUserDto);
  }

  private LdapUserDto fetchUserFromLdap(String usernamePart) {
    List<LdapUserDto> dtoList = ldapApi.searchUsingGET(usernamePart.toUpperCase(), null, null);

    if (dtoList.size() != 1) {
      throw new IllegalArgumentException("Search for username='" + usernamePart + "' did not return a single result: " + dtoList);
    }

    return dtoList.get(0);
  }

  private User map(LdapUserDto ldapUserDto) {
    // ⚠️ Data mapping mixed with core logic TODO pull it earlier
    String fullName = ldapUserDto.getFname() + " " + ldapUserDto.getLname().toUpperCase();
    normalize(ldapUserDto); // NU E FUNCTIE PURA(=da acelasi rez fara sa modifice chestii)!

    return new User(fullName,
        Optional.ofNullable(ldapUserDto.getWorkEmail()),
        ldapUserDto.getUn());
  }

  private void normalize(LdapUserDto ldapUserDto) {
    if (ldapUserDto.getUn().startsWith("s")) {
      ldapUserDto.setUn("system"); // ⚠️ dirty hack: replace any system user with 'system'
    }
  }

}
