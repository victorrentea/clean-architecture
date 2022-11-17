package victor.training.clean.common.domain.service;

import victor.training.clean.common.domain.model.Email;

public interface IEmailSender {
  void sendEmail(Email email);
}
