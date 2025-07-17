package victor.training.clean;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
import victor.training.clean.domain.model.Customer;
import victor.training.clean.domain.repo.CustomerRepo;
import victor.training.clean.domain.service.NotificationService;

@Import({
    NotificationService.class,
})
@EnableAsync
@SpringBootApplication
@RequiredArgsConstructor
public class CleanApplication {

  @Bean
  public RestTemplate rest() {
    return new RestTemplate();
  }

  public static void main(String[] args) {
    SpringApplication.run(CleanApplication.class, args);
  }
}

