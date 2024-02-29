package victor.training.clean.infra;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.DomainEvent73;
import victor.training.clean.domain.service.KafkaAvroAdapter;

@Slf4j
@Service
public class EmailSender implements KafkaAvroAdapter {
   @Override
   public void publishEvent(DomainEvent73 email) {
      // Imagine 20 lines of infra code to:
      // - get a SMTP connection to RELAY-COSMO-SMTP server
      // - basic authentication with the mail server
      // - track emails sent and avoid spamming users
      // - more
      log.info("Sending email: " + email);
   }
}
