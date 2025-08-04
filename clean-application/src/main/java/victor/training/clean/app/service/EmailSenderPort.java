package victor.training.clean.app.service;

import victor.training.clean.app.model.Email;

public interface EmailSenderPort {
  void sendEmail(Email email);
}
