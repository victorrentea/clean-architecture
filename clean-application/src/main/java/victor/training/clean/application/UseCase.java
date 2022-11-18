package victor.training.clean.application;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@RestController
@Retention(RetentionPolicy.RUNTIME)
public @interface UseCase {

}
