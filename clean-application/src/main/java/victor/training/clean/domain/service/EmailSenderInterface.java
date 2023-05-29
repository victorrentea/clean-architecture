package victor.training.clean.domain.service;

import victor.training.clean.domain.model.Email;

// TODO victorrentea 2023-05-29: improve names
public interface EmailSenderInterface {
  void sendEmail(Email email);
}
