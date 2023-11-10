package victor.training.clean.application.usecase;

import victor.training.clean.application.entity.Email;

public interface EmailSender {
  void sendEmail(Email email);
}
