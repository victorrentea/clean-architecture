package victor.training.clean.customer.service;

import victor.training.clean.customer.entity.Email;

public interface EmailSender {
   void sendEmail(Email email);
}
