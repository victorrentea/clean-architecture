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

   public void importUserFromLdap(String username) {
      List<LdapUserDto> list = ldapApi.searchUsingGET(null, null, username.toUpperCase());

      if (list.size() != 1) {
         throw new IllegalArgumentException("There is no single user matching username " + username);
      }

      LdapUserDto dto = list.get(0);

      String fullName = dto.getFname() + " " + dto.getLname().toUpperCase(); // i used a derived value from the original fieelds
      if (dto.getUid() == null) {
         dto.setUid("anonymous");
      }
      User user = new User(dto.getUid() == null ? "anonymous" : dto.getUid(), fullName, dto.getWorkEmail());
      deepDomainLogic(user);
   }

   private void deepDomainLogic(User user) { // an object enters my domain logic with too much data
      if (user.getEmail().isPresent()) { // null in fields. NO i want optional !
         log.debug("Send welcome email to  " + user.getEmail().get()); // "work" is redundant
      }

      log.debug("Insert user in my database: " + user.getUsername()); // bad name "username"

      log.debug("More business logic with " + user.getFullName() + " of id " + user.getUsername().toLowerCase());

      // then, in multiple places:
      user.asContact().ifPresent(this::sendMailTo);
      user.asContact().ifPresent(this::sendMailTo);
      user.asContact().ifPresent(this::sendMailTo);
      user.asContact().ifPresent(this::sendMailTo);
   }


   private void sendMailTo(String emailContact) {
      System.out.println("Contact: " + emailContact);
      //..implem left out
   }

}
