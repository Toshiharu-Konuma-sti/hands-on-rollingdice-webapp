package jp.sios.apisl.handson.grafana.webapp.webui.util;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 環境情報やリクエスト情報のログ出力を支援するユーティリティクラスです。.
 *
 * <p>主に以下の機能を提供します:</p>
 * <ul>
 *   <li>リクエスト開始・終了時のURLログ出力</li>
 *   <li>クラス名・メソッド名のログ出力</li>
 *   <li>現在のリクエストURLの取得</li>
 * </ul>
 * ログ出力にはSLF4JのLoggerを利用しています。
 */
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
   * リクエストの処理開始時に、"START"ラベル付きでリクエスト情報をログ出力します。.
   *
   * @param request ログ出力対象のHTTPリクエスト
   */
  public static void logStartRequest(final HttpServletRequest request) {
    logRequestWithLabel("START", request);
  }

  /**
   * リクエストの処理完了時に、"FINISH"ラベル付きでリクエスト情報をログ出力します。.
   *
   * @param request ログ出力対象のHTTPリクエスト
   */
  public static void logFinishRequest(final HttpServletRequest request) {
    logRequestWithLabel("FINISH", request);
  }

  private static void logRequestWithLabel(final String label, final HttpServletRequest request) {
    final String url = getCurrentUrl(request);
    LOGGER.info("### {} ### {} ###", label, url);
  }

  /**
   * 現在リクエストされているURLを取得します。.
   *
   * @param request 現在のHTTPリクエスト
   * @return リクエストされたURLの文字列
   */
  public static String getCurrentUrl(final HttpServletRequest request) {
    return request.getRequestURL().toString();
  }

  /**
   * 現在実行中のクラス名とメソッド名をログに出力します。.
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
