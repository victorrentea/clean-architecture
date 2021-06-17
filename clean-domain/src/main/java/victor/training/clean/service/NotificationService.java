package victor.training.clean.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.clean.entity.Email;

@Service
@RequiredArgsConstructor
public class NotificationService {
   private final EmailSender emailSender;

   public void orderWasInStock(String emailAddress) {
      sendEmail(emailAddress, "Your products were surprise!", "Corgrats!");

   }
   public void sendRegistrationEmail(String emailAddress) {
      sendEmail(emailAddress, "Welcome!", "You'll like it! Sincerely, Team");
   }

   private void sendEmail(String emailAddress, String subject, String body) {
      System.out.println("Sending activation link via email to " + emailAddress);
      Email email = new Email();
      email.setFrom("noreply");
      email.setTo(emailAddress);
      email.setSubject(subject);
      email.setBody(body);
      emailSender.sendEmail(email);
   }
}
