package victor.training.clean.shared.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import victor.training.clean.ApiClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
@AutoConfigureWireMock(port = 9999)
class UserServiceTest {

  @Autowired
  UserService userService;

  @Autowired
  ApiClient apiClient;

  @BeforeEach
  final void before() {
    apiClient.setBasePath("http://localhost:9999");
  }

  @Test
  void importUserFromLdap(CapturedOutput capturedOutput) {
    userService.importUserFromLdap("jdoe");

    assertThat(capturedOutput)
            .contains("More business logic with John DOE of id jdoe")
            .contains("Contact: John DOE <jdoe@corp.com>");
  }
}