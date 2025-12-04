package victor.training.clean.domain.port;

import victor.training.clean.domain.model.Email;

public interface EmailSender {
  void sendEmail(Email email);
}
