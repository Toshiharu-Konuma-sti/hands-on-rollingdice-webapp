package jp.sios.apisl.handson.rollingdice.webapp.webapi.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * {@code HandsOnExceptionTest} クラスは、{@link HandsOnException} クラスのユニットテストを提供します。.
 *
 * @author Toshiharu Konuma
 */
class HandsOnExceptionTest {

  /**
   * HandsOnExceptionTestのコンストラクタです。
   * このクラスのテストインスタンスを初期化します。.
   */
  public HandsOnExceptionTest() {
    // Constructor for HandsOnExceptionTest
  }

  @Test
  void testHandsOnExceptionWithMessage() {
    final String message = "Test error message";
    final HandsOnException exception = new HandsOnException(message);

    assertEquals(message, exception.getMessage(),
        "The exception message should match the provided message.");
    assertNull(exception.getCause(),
        "The exception cause should be null when initialized with only a message.");
  }

  @Test
  void testHandsOnExceptionWithMessageAndCause() {
    final String message = "Test error message";
    final Throwable cause = new RuntimeException("Original cause");
    final HandsOnException exception = new HandsOnException(message, cause);

    assertEquals(message, exception.getMessage(),
        "The exception message should match the provided message.");
    assertEquals(cause, exception.getCause(),
        "The exception cause should match the provided cause.");
  }

}
