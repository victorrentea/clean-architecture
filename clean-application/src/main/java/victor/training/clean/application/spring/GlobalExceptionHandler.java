package victor.training.clean.application.spring;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import victor.training.clean.domain.CleanException;
import victor.training.clean.domain.CleanException.ErrorCode;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.status;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {
//  @ResponseStatus(BAD_REQUEST)
//  @ExceptionHandler(MethodArgumentNotValidException.class) // @Validated
//  public List<String> onJavaxValidationException(MethodArgumentNotValidException e) {
//    List<String> validationErrors = e.getBindingResult().getFieldErrors().stream()
//        .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
//        .toList();
//    log.error("Invalid request: {}", validationErrors, e);
//    return validationErrors;
//  }

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
  public void onAnyOtherException(Exception exception, HttpServletResponse response) throws IOException {
//    return handleException(ErrorCode.GENERAL, null, exception);
    exception.printStackTrace(response.getWriter());
  }

}