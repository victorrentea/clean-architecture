package victor.training.clean.infra;

import victor.training.clean.domain.model.Email;

public interface EmailSender {
  void sendEmail(Email email);
}
