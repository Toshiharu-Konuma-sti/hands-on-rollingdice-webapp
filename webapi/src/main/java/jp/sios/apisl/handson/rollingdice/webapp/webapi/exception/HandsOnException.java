package jp.sios.apisl.handson.rollingdice.webapp.webapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ハンズオンアプリケーションで発生する例外を扱うカスタム例外クラスです。.
 *
 * <p>この例外は、アプリケーション固有のエラーが発生した場合にスローされます。
 * </p>
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class HandsOnException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  // {{{ public HandsOnException(String message)
  /**
   * 指定されたメッセージを持つHandsOnExceptionを生成します。.
   *
   * @param message 例外の詳細メッセージ
   */
  public HandsOnException(final String message) {
    super(message);
  }
  // }}}

  /**
   * 指定されたメッセージと原因となった例外を持つHandsOnExceptionを生成します。.
   *
   * @param message 例外の詳細メッセージ
   * @param cause   原因となった例外（InterruptedExceptionなど）
   */
  // {{{
  public HandsOnException(final String message, final Throwable cause) {
    super(message, cause);
  }
  // }}}

}