package victor.training.clean.vsa;

import com.google.common.annotations.VisibleForTesting;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.persistence.EntityManager;
import org.springframework.web.bind.annotation.RestController;
import victor.training.clean.application.repo.CustomerRepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.join;

@Component
@RequiredArgsConstructor
public class CreateCustomerUseCase {
  private final CustomerRepo customerRepo;

  @VisibleForTesting
  record CreateCustomerMessage(
      String name,
      String email,
      Long countryId) {
  }

//  @RabbitListener // imagine
  public void search(CreateCustomerMessage message) {
    // etc
  }
}
