package victor.training.clean;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.service.NotificationService;
import victor.training.clean.infra.EmailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
@AutoConfigureWireMock(port = 0)
class NotificationServiceTest {

  @Autowired
  NotificationService notificationService;

  @Autowired
  ApiClient apiClient;

  @MockBean
  EmailSender emailSender;

  ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);

  Customer customer = new Customer()
      .setName("Customer Name")
      .setEmail("jdoe@example.com");

  @Autowired
  final void before(@Value("${wiremock.server.port}") int port) {
    System.out.println("RUN!! " + port);
    apiClient.setBasePath("http://localhost:"  + port);
  }

  @Test
  void sendWelcomeEmail_baseFlow() {
    notificationService.sendWelcomeEmail(customer,"full");

    verify(emailSender).sendEmail(emailCaptor.capture());
    Email email = emailCaptor.getValue();
    assertThat(email.getFrom()).isEqualTo("noreply@cleanapp.com");
    assertThat(email.getTo()).isEqualTo("jdoe@example.com");
    assertThat(email.getSubject()).isEqualTo("Welcome!");
    assertThat(email.getBody())
        .contains("Dear Customer Name")
        .contains("John DOE");
    assertThat(email.getCc()).containsExactly("John DOE <jdoe@cleanApp.com>");
    assertThat(customer.getCreatedByUsername()).isEqualTo("jdoe");
  }
  @Test
  void sendWelcomeEmail_noWorkEmail() {
    notificationService.sendWelcomeEmail(customer,"noemail");

    verify(emailSender).sendEmail(argThat(email -> email.getCc().isEmpty()));
  }
  @Test
  void sendWelcomeEmail_noEmail() {
    notificationService.sendWelcomeEmail(customer,"externalEmail");

    verify(emailSender).sendEmail(argThat(email -> email.getCc().isEmpty()));
  }
  @Test
  void sendWelcomeEmail_systemUser() {
    notificationService.sendWelcomeEmail(customer, "nouid");

    assertThat(customer.getCreatedByUsername())
        .isEqualTo("system");
  }

  @Test
  void sendGoldBenefits_baseFlow() {
    customer.setGoldMember(true);
    notificationService.sendGoldBenefitsEmail(customer,"full");

    verify(emailSender).sendEmail(emailCaptor.capture());
    Email email = emailCaptor.getValue();
    assertThat(email.getFrom()).isEqualTo("noreply@cleanapp.com");
    assertThat(email.getTo()).isEqualTo("jdoe@example.com");
    assertThat(email.getSubject()).isEqualTo("Welcome to our Gold membership!");
    assertThat(email.getBody()).isEqualTo("You are allowed to return orders\n" +
                                          "Yours sincerely, John DOE");
    assertThat(email.getCc()).containsExactly("John DOE <jdoe@cleanApp.com>");
  }
  @Test
  @Disabled("BUG: throws NPE. Why !?")
  void sendGoldBenefits_missingEmail() {
    notificationService.sendGoldBenefitsEmail(customer,"noemail");

    verify(emailSender).sendEmail(argThat(email -> email.getCc().isEmpty()));
  }

}