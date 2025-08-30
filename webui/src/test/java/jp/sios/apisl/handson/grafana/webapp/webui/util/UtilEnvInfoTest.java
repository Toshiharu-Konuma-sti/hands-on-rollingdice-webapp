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
    final var utilEnvInfo = new UtilEnvInfo();
    assertNotNull(utilEnvInfo, "UtilEnvInfo bean should not be null");
  }

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
