package jp.sios.apisl.handson.rollingdice.webapp.webapi.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class HandsOnExceptionTest {

  @Test
  public void testHandsOnExceptionWithMessage() {
    String message = "Test error message";
    HandsOnException exception = new HandsOnException(message);

    assertEquals(message, exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
  public void testHandsOnExceptionWithMessageAndCause() {
    String message = "Test error message";
    Throwable cause = new RuntimeException("Original cause");
    HandsOnException exception = new HandsOnException(message, cause);

    assertEquals(message, exception.getMessage());
    assertEquals(cause, exception.getCause());
  }

}
