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
      String fullName = dto.getFname() + " " + dto.getLname().toUpperCase();

      String uid = dto.getUid();
      if (uid == null) {
         uid = "anonymous";
      }
      User user = new User(uid, dto.getWorkEmail(), fullName);
      deepDomainLogic(user);
   }

   private void deepDomainLogic(User user) {
      // DO NOT DO defensive programming in our core logic  < fear driven development
      if (user.getEmail().isPresent()) {
         log.debug("Send welcome email to  " + user.getEmail().get());
      }

      log.debug("Insert user in my database: " + user.getUsername());

      log.debug("More business logic with " + user.getFullName() + " of id " + user.getUsername().toLowerCase());

      // then, in multiple places:
      user.getEmailContact().ifPresent(this::sendMailTo);
      user.getEmailContact().ifPresent(this::sendMailTo);
      user.getEmailContact().ifPresent(this::sendMailTo);
      user.getEmailContact().ifPresent(this::sendMailTo);

   }

   private void sendMailTo(String emailContact) {
      System.out.println("Contact: " + emailContact);
      //..implem left out
   }

}
