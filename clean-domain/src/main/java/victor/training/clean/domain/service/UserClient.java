package victor.training.clean.domain.service;

import victor.training.clean.domain.model.User;

// Joel Spolski "leaking abstractions" CEO of stackoverflow.com
public interface UserClient {
  // in DDD book EEvans had a dream: that we should not need to distinguish between
  // a call in my own DB (2ms) and a foreign API(200ms)

  // infra gargabe
  User fetchUser(String userId);
}
