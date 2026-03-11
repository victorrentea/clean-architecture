package victor.training.clean.application.spring;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import victor.training.clean.domain.CleanException;
import victor.training.clean.domain.CleanException.ErrorCode;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.status;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {
  private static final URI VALIDATION_TYPE = URI.create("https://example.net/validation-error");

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(BindException.class)
  public ProblemDetail onValidationException(BindException e) {
    List<ValidationError> errors = e.getBindingResult().getFieldErrors().stream()
        .map(this::toValidationError)
        .toList();

    List<ValidationError> globalErrors = e.getBindingResult().getGlobalErrors().stream()
        .map(this::toValidationError)
        .toList();

    List<ValidationError> allErrors = List.copyOf(
        Stream.concat(errors.stream(), globalErrors.stream()).toList());

    log.error("Invalid request: {}", allErrors, e);

    ProblemDetail problemDetail = ProblemDetail.forStatus(BAD_REQUEST);
    problemDetail.setType(VALIDATION_TYPE);
    problemDetail.setTitle("Your request is not valid.");
    problemDetail.setProperty("errors", allErrors);
    return problemDetail;
  }

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(ConstraintViolationException.class)
  public ProblemDetail onConstraintViolation(ConstraintViolationException e) {
    List<ValidationError> errors = e.getConstraintViolations().stream()
        .map(violation -> new ValidationError(violation.getMessage(), toPointer(violation.getPropertyPath().toString())))
        .toList();

    log.error("Invalid request: {}", errors, e);

    ProblemDetail problemDetail = ProblemDetail.forStatus(BAD_REQUEST);
    problemDetail.setType(VALIDATION_TYPE);
    problemDetail.setTitle("Your request is not valid.");
    problemDetail.setProperty("errors", errors);
    return problemDetail;
  }

  @ResponseStatus(NOT_FOUND)
  @ExceptionHandler(NoSuchElementException.class) // optional.get() on empty Optional
  public String onNoSuchElementException() {
    return "Not Found!";
  }

  @ExceptionHandler(CleanException.class) // my custom exception
  public ResponseEntity<String> onCleanException(CleanException cleanException) {
    return handleException(cleanException.getErrorCode(), cleanException.getParameters(), cleanException);
  }

  private final MessageSource messageSource;

  private ResponseEntity<String> handleException(ErrorCode errorCode, String[] messageParameters, Exception exception) {
    String userMessage = messageSource.getMessage(
        "error." + errorCode,
        messageParameters, "Internal Error",
        Locale.ENGLISH); // supports i18n of messages based on user's locale

    log.error("Error {}: {}", errorCode, userMessage, exception);
    return status(errorCode.statusCode).body(userMessage);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> onAnyOtherException(Exception exception) {
    return handleException(ErrorCode.GENERAL, null, exception);
  }

  private ValidationError toValidationError(FieldError fieldError) {
    return new ValidationError(fieldError.getDefaultMessage(), toPointer(fieldError.getField()));
  }

  private ValidationError toValidationError(ObjectError objectError) {
    return new ValidationError(objectError.getDefaultMessage(), "#");
  }

  private String toPointer(String fieldPath) {
    if (fieldPath == null || fieldPath.isBlank()) {
      return "#";
    }
    String pointer = fieldPath
        .replaceAll("\\[(\\d+)]", "/$1")
        .replace('.', '/');
    return "#/" + pointer;
  }

  private record ValidationError(String detail, String pointer) {
  }
}