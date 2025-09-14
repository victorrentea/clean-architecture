package victor.training.clean.domain.service;

import victor.training.clean.domain.model.Email;

public interface EmailSenderPort {
  void sendEmail(Email email);
}
