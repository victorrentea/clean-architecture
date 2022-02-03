package victor.training.clean.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LdapFakeController {

   @GetMapping("ldap/user")
   public String method() {
      return "a";
   }
}
