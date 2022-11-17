package victor.training.clean.infra;

import org.springframework.stereotype.Service;
import victor.training.clean.common.domain.model.Email;
import victor.training.clean.common.domain.service.IEmailSender;

// We pretend this is an external API we have to use
@Service
public class EmailSender implements IEmailSender {
	
   @Override
   public void sendEmail(Email email) {
		// implementation goes here
   }
}
