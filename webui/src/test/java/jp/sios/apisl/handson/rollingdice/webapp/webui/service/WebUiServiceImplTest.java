package jp.sios.apisl.handson.rollingdice.webapp.webui.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import jp.sios.apisl.handson.rollingdice.webapp.webui.util.UtilEnvInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.json.JSONArray;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

/**
 * WebUiServiceImplのユニットテストクラスです。.
 * 
 * <p>このクラスでは、WebUiServiceImplの各種API呼び出しメソッドの動作検証を行います。
 * RestClientやHttpServletRequestのモックを利用し、APIの正常系・異常系のレスポンスや
 * パラメータ付与時の挙動、ユーティリティメソッドの動作確認などを網羅的にテストします。
 * </p>
 * <ul>
 *   <li>サイコロAPIの呼び出し（通常・パラメータ付き・サーバエラー時）</li>
 *   <li>サイコロリストAPIの呼び出し</li>
 *   <li>現在のURL取得メソッドの検証</li>
 * </ul>
 * 
 * <p>モックサーバやMockitoの静的モック機能を活用し、外部依存を排除したテストを実現しています。
 * </p>
 */
@SuppressWarnings({"PMD.AtLeastOneConstructor", "PMD.TooManyStaticImports"})
class WebUiServiceImplTest {

  /**
   * REST APIとの通信を行うためのクライアント。.
   */
  @Mock
  private RestClient restClient;

  /**
   * HTTPリクエスト情報を保持するためのフィールドです。
   * テストケース内でリクエストのモックや検証に使用されます。.
   */
  @Mock
  private HttpServletRequest request;

  /**
   * Web UIサービスの実装クラスのインスタンス。
   * テスト対象となるWebUiServiceImplを保持します。.
   */
  private WebUiServiceImpl webUiService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    webUiService = new WebUiServiceImpl(this.restClient);
  }

  @Test
  void testCallRollDiceApi() {
    final String testUrl = "http://null/api/dice/v1/roll";
    final String testResponse = "1";

    final RestClient.Builder restClientBuilder = RestClient.builder();
    final MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
    mockServer.expect(requestTo(testUrl))
        .andExpect(method(HttpMethod.POST))
        .andRespond(withSuccess().body(testResponse));
    this.restClient = restClientBuilder.build();
    this.webUiService = new WebUiServiceImpl(this.restClient);

    final Optional<String> optSleep = Optional.empty();
    final Optional<String> optLoop = Optional.empty();
    final Optional<String> optError = Optional.empty();
    final Optional<Integer> optFixedValue = Optional.empty();

    final String response = webUiService.callRollDiceApi(optSleep, optLoop, optError, optFixedValue);

    assertNotNull(response, "response should not be null");
    assertEquals(testResponse, response, "response should match the expected testResponse");
  }

/*
  @Test
  void testCallRollDiceApiSleepAndLoop() {

    final String testUrl = "http://null/api/dice/v1/roll?sleep=1000&loop=5";
    final String testResponse = "2";

    final RestClient.Builder restClientBuilder = RestClient.builder();
    final MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
    mockServer.expect(requestTo(testUrl))
        .andExpect(method(HttpMethod.POST))
        .andRespond(withSuccess().body(testResponse));
    this.restClient = restClientBuilder.build();
    this.webUiService = new WebUiServiceImpl(this.restClient);

    final Optional<String> optSleep = Optional.of("1000");
    final Optional<String> optLoop = Optional.of("5");
    final Optional<String> optError = Optional.empty();
    final Optional<Integer> optFixedValue = Optional.empty();

    final String response = webUiService.callRollDiceApi(optSleep, optLoop, optError, optFixedValue);

    assertNotNull(response, "response should not be null");
    assertEquals(testResponse, response, "response should match the expected testResponse");
  }

  @Test
  void testCallRollDiceApiWhenApiReturnsServerError() {
    // reproduce the situation where the API returns a server error (500)
    final String testUrl = "http://null/api/dice/v1/roll";

    final RestClient.Builder restClientBuilder = RestClient.builder();
    final MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
    // return a server error
    mockServer.expect(requestTo(testUrl))
        .andExpect(method(HttpMethod.POST))
        .andRespond(withServerError());

    this.restClient = restClientBuilder.build();
    this.webUiService = new WebUiServiceImpl(this.restClient);

    final String response = webUiService.callRollDiceApi(
        Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

    assertNotNull(response, "response should not be null when API returns server error");
    assertEquals("0", response, "response should be '0' when API returns server error");
  }

  @Test
  void testCallListDiceApi() {

    final String testUrl = "http://null/api/dice/v1/list";
    final String testResponse
        = "[{\"id\":2,\"value\":6,\"updateAt\":\"2025-04-01T13:00:00\"},"
        + "{\"id\":1,\"value\":3,\"updateAt\":\"2025-04-01T12:00:00\"}]";

    final RestClient.Builder restClientBuilder = RestClient.builder();
    final MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
    mockServer.expect(requestTo(testUrl))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess().body(testResponse));
    this.restClient = restClientBuilder.build();
    this.webUiService = new WebUiServiceImpl(this.restClient);

    final JSONArray response = webUiService.callListDiceApi();

    assertNotNull(response, "response should not be null");
    assertEquals(2, response.length(), "response JSONArray length should be 2");
    assertEquals(6, response.getJSONObject(0).getInt("value"), "The value of the first dice should be 6");
  }
*/

  @Test
  void testGetCurrentUrlWithQueryString() {
    // Mock UtilEnvInfo.getCurrentUrl to return expected value
    final String expectedUrl = "http://localhost:8080/test?param=value";
    final HttpServletRequest mockRequest = mock(HttpServletRequest.class);

    // Use reflection or a static mock for UtilEnvInfo if possible
    // Here, we simulate the static method
    try (org.mockito.MockedStatic<UtilEnvInfo> mocked = mockStatic(UtilEnvInfo.class)) {
      mocked.when(() ->
        UtilEnvInfo.getCurrentUrl(mockRequest))
          .thenReturn(expectedUrl);

      final String result = webUiService.getCurrentUrl(mockRequest);

      assertNotNull(result, "result should not be null");
      assertEquals(expectedUrl, result, "result should match the expectedUrl");
    }
  }

}
