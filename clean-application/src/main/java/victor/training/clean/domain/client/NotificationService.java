package victor.training.clean.domain.client;

import victor.training.clean.domain.model.Email;

// TODO victorrentea 2023-05-29: improve names
public interface NotificationService {
  void sendEmail(Email email);
}
