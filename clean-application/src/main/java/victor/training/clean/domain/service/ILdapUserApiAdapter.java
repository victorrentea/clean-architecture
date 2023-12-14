package victor.training.clean.domain.service;

import victor.training.clean.domain.model.User;

// Joel Spolski "leaking abstractions" CEO of stackoverflow.com
public interface ILdapUserApiAdapter {
  // infra gargabe
  User fetchUserDetailsFromLdap(String userId);
}
