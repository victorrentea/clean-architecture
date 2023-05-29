package victor.training.clean.domain.service;

import victor.training.clean.domain.model.Email;

public interface EmailNotificationGateway {
    void sendEmail(Email email);
}
