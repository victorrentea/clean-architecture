package victor.training.clean.infra;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Email;

@Service
public class EmailSender implements victor.training.clean.domain.service.IEmailSender {
   private static final Logger log = org.slf4j.LoggerFactory.getLogger(EmailSender.class);

   @Override
   public void sendEmail(Email email) {
      // Imagine 20 lines of infra code to:
      // - get a SMTP connection to RELAY-COSMO-SMTP server
      // - basic authentication with the mail server
      // - track emails sent and avoid spamming users
      // - more
      log.info("Sending email: " + email);
   }
}
