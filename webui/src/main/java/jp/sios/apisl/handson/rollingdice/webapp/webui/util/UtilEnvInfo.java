package jp.sios.apisl.handson.rollingdice.webapp.webui.util;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 環境情報やリクエスト情報のログ出力を支援するユーティリティクラスです。.
 *
 * <p>主に以下の機能を提供します:</p>
 * <ul>
 *   <li>リクエストの開始、および終了時にリクエスト情報のログ出力</li>
 *   <li>リクエストされているURLの取得</li>
 *   <li>実行中のクラス名・メソッド名のログ出力</li>
 * </ul>
 *
 * <p>このクラスはインスタンス化せず、すべてのメソッドはstaticとして利用します。</p>
 */
@SuppressWarnings({"PMD.CommentSize"})
public final class UtilEnvInfo {

  /**
   * ログ出力を行うためのLoggerインスタンス。
   * クラス名を指定して初期化され、アプリケーションの動作状況やエラー情報を記録します。.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(getClassName());

  private UtilEnvInfo() {
    // Prevents instantiation.
  }

  /**
   * リクエストの開始時に、"START"ラベル付きでリクエスト情報をログに出力します。.
   *
   * @param request 情報取得対象のHTTPリクエスト
   */
  public static void logStartRequest(final HttpServletRequest request) {
    logRequestWithLabel("START", request);
  }

  /**
   * リクエストの完了時に、"FINISH"ラベル付きでリクエスト情報をログに出力します。.
   *
   * @param request 情報取得対象のHTTPリクエスト
   */
  public static void logFinishRequest(final HttpServletRequest request) {
    logRequestWithLabel("FINISH", request);
  }

  private static void logRequestWithLabel(final String label, final HttpServletRequest request) {
    final String url = getCurrentUrl(request);
    LOGGER.info("### {} ### {} ###", label, url);
  }

  /**
   * リクエストされているURLを取得します。.
   *
   * @param request 情報取得対象のHTTPリクエスト
   * @return リクエストされたURLの文字列
   */
  public static String getCurrentUrl(final HttpServletRequest request) {
    return request.getRequestURL().toString();
  }

  /**
   * 実行中のクラス名とメソッド名を取得し、ログに出力します。.
   * 
   * <p>ログは「>>> calling: クラス名#メソッド名()」の形式で出力されます。
   * 主にデバッグやトレース目的でメソッドの開始時に呼び出してください。</p>
   */
  public static void logStartClassMethod() {
    final String className = getClassName();
    final String methodName = getMethodName();
    LOGGER.info(">>> calling: {}#{}()", className, methodName);
  }

  @SuppressWarnings("PMD.DoNotUseThreads")
  private static String getClassName() {
    return Thread.currentThread().getStackTrace()[3].getClassName();
  }

  @SuppressWarnings("PMD.DoNotUseThreads")
  private static String getMethodName() {
    return Thread.currentThread().getStackTrace()[3].getMethodName();
  }

}
