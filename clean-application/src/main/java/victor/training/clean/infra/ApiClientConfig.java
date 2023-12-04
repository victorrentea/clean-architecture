package victor.training.clean.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import victor.training.clean.ApiClient;

@Configuration
public class ApiClientConfig {
  @Autowired
  private ApiClient apiClient;
  @Value("${ldap.client.base.url}")
  private String baseUrl;
  @EventListener(ApplicationStartedEvent.class)
  public void configureLdapClient() {
    apiClient.setBasePath(baseUrl);
  }
}
