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

   public void importUserFromLdap(String username) {
      List<LdapUserDto> list = ldapApi.searchUsingGET(null, null, username.toUpperCase());

      if (list.size() != 1) {
         throw new IllegalArgumentException("There is no single user matching username " + username);
      }

      LdapUserDto dto = list.get(0);

      deepDomainLogic(dto);
   }

   private void deepDomainLogic(LdapUserDto dto) { // too many fields
      if (dto.getWorkEmail()!=null) {
         log.debug("Send welcome email to  " + dto.getWorkEmail());
      }

      log.debug("Insert user in my database: " + dto.getUid()); // bad names : "username"

      String fullName = dto.getFname() + " " + dto.getLname().toUpperCase();

      // evil unexpected temporal coupling: hack should happen before uid.toLowerCase()
      innocentHack(dto);
      log.debug("More business logic with " + fullName + " of id " + dto.getUid().toLowerCase()); // NUll !!! OMG

      // then, in multiple places:
      sendMailTo(asContact(dto, fullName));
      sendMailTo(asContact(dto, fullName));
      sendMailTo(asContact(dto, fullName));
      sendMailTo(asContact(dto, fullName));
   }

   private static String asContact(LdapUserDto dto, String fullName) {
      return fullName + " <" + dto.getWorkEmail() + ">";
   }

   private void innocentHack(LdapUserDto dto) {
      if (dto.getUid() == null) {
         dto.setUid("anonymous"); // setters ??!! yuuu 🤢
      }
   }

   private void sendMailTo(String emailContact) {
      System.out.println("Contact: " + emailContact);
      //..implem left out
   }

}
