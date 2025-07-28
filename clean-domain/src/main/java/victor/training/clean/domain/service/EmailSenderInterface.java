package victor.training.clean.domain.service;

import victor.training.clean.domain.model.Email;

public interface EmailSenderInterface {
  void sendEmail(Email email);
}
