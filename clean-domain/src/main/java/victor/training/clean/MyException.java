package victor.training.clean;

public class MyException extends RuntimeException {
   public enum ErrorCode {
      GENERAL,
      NON_SINGLE_LDAP, CUSTOMER_NAME_TOO_SHORT
   }
   private final ErrorCode errorCode;
   private final String[] parameters;

   public MyException(ErrorCode errorCode, String... parameters) {
      this.errorCode = errorCode;
      this.parameters = parameters;
   }

   public MyException(String message, ErrorCode errorCode, String... parameters) {
      super(message);
      this.errorCode = errorCode;
      this.parameters = parameters;
   }

   public MyException(String message, Throwable cause, ErrorCode errorCode, String... parameters) {
      super(message, cause);
      this.errorCode = errorCode;
      this.parameters = parameters;
   }

   public MyException(Throwable cause, ErrorCode errorCode, String... parameters) {
      super(cause);
      this.errorCode = errorCode;
      this.parameters = parameters;
   }

   public MyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode, String... parameters) {
      super(message, cause, enableSuppression, writableStackTrace);
      this.errorCode = errorCode;
      this.parameters = parameters;
   }

   public ErrorCode getErrorCode() {
      return errorCode;
   }

   public String[] getParameters() {
      return parameters;
   }
}
