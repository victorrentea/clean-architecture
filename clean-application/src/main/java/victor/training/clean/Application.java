package victor.training.clean;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
import victor.training.clean.service.UserService;

@EnableAsync
@SpringBootApplication
@RequiredArgsConstructor
public class Application {

   @Bean
   public RestTemplate rest() {
      return new RestTemplate();
   }

   public static void main(String[] args) {
      SpringApplication.run(Application.class, args);
   }

//   @Autowired
//   public void configure(ObjectMapper objectMapper) {
//      objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
//   }

   @Autowired
   private UserService userService;
   public void someUsage() {
      userService.importUserFromLdap("a");
   }

}

