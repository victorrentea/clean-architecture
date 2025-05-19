package victor.training.clean.domain.service;

import victor.training.clean.domain.model.User;

public interface RetrieveUserService { // SPI
  User retrieve(String usernamePart);
}
