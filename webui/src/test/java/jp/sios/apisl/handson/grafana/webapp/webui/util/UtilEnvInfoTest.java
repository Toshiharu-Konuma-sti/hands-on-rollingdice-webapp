package jp.sios.apisl.handson.grafana.webapp.webui.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

class UtilEnvInfoTest {

  @Test
  void testUtilEnvInfoBeanExists() {
    var utilEnvInfo = new UtilEnvInfo();
    assertNotNull(utilEnvInfo, "UtilEnvInfo bean should not be null");
  }

  @Test
  void testLogStartRequestDoesNotThrow() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost/start"));

    assertDoesNotThrow(() -> UtilEnvInfo.logStartRequest(request));
  }

  @Test
  void testLogFinishRequestDoesNotThrow() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost/finish"));

    assertDoesNotThrow(() -> UtilEnvInfo.logFinishRequest(request));
  }

  @Test
  void testGetCurrentUrl() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    StringBuffer url = new StringBuffer("http://localhost/test/path");
    when(request.getRequestURL()).thenReturn(url);

    String result = UtilEnvInfo.getCurrentUrl(request);

    assertEquals("http://localhost/test/path", result);
  }

  @Test
  void testGetCurrentUrlWithEmptyUrl() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getRequestURL()).thenReturn(new StringBuffer(""));
    String result = UtilEnvInfo.getCurrentUrl(request);
    assertEquals("", result);
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
