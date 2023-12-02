package victor.training.clean;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;

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

