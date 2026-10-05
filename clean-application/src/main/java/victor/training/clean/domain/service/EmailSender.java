package victor.training.clean.domain.service;

import victor.training.clean.domain.model.Email;

/**
 * Port: sending an email is a domain intent; SMTP, retries and throttling are not.
 * Implemented by {@code victor.training.clean.infra.SmtpEmailSender}.
 */
public interface EmailSender {
  void sendEmail(Email email);
}
