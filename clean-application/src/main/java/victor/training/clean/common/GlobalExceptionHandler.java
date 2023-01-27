package victor.training.clean.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import victor.training.clean.CleanException;
import victor.training.clean.CleanException.ErrorCode;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.NoSuchElementException;

import static org.springframework.http.ResponseEntity.internalServerError;
import static org.springframework.http.ResponseEntity.status;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {
   private final MessageSource messageSource;

   @ExceptionHandler(Exception.class)
   public ResponseEntity<String> handleGeneralException(HttpServletRequest request, Exception exception) throws Exception {
      String userMessage = translateError(exception, ErrorCode.GENERAL, null, request);
      return internalServerError().body(userMessage);
   }
   @ResponseStatus(HttpStatus.NOT_FOUND)
   @ExceptionHandler(NoSuchElementException.class)
   public String notFound(HttpServletRequest request, NoSuchElementException exception) throws Exception {
      return "oups";
   }

   @ExceptionHandler(CleanException.class)
   public ResponseEntity<String> handleCleanException(HttpServletRequest request, CleanException cleanException) {
      String userMessage = translateError(cleanException, cleanException.getErrorCode(), cleanException.getParameters(), request);
      String httpStatusCodeStr = messageSource.getMessage("error." + cleanException.getErrorCode() + ".code", null, "500", Locale.ENGLISH);
      int httpStatusCode = Integer.parseInt(httpStatusCodeStr);
      return status(httpStatusCode).body(userMessage);
   }


   private String translateError(Throwable throwable, ErrorCode errorCode, String[] parameters, HttpServletRequest request) {
      String messageKey = "error." + errorCode + ".message";
      String userMessage = messageSource.getMessage(messageKey, parameters, "Internal Error", request.getLocale());
      log.error(String.format("Error occurred [%s]: %s", errorCode, userMessage), throwable);
      return userMessage;
   }

}