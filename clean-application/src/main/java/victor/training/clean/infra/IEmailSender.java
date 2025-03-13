package victor.training.clean.infra;

import victor.training.clean.domain.model.Email;

public interface IEmailSender {
  void sendEmail(Email email);
}
