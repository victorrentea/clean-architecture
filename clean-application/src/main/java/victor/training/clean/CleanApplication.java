package victor.training.clean;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
import victor.training.clean.domain.repo.UserRepo;
import victor.training.clean.domain.service.EmailSender;
import victor.training.clean.domain.service.NotificationService;

@EnableAsync
@SpringBootApplication
@RequiredArgsConstructor
@Import(NotificationService.class)
public class CleanApplication {

//  @Bean
//  NotificationService notificationService(UserRepo userRepo, EmailSender emailSender) {
//    return new NotificationService(emailSender, userRepo);
//  }

  @Bean
  public RestTemplate rest() {
    return new RestTemplate();
  }

  public static void main(String[] args) {
    SpringApplication.run(CleanApplication.class, args);
  }
}

