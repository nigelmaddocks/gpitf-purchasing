package uk.nhs.gpitf.purchasing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UnauthorizedDataAccessException extends Exception {

  private static final long serialVersionUID = 1092506363158164683L;

  public UnauthorizedDataAccessException(String message) {
    super(message);
  }

  public UnauthorizedDataAccessException(Throwable cause) {
    super(cause);
  }

  public UnauthorizedDataAccessException(String message, Throwable cause) {
    super(message, cause);
  }

  public UnauthorizedDataAccessException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
