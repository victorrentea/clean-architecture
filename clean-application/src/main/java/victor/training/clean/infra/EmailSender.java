package victor.training.clean.infra;

import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.client.NotificationService;

// We pretend this is an external API we have to use
@Service
public class EmailSender implements NotificationService {
	
   @Override
   public void sendEmail(Email email) {
		// implementation goes here
   }
}
