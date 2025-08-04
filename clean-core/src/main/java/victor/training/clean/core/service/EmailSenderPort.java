package victor.training.clean.core.service;

import victor.training.clean.core.model.Email;

public interface EmailSenderPort {
  void sendEmail(Email email);
}
