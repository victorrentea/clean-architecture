package victor.training.clean.application.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Size(min = 5, message = "{customer-name-too-short}") // yes
public @interface CustomerName {
}
