package victor.training.clean.application.controller.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.lang.annotation.Retention;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@NotNull
@Size(min = 5)
public @interface ValidCustomerName {
}
