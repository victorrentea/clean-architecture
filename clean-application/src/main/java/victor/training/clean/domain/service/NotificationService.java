package victor.training.clean.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.model.User;
import victor.training.clean.infra.EmailSender;
import victor.training.clean.infra.LdapApi;
import victor.training.clean.infra.LdapApiAdapter;
import victor.training.clean.infra.LdapUserDto;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {
  private final EmailSender emailSender;
  private final LdapApiAdapter ldapApiAdapter;

  public void sendWelcomeEmail(Customer customer, String usernamePart) {
    User user = ldapApiAdapter.fetchUser(usernamePart);
    // ------
    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", welcome! Sincerely, " + user.fullName())
        .build();
    if (user.email().isPresent()) {
      String contact = user.fullName() + " <" + user.email().get().toLowerCase() + ">";
      email.getCc().add(contact);
    }
    emailSender.sendEmail(email);
    customer.setCreatedByUsername(user.username());
  }

  public void sendGoldBenefitsEmail(Customer customer, String usernamePart) {
    User user = ldapApiAdapter.fetchUser(usernamePart);

    boolean canReturnOrders = customer.canReturnOrders();

    String returnOrdersStr = canReturnOrders ? "You are allowed to return orders\n" : "";

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome to our Gold membership!")
        .body(returnOrdersStr +
              "Yours sincerely, " + user.fullName())
        .build();

    if (user.email().isPresent()) {
      String contact = user.fullName() + " <" + user.email().get().toLowerCase() + ">";
      email.getCc().add(contact);
    }

    emailSender.sendEmail(email);
  }


}
