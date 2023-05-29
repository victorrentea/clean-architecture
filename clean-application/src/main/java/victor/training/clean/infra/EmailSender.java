package victor.training.clean.infra;

import org.springframework.stereotype.Service;
import victor.training.clean.domain.client.EmailAdapter;
import victor.training.clean.domain.model.Email;

// We pretend this is an external API we have to use
@Service
public class EmailSender implements EmailAdapter {
	
   public void sendEmail(Email email) {
		// implementation goes here
   }
}
