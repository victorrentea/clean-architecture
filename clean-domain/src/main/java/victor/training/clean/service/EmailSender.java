package victor.training.clean.service;

import victor.training.clean.entity.Email;

public interface EmailSender {
   void sendEmail(Email email);
}
