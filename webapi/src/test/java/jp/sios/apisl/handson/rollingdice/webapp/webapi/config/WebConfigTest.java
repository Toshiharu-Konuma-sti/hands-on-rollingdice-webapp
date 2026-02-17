package jp.sios.apisl.handson.rollingdice.webapp.webapi.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@link WebConfig} の単体テストクラス.
 *
 * <p>MockMvcを使用して、実際にCORSヘッダーが適用されるかを検証します。</p>
 */
@WebMvcTest(controllers = WebConfigTest.DummyController.class)
@Import(WebConfig.class) // テスト対象の設定クラスをインポート
@TestPropertySource(properties = {
    "app.cors.allowed-origins=http://localhost:3000,http://example.com"
})
@SuppressWarnings("PMD.AtLeastOneConstructor")
class WebConfigTest {

  /**
   * Spring MVCのコントローラーをテストするためのツールで、HTTPリクエストをシミュレートし、レスポンスを検証できます.
   */
  @Autowired
  private MockMvc mockMvc;

  /**
   * 許可されたオリジンからのPreflightリクエストが成功し、適切なヘッダーが返却されることを検証します.
   */
  @Test
  @DisplayName("許可されたオリジンからのアクセスの場合、CORSヘッダーが返却されること")
  @SuppressWarnings("PMD.LawOfDemeter")
  void testCorsWithAllowedOrigin() throws Exception {
    final String allowedOrigin = "http://localhost:3000";

    final MvcResult result = mockMvc.perform(options("/test-endpoint")
        .header(HttpHeaders.ORIGIN, allowedOrigin)
        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET"))
        .andExpect(status().isOk()) // 200 OK
        .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, allowedOrigin))
        .andExpect(header().string(
            HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET,POST,PUT,DELETE,OPTIONS"))
        .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true"))
        .andReturn();

    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus(),
        "許可されたオリジンのため、ステータスコードは200 OKである必要があります");
  }

  /**
   * 許可されていないオリジンからのリクエストが拒否される（または403になる）ことを検証します.
   *
   * <p>Spring MVCのデフォルト動作では、許可されていないOriginの場合、403 Forbiddenを返します。</p>
   */
  @Test
  @DisplayName("許可されていないオリジンからのアクセスの場合、403 Forbiddenとなること")
  @SuppressWarnings("PMD.LawOfDemeter")
  void testCorsWithNotAllowedOrigin() throws Exception {
    final String notAllowedOrigin = "http://evil.com";

    final MvcResult result = mockMvc.perform(options("/test-endpoint")
        .header(HttpHeaders.ORIGIN, notAllowedOrigin)
        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET"))
        .andReturn();

    assertEquals(HttpStatus.FORBIDDEN.value(), result.getResponse().getStatus(),
        "許可されていないオリジンのため、ステータスコードは403 Forbiddenである必要があります");
  }

  /**
   * テスト用のダミーコントローラー.
   *
   * <p>CORS設定を検証するためのエンドポイントを提供します。</p>
   */
  @RestController
  /* default */ static class DummyController {
    /**
     * テストリクエストを受け付けるダミーのエンドポイント.
     */
    @GetMapping("/test-endpoint")
    public void dummyEndpoint() {
            // テスト用のため何もしない
    }
  }
}