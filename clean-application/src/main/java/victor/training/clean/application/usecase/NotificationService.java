package victor.training.clean.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.clean.application.entity.Customer;
import victor.training.clean.application.entity.Email;
import victor.training.clean.application.entity.User;
//import victor.training.clean.infra.LdapUserDto;

@RequiredArgsConstructor
@Slf4j
@Service
// ATENTIE: intram (cu respect) in cod de domeniu - asta tre sa fie cel mai curat din aplicatie
public class NotificationService {
  private final EmailSender emailSender;
  private final UserProvider userProvider;
  //1) acum pot @MockBean/@Mock in @Test pe Adapter
  //2) inainte trebuie WireMock.stubFor(JSON de-al lor care sa-mi dea mie ce-mi trebuie)


  public void sendWelcomeEmail(Customer customer, String userId) {
    // ⚠️ external DTO directly used inside my core logic
    //  TODO convert it into a new dedicated class - a Value Object (VO)
    User user = userProvider.fetchUser(userId);
//    LdapUserDto dto = new LdapUserDto();
//    System.out.println(dto.getFname());

    // ⚠️ data mapping mixed with my core domain logic TODO pull it earlier
    String fullName = user.fullName();

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome!")
        .body("Dear " + customer.getName() + ", Welcome to our clean app!\n" +
              "Sincerely, " + fullName)
        .build();

    // ⚠️ Null check can be forgotten in other places; TODO return Optional<> from the getter
    if (user.email().isPresent()) {
      // ⚠️ the same logic repeats later TODO extract method in the new VO class
      if (user.isCleanApp()) {
        email.getCc().add(user.email().get());
      }
    }

    emailSender.sendEmail(email);

    // ⚠️ TEMPORAL COUPLING: tests fail if you swap the next 2 lines TODO use immutable VO
//    normalize(user);

    customer.setCreatedByUsername(user.username());
  }



//  private void normalize(User dto) {
//    if (dto.getUn().startsWith("s")) {// eg s12051 - a system user
//      dto.setUn("system"); // ⚠️ dirty hack
//    }
//  }

  public void sendGoldBenefitsEmail(Customer customer, String userId) {
    User user = userProvider.fetchUser(userId);

    int discountPercentage = customer.getDiscountPercentage();

    Email email = Email.builder()
        .from("noreply@cleanapp.com")
        .to(customer.getEmail())
        .subject("Welcome to our Gold membership!")
        .body("Please enjoy a special discount of " + discountPercentage + "%\n" +
              "Yours sincerely, " + user.fullName())
        .build();

    if (user.isCleanApp()) {
      email.getCc().add(user.email().get());
    }

    emailSender.sendEmail(email);
  }


}
