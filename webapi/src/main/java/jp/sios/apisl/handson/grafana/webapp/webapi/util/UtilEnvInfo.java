package jp.sios.apisl.handson.grafana.webapp.webapi.util;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 環境情報やリクエスト情報のログ出力を支援するユーティリティクラスです。.
 *
 * <p>主にリクエストの開始・終了や、クラス・メソッド呼び出し時の情報をログ出力するための
 * 静的メソッドを提供します。
 * </p>
 * <ul>
 *   <li>リクエストのURLやラベル付きログ出力</li>
 *   <li>呼び出し元クラス名・メソッド名の取得とログ出力</li>
 * </ul>
 *
 * <p>このクラスはインスタンス化せず、すべてのメソッドはstaticとして利用します。</p>
 */
public class UtilEnvInfo {

  private static final Logger logger = LoggerFactory.getLogger(UtilEnvInfo.getClassName());

  // {{{ public static void logStartRequest(HttpServletRequest request)
  /**
   * リクエストの開始をログに記録します。.
   *
   * @param request ログ記録対象のHTTPリクエスト
   */
  public static void logStartRequest(HttpServletRequest request) {
    UtilEnvInfo.logRequestWithLabel("START", request);
  }
  // }}}

  // {{{ public static void logFinishRequest(HttpServletRequest request)
  /**
   * リクエストの処理完了時に、"FINISH"ラベル付きでリクエスト情報をログ出力します。.
   *
   * @param request ログ出力対象のHTTPリクエスト
   */
  public static void logFinishRequest(HttpServletRequest request) {
    UtilEnvInfo.logRequestWithLabel("FINISH", request);
  }
  // }}}

  // {{{ private static void logRequestWithLabel(String label, HttpServletRequest request)
  private static void logRequestWithLabel(String label, HttpServletRequest request) {
    String url = UtilEnvInfo.getCurrentUrl(request);
    logger.info("### {} ### {} ###", label, url);
  }
  // }}}

  // {{{ public static String getCurrentUrl(HttpServletRequest request)
  /**
   * 現在のリクエストのURLを取得します。.
   *
   * @param request 現在のHTTPリクエスト
   * @return リクエストされたURLの文字列
   */
  public static String getCurrentUrl(HttpServletRequest request) {
    String currentUrl = request.getRequestURL().toString();
    return currentUrl;
  }
  // }}}

  // {{{ public static void logStartClassMethod()
  /**
   * 現在実行中のクラス名とメソッド名を取得し、ログに出力します。.
   *
   * <p>ログの出力形式は「>>> calling: クラス名#メソッド名()」となります。
   * 主にデバッグやトレース目的で、メソッドの開始時に呼び出してください。</p>
   */
  public static void logStartClassMethod() {
    String className = UtilEnvInfo.getClassName();
    String methodName = UtilEnvInfo.getMethodName();
    logger.info(">>> calling: {}#{}()", className, methodName);
  }
  // }}}

  // {{{ private static String getClassName()
  private static String getClassName() {
    String className = Thread.currentThread().getStackTrace()[3].getClassName();
    return className;
  }
  // }}}

  // {{{ private static String getMethodName()
  private static String getMethodName() {
    String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
    return methodName;
  }
  // }}}

}