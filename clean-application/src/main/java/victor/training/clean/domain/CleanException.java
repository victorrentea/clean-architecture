package victor.training.clean.domain;

public class CleanException extends RuntimeException {
  public enum ErrorCode {
    GENERAL,
    CUSTOMER_NAME_TOO_SHORT(400),
    DUPLICATED_CUSTOMER_EMAIL(400);

    public final int statusCode;
    ErrorCode() {
      this(500);
    }
    ErrorCode(int statusCode) {
      this.statusCode = statusCode;
    }
  }

  private final ErrorCode errorCode;
  private final String[] parameters;

  public CleanException(String message) {
    this(message, ErrorCode.GENERAL);
  }

  public CleanException(Throwable cause) {
    this(cause, ErrorCode.GENERAL);
  }

  public CleanException(ErrorCode errorCode, String... parameters) {
    this.errorCode = errorCode;
    this.parameters = parameters;
  }

  public CleanException(String message, ErrorCode errorCode, String... parameters) {
    super(message);
    this.errorCode = errorCode;
    this.parameters = parameters;
  }

  public CleanException(String message, Throwable cause, ErrorCode errorCode, String... parameters) {
    super(message, cause);
    this.errorCode = errorCode;
    this.parameters = parameters;
  }

  public CleanException(Throwable cause, ErrorCode errorCode, String... parameters) {
    super(cause);
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
