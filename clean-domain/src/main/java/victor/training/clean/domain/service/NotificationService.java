package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.DomainEvent73;
import victor.training.clean.domain.model.User;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {
  private final KafkaAvroAdapter emailSender;
  private final UserPort adapter;

//  private final victor.training.clean.infra.LdapApi a;

  public void sendWelcomeEmail(Customer customer, String userId) {
    User user = adapter.fetchUser(userId);

    DomainEvent73 email = DomainEvent73.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", Welcome to our clean app!\n" +
              "Sincerely, " + user.fullName())
        .build();

    emailSender.publishEvent(email);

    // ⚠️ TEMPORAL COUPLING: tests fail if you swap the next 2 lines TODO use immutable VO

//    normalize(ldapUserDto);
    customer.setCreatedByUsername(user.username());
  }




}
