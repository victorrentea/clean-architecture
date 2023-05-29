package victor.training.clean.domain.model;

import lombok.Value;

@Value
public class LdapUser {

  private String userName;

  private String fullName;

  private String email;

}
