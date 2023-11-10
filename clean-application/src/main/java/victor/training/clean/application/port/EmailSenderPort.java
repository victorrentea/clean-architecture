package victor.training.clean.application.port;

import victor.training.clean.application.entity.Email;

public interface EmailSenderPort {
  void sendEmail(Email email);
}
