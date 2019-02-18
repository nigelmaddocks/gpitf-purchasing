package uk.nhs.gpitf.purchasing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ProcurementNotFoundException extends Exception {

  private static final long serialVersionUID = 8656179594990810247L;

  public ProcurementNotFoundException(String message) {
    super(message);
  }

  public ProcurementNotFoundException(Throwable cause) {
    super(cause);
  }

  public ProcurementNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public ProcurementNotFoundException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
