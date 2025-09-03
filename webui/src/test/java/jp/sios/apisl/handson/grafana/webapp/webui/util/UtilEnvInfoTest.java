package jp.sios.apisl.handson.grafana.webapp.webui.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

/**
 * UtilEnvInfoクラスのユーティリティメソッドに対する単体テストを提供するテストクラスです。.
 * 
 * <p>主に、リクエストのURL取得やログ出力処理が例外を投げずに正常に動作することを検証します。
 * </p>
 * <ul>
 *   <li>UtilEnvInfoインスタンスの生成確認</li>
 *   <li>リクエスト開始・終了時のログ出力メソッドの例外発生有無</li>
 *   <li>現在のリクエストURL取得メソッドの正常系・異常系動作確認</li>
 *   <li>クラスメソッドのログ出力メソッドの例外発生有無</li>
 * </ul>
 * 
 * <p>モックを利用してHttpServletRequestの挙動を再現し、各メソッドの堅牢性をテストします。
 * </p>
 */
@SuppressWarnings({"PMD.AtLeastOneConstructor", "PMD.TooManyStaticImports"})
class UtilEnvInfoTest {

  @Test
  void testLogStartRequestDoesNotThrow() {
    final HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost/start"));

    assertDoesNotThrow(() -> UtilEnvInfo.logStartRequest(request));
  }

  @Test
  void testLogFinishRequestDoesNotThrow() {
    final HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost/finish"));

    assertDoesNotThrow(() -> UtilEnvInfo.logFinishRequest(request));
  }

  @Test
  void testGetCurrentUrl() {
    final HttpServletRequest request = mock(HttpServletRequest.class);
    final StringBuffer url = new StringBuffer("http://localhost/test/path");
    when(request.getRequestURL()).thenReturn(url);

    final String result = UtilEnvInfo.getCurrentUrl(request);

    assertEquals("http://localhost/test/path", result, "getCurrentUrl should return the correct URL");
  }

  @Test
  void testGetCurrentUrlWithEmptyUrl() {
    final HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getRequestURL()).thenReturn(new StringBuffer(""));
    final String result = UtilEnvInfo.getCurrentUrl(request);
    assertEquals("", result, "getCurrentUrl should return an empty string when request URL is empty");
  }

  @Test
  void testGetCurrentUrlWithNullRequest() {
    assertThrows(NullPointerException.class, () -> UtilEnvInfo.getCurrentUrl(null));
  }

  @Test
  void testLogStartClassMethodDoesNotThrow() {
    assertDoesNotThrow(UtilEnvInfo::logStartClassMethod);
  }

}
