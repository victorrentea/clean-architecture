package victor.training.clean.infra;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Email;

@Slf4j
@Service
public class EmailSender {
   public void sendEmail(Email email) {
      // Imagine 20 lines of infra code to:
      // - get a SMTP connection to RELAY-COSMO-SMTP server
      // - basic authentication with the mail server
      // - track emails sent and avoid spamming users
      // - more
      log.info("Sending emailAddres: " + email);
   }
}
