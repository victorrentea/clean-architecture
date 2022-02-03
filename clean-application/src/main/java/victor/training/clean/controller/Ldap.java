package victor.training.clean.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class Ldap {

   @GetMapping("ldap/user")
   public List<LdapUser> search(String username, String fName, String lName) {
      return List.of(ldapUser1, ldapUser2);
   }

   public static final LdapUser ldapUser1 = new LdapUser();
   static {
      ldapUser1.setFName("John");
      ldapUser1.setLName("DOE");
      ldapUser1.setUId("jdoe");
      ldapUser1.setCreationDate("2021-01-03");
      ldapUser1.setWorkEmail("0123456789");
      ldapUser1.setEmailAddresses(List.of(new LdapUserPhone("WORK", "jdoe@bigcorp.com")));
   }
   public static final LdapUser ldapUser2 = new LdapUser();
   static {
      ldapUser2.setFName("Jane");
      ldapUser2.setLName("DOE");
      ldapUser2.setUId("janedoe");
      ldapUser2.setCreationDate("2021-01-03");
   }

   @Data
   @AllArgsConstructor
   public static class LdapUserPhone {
      private String type;
      private String val;
   }

   @Data
   public static class LdapUser {
      private String uId;
      private String fName;
      private String lName;
      private String departmentId;
      private String language;
      private String creationDate;
      private String workEmail;
      private List<LdapUserPhone> emailAddresses = new ArrayList<>();
   }
}
