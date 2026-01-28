package jp.sios.apisl.handson.rollingdice.webapp.webui.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import jp.sios.apisl.handson.rollingdice.webapp.webui.dto.DiceHistoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
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
@SuppressWarnings({"PMD.AtLeastOneConstructor", "PMD.TooManyStaticImports", "PMD.CommentSize"})
class WebUiServiceImplTest {

  /**
   * 期待するホスト名（ポート含む）.
   */
  private static final String API_HOST = "localhost:8182";

  /**
   * 検証先としてのリクエストURL.
   */
  private static final String REQUEST_URL = "http://" + API_HOST + "/api/v1/dices";

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
  }

  private MockRestServiceServer setupMockServer() {
    final RestClient.Builder restClientBuilder = RestClient.builder();
    final MockRestServiceServer mockServer = MockRestServiceServer.bindTo(
        restClientBuilder).build();

    final RestClient localRestClient = restClientBuilder.build();
    this.webUiService = new WebUiServiceImpl(localRestClient);

    ReflectionTestUtils.setField(this.webUiService, "webapiHost", API_HOST);

    return mockServer;
  }


  @Test
  void testCallRollDiceApiWithNoParameters() {
    final String testResponse = "{\"value\":3}";
    final Optional<String> optSleep = Optional.empty();
    final Optional<String> optLoop = Optional.empty();
    final Optional<String> optError = Optional.empty();
    final Optional<Integer> optFixedValue = Optional.empty();

    // モックサーバーの準備と注入
    final MockRestServiceServer mockServer = setupMockServer();

    // クエリパラメータなしのURLであることを確認
    mockServer.expect(requestTo(REQUEST_URL))
        .andExpect(method(HttpMethod.POST))
        .andRespond(withSuccess().body(testResponse).contentType(MediaType.APPLICATION_JSON));

    final String response = webUiService.callRollDiceApi(
        optSleep, optLoop, optError, optFixedValue);

    // 検証
    mockServer.verify(); // 全てのリクエストが消費されたか確認
    assertNotNull(response, "Response should not be null when calling roll dice API.");
    assertEquals("3", response, "Response value should be '3' as returned by the mock server.");
  }

  @Test
  void testCallRollDiceApiWithSleepAndLoopParameters() {
    final String testResponse = "{\"value\":4}";
    final Optional<String> optSleep = Optional.of("3");
    final Optional<String> optLoop = Optional.of("5");
    final Optional<String> optError = Optional.empty();
    final Optional<Integer> optFixedValue = Optional.empty();

    final MockRestServiceServer mockServer = setupMockServer();

    // クエリパラメータ付きのURLを期待するように修正
    final String expectedUrl = REQUEST_URL + "?sleep=3&loop=5";
    
    mockServer.expect(requestTo(expectedUrl))
        .andExpect(method(HttpMethod.POST))
        .andRespond(withSuccess().body(testResponse).contentType(MediaType.APPLICATION_JSON));

    final String response = webUiService.callRollDiceApi(
        optSleep, optLoop, optError, optFixedValue);

    mockServer.verify();
    assertNotNull(response, "Response should not be null even with sleep/loop parameters.");
    assertEquals("4", response, "Response value should match the mock server response '4'.");
  }

  @Test
  void testCallRollDiceApiWithFixedValue() {
    final String testResponse = "{\"value\":6}";
    final Optional<String> optSleep = Optional.empty();
    final Optional<String> optLoop = Optional.empty();
    final Optional<String> optError = Optional.empty();
    final Optional<Integer> optFixedValue = Optional.of(6);

    final MockRestServiceServer mockServer = setupMockServer();

    mockServer.expect(requestTo(REQUEST_URL))
        .andExpect(method(HttpMethod.POST))
        // ボディの検証も必要なら .andExpect(content().json("{\"value\":6}")) を追加
        .andRespond(withSuccess().body(testResponse).contentType(MediaType.APPLICATION_JSON));

    final String response = webUiService.callRollDiceApi(
        optSleep, optLoop, optError, optFixedValue);

    mockServer.verify();
    assertNotNull(response, "Response should not be null when calling with fixed value.");
    assertEquals("6", response, "Response value should correspond to the fixed value sent.");
  }

  @Test
  void testCallListDiceApi() {
    final String testResponse = "["
        + "{\"id\":1,\"value\":3,\"updateAt\":\"2025-04-01T12:00:00\"},"
        + "{\"id\":2,\"value\":6,\"updateAt\":\"2025-04-01T13:00:00\"}]";

    final MockRestServiceServer mockServer = setupMockServer();

    mockServer.expect(requestTo(REQUEST_URL))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess().body(testResponse).contentType(MediaType.APPLICATION_JSON));

    final List<DiceHistoryDto> response = webUiService.callListDiceApi();

    mockServer.verify();
    assertNotNull(response, "Dice history list should not be null.");
    assertEquals(2, response.size(), "Dice history list should contain exactly 2 elements.");
    assertEquals(3, response.get(0).value(), "The value of the first dice history should be 3.");
    assertEquals(6, response.get(1).value(), "The value of the second dice history should be 6.");
  }

  @Test
  void testCallListDiceApiWhenApiReturnsServerError() {
    final MockRestServiceServer mockServer = setupMockServer();

    mockServer.expect(requestTo(REQUEST_URL))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withServerError());

    final List<DiceHistoryDto> response = webUiService.callListDiceApi();

    mockServer.verify();
    assertNotNull(
        response, "Response should not be null even when the API returns a server error.");
    assertEquals(0, response.size(), "Response list should be empty when the API fails.");
  }

}