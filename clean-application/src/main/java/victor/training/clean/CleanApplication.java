package victor.training.clean;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import victor.training.clean.user.service.UserService;

@EnableSwagger2
@EnableAsync
@SpringBootApplication
@RequiredArgsConstructor
public class CleanApplication {

   @Bean
   public RestTemplate rest() {
      return new RestTemplate();
   }

//   @Bean
//   public LdapApi ldapApi() {
//      // TODO setup auth on ldapApi.getApiClient()....
//      return new LdapApi();
//   }

   public static void main(String[] args) {
      SpringApplication.run(CleanApplication.class, args);
   }

   public void someUsage() {
      UserService userService = null;
      userService.importUserFromLdap("a");
   }

}

