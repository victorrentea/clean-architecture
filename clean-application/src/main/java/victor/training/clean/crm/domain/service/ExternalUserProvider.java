package victor.training.clean.crm.domain.service;

import victor.training.clean.crm.domain.model.User;

public interface ExternalUserProvider {
  User fetchByUsername(String targetUsername);
}
