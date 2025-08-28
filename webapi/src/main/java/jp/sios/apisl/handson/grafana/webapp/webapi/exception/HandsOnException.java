package jp.sios.apisl.handson.grafana.webapp.webapi.exception;

/**
 * ハンズオンアプリケーションで発生する例外を表すカスタム例外クラスです。.
 *
 * <p>この例外は、アプリケーション固有のエラーが発生した場合にスローされます。
 * </p>
 */
public class HandsOnException extends Exception {

  private static final long serialVersionUID = 1L;

  // {{{ public HandsOnException(String message)
  /**
   * 指定されたメッセージを持つHandsOnExceptionを生成します。.
   *
   * @param message 例外の詳細メッセージ
   */
  public HandsOnException(String message) {
    super(message);
  }
  // }}}

}