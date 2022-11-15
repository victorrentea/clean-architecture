package victor.training.clean;

import org.springframework.stereotype.Service;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public @interface DDD {
  @Service
  @Retention(RetentionPolicy.RUNTIME)
  public @interface DomainService {

  }
}
