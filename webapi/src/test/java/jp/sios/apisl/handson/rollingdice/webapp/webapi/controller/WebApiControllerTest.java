package jp.sios.apisl.handson.rollingdice.webapp.webapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import jp.sios.apisl.handson.rollingdice.webapp.webapi.entity.Dice;
import jp.sios.apisl.handson.rollingdice.webapp.webapi.service.WebApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

/**
 * {@link WebApiController} の単体テストを行うクラスです。.
 *
 * <p>モックを利用して、コントローラの各メソッドが期待通りに動作するかを検証します。
 * </p>
 * <ul>
 *   <li>rollDiceメソッドの正常系動作の検証</li>
 *   <li>listDiceメソッドの正常系動作の検証</li>
 * </ul>
 *
 * <p>各テストケースでは、依存するサービスやリクエストをモック化し、コントローラの振る舞いを確認します。
 * </p>
 *
 * @author Toshiharu Konuma
 */
class WebApiControllerTest {

  /**
   * Web API サービスのインスタンス。
   * コントローラのテストで利用されるモックやスタブのサービスを格納します。.
   */
  @Mock
  private WebApiService service;

  /**
   * テストで使用するHTTPリクエストオブジェクト。
   * モック化してコントローラのリクエスト処理を検証するために利用します。.
   */
  @Mock
  private HttpServletRequest request;

  /**
   * テスト対象となるWebApiControllerのインスタンス。.
   */
  @InjectMocks
  private WebApiController controller;

  /**
   * WebApiControllerTestのコンストラクタです。
   * このクラスのテストインスタンスを初期化します。.
   */
  public WebApiControllerTest() {
    // Constructor for WebApiControllerTest
  }

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testRollDice() {
    // Arrange
    final String mockCurrentUrl = "http://localhost:8080";
    final Optional<Integer> optSleep = Optional.of(1000);
    final Optional<Integer> optLoop = Optional.of(5);
    final Optional<Boolean> optError = Optional.empty();
    final String mockDice = "6";
    final ResponseEntity<String> mockResponse = ResponseEntity.ok(mockDice);

    when(request.getRequestURL()).thenReturn(new StringBuffer(mockCurrentUrl));
    when(service.rollDice(optSleep, optLoop, optError)).thenReturn(mockResponse);

    // Act
    final ResponseEntity<String> response = controller.rollDice(
        request, optSleep, optLoop, optError);

    // Assert
    assertEquals(mockDice, response.getBody(), 
        "The response body should match the expected dice value");
    verify(service, times(1)).rollDice(optSleep, optLoop, optError);
  }

  @Test
  void testListDice() {
    // Arrange
    final String mockCurrentUrl = "http://localhost:8080";
    final List<Dice> mockDiceList = List.of(
        new Dice(3, 1, LocalDateTime.of(2025, 3, 1, 12, 34, 56)), 
        new Dice(2, 3, LocalDateTime.of(2025, 2, 1, 12, 34, 56)), 
        new Dice(1, 5, LocalDateTime.of(2025, 1, 1, 12, 34, 56)));

    when(request.getRequestURL()).thenReturn(new StringBuffer(mockCurrentUrl));
    when(service.listDice()).thenReturn(mockDiceList);

    // Act
    final List<Dice> result = controller.listDice(request);

    // Assert
    assertEquals(3, result.size(), "The result list size should be 3");
    verify(service, times(1)).listDice();
  }
}