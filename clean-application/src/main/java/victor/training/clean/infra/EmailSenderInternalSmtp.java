package victor.training.clean.infra;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.service.EmailService;

@Slf4j
@Service
public class EmailSenderInternalSmtp implements EmailService {
   @Override
   public void sendEmail(Email email) {
      log.info("Sending email: " + email);
   }
}
