package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {
  private final EmailSender emailSender;
  private final UserAdapter userAdapter;


  public void sendWelcomeEmail(Customer customer, String userId) {
    User user = userAdapter.fetchUser(userId);

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", Welcome to our clean app!\n" +
              "Sincerely, " + user.fullName())
        .build();

    emailSender.sendEmail(email);

    // ⚠️ TEMPORAL COUPLING: tests fail if you swap the next 2 lines TODO use immutable VO

//    normalize(ldapUserDto);
    customer.setCreatedByUsername(user.username());
  }




}
