package jp.sios.apisl.handson.grafana.webapp.webui.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import jp.sios.apisl.handson.grafana.webapp.webui.util.UtilEnvInfo;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

@SuppressWarnings("PMD.AtLeastOneConstructor")
class WebUiServiceImplTest {

  @Mock
  private RestClient restClient;

  @Mock
  private HttpServletRequest request;

  private WebUiServiceImpl webUiService;

  @Value("${handson.webapp.webapi.host}")
  final private String webapiHost = "localhost";

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
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess().body(testResponse));
    this.restClient = restClientBuilder.build();
    this.webUiService = new WebUiServiceImpl(this.restClient);

    final Optional<String> optSleep = Optional.empty();
    final Optional<String> optLoop = Optional.empty();
    final Optional<String> optError = Optional.empty();

    final String response = webUiService.callRollDiceApi(optSleep, optLoop, optError);

    assertNotNull(response, "response should not be null");
    assertEquals(testResponse, response, "response should match the expected testResponse");
  }

  @Test
  void testCallRollDiceApi_SleepAndLoop() {

    final String testUrl = "http://null/api/dice/v1/roll?sleep=1000&loop=5";
    final String testResponse = "2";

    final RestClient.Builder restClientBuilder = RestClient.builder();
    final MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
    mockServer.expect(requestTo(testUrl))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess().body(testResponse));
    this.restClient = restClientBuilder.build();
    this.webUiService = new WebUiServiceImpl(this.restClient);

    final Optional<String> optSleep = Optional.of("1000");
    final Optional<String> optLoop = Optional.of("5");
    final Optional<String> optError = Optional.empty();

    final String response = webUiService.callRollDiceApi(optSleep, optLoop, optError);

    assertNotNull(response, "response should not be null");
    assertEquals(testResponse, response, "response should match the expected testResponse");
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

  @Test
  void testCallRollDiceApi_WhenApiReturnsServerError() {
    // reproduce the situation where the API returns a server error (500)
    final String testUrl = "http://null/api/dice/v1/roll";

    final RestClient.Builder restClientBuilder = RestClient.builder();
    final MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
    // return a server error
    mockServer.expect(requestTo(testUrl))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withServerError());

    this.restClient = restClientBuilder.build();
    this.webUiService = new WebUiServiceImpl(this.restClient);

    final String response = webUiService.callRollDiceApi(
        Optional.empty(), Optional.empty(), Optional.empty());

    assertNotNull(response, "response should not be null when API returns server error");
    assertEquals("0", response, "response should be '0' when API returns server error");
  }

  @Test
  void testGetCurrentUrl_WithQueryString() {
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
