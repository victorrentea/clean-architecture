package victor.training.clean.adapter;

import victor.training.clean.domain.model.LdapUser;

public interface LdapAdapter {

  LdapUser find(String targetUsername);
}
