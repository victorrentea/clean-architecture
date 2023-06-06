package victor.training.clean.infra;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Email;

@Slf4j
@Service
public class EmailSender {
   public void sendEmail(Email email) {
      log.info("Sending email: " + email);
   }
}
