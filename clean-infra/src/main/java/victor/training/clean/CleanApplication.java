package victor.training.clean;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

@EnableAsync
@SpringBootApplication
@RequiredArgsConstructor
public class CleanApplication {

  public static void main(String[] args) {
    SpringApplication.run(CleanApplication.class, args);
  }

  @Bean
  public RestTemplate rest() {
    return new RestTemplate();
  }
}

