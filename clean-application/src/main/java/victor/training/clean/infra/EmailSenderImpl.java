package victor.training.clean.infra;

import org.springframework.stereotype.Service;
import victor.training.clean.entity.Email;
import victor.training.clean.service.EmailSender;

// We pretend this is an external API we have to use
@Service
public class EmailSenderImpl implements EmailSender {
	
   @Override
   public void sendEmail(Email email) {
		// implementation goes here
   }
}
