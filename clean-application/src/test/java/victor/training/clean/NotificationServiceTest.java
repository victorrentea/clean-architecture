package victor.training.clean;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.model.Email;
import victor.training.clean.domain.service.EmailSenderInterface;
import victor.training.clean.domain.service.NotificationService;

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
  EmailSenderInterface emailSenderInterface;

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

    verify(emailSenderInterface).sendEmail(emailCaptor.capture());
    Email email = emailCaptor.getValue();
    assertThat(email.getFrom()).isEqualTo("noreply@cleanapp.com");
    assertThat(email.getTo()).isEqualTo("jdoe@example.com");
    assertThat(email.getSubject()).isEqualTo("Welcome!");
    assertThat(email.getBody())
        .contains("Welcome Customer Name")
        .contains("you can return orders")
        .contains("John DOE");
    assertThat(email.getCc()).containsExactly("John DOE <jdoe@cleanapp.com>");
    assertThat(customer.getCreatedByUsername()).isEqualTo("jdoe");
  }
  @Test
  void sendWelcomeEmail_noWorkEmail() {
    notificationService.sendWelcomeEmail(customer,"noemail");

    verify(emailSenderInterface).sendEmail(argThat(email -> email.getCc().isEmpty()));
  }
  @Test
  void sendWelcomeEmail_noEmail() {
    notificationService.sendWelcomeEmail(customer,"externalEmail");

    verify(emailSenderInterface).sendEmail(argThat(email -> email.getCc().isEmpty()));
  }
  @Test
  void sendWelcomeEmail_systemUser() {
    notificationService.sendWelcomeEmail(customer, "nouid");

    assertThat(customer.getCreatedByUsername())
        .isEqualTo("system");
  }

}