package victor.training.clean.domain.service;

import victor.training.clean.domain.model.Customer;

public interface NotificationServiceInterface {
  void sendWelcomeEmail(Customer customer, String usernamePart);
}
