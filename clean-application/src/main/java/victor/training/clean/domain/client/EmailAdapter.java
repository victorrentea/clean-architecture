package victor.training.clean.domain.client;

import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.model.User;

public interface EmailAdapter {

    void sendEmail(Email email);

}
