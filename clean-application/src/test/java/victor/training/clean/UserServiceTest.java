package victor.training.clean;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import victor.training.clean.ApiClient;
import victor.training.clean.domain.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
  void ok(CapturedOutput capturedOutput) {
    userService.importUserFromLdap("full");

    assertThat(capturedOutput)
            .contains("More logic for John DOE of id jdoe")
            .contains("Check this user is not already in my system  jdoe@corp.com")
            .contains("Contact: John DOE <jdoe@corp.com>");
  }
  @Test
  void missingEmail(CapturedOutput capturedOutput) {
    userService.importUserFromLdap("noemail");

    assertThat(capturedOutput)
            .contains("More logic for John DOE of id jdoe")
            .doesNotContain("Contact: John DOE");
  }
  @Test
  void missingUid(CapturedOutput capturedOutput) {
    userService.importUserFromLdap("nouid");

    assertThat(capturedOutput)
            .contains("More logic for John DOE of id anonymous");
  }
}