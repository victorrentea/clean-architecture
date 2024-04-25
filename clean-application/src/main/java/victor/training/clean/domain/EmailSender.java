package victor.training.clean.domain;

import victor.training.clean.domain.model.Email;

public interface EmailSender {
  void sendEmail(Email email);
}
