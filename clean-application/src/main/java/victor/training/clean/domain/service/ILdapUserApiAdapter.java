package victor.training.clean.domain.service;

import victor.training.clean.domain.model.User;

public interface ILdapUserApiAdapter {
  // infra gargabe
  User fetchUserDetailsFromLdap(String userId);
}
