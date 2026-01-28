package jp.sios.apisl.handson.rollingdice.webapp.webapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import jp.sios.apisl.handson.rollingdice.webapp.webapi.dto.DiceValueDto;
import jp.sios.apisl.handson.rollingdice.webapp.webapi.entity.DiceEntity;
import jp.sios.apisl.handson.rollingdice.webapp.webapi.service.WebApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
@SuppressWarnings("PMD.CommentSize")
class WebApiControllerTest {

  /**
   * A constant representing the mock URL for the current request.
   */
  private static final String MOCK_CURRENT_URL = "http://localhost:8182/api/v1/dices";

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
    final Optional<Integer> optSleep = Optional.of(3);
    final Optional<Integer> optLoop = Optional.of(5);
    final Optional<Boolean> optError = Optional.empty();
    final DiceValueDto mockResponse = new DiceValueDto(6);

    when(request.getRequestURL()).thenReturn(new StringBuffer(MOCK_CURRENT_URL));
    when(service.rollDice(optSleep, optLoop, optError, null)).thenReturn(mockResponse);

    // Act
    final DiceValueDto response = controller.rollDice(
        request, null, optSleep, optLoop, optError);

    // Assert
    assertEquals(6, response.value(), 
        "The response value should match the expected dice value");
    verify(service, times(1)).rollDice(optSleep, optLoop, optError, null);
  }

  @Test
  void testRollDiceWithRequestBody() {
    // Arrange
    final DiceValueDto requestBody = new DiceValueDto(4);
    final Optional<Integer> optSleep = Optional.empty();
    final Optional<Integer> optLoop = Optional.empty();
    final Optional<Boolean> optError = Optional.empty();
    final DiceValueDto mockResponse = new DiceValueDto(4);

    when(request.getRequestURL()).thenReturn(new StringBuffer(MOCK_CURRENT_URL));
    when(service.rollDice(optSleep, optLoop, optError, requestBody)).thenReturn(mockResponse);

    // Act
    final DiceValueDto response = controller.rollDice(
        request, requestBody, optSleep, optLoop, optError);

    // Assert
    assertEquals(4, response.value(), 
        "The response value should match the fixed dice value from request body");
    verify(service, times(1)).rollDice(optSleep, optLoop, optError, requestBody);
  }

  @Test
  void testRollDiceWithAllParameters() {
    // Arrange
    final DiceValueDto requestBody = new DiceValueDto(2);
    final Optional<Integer> optSleep = Optional.of(1);
    final Optional<Integer> optLoop = Optional.of(2);
    final Optional<Boolean> optError = Optional.of(false);
    final DiceValueDto mockResponse = new DiceValueDto(2);

    when(request.getRequestURL()).thenReturn(new StringBuffer(MOCK_CURRENT_URL));
    when(service.rollDice(optSleep, optLoop, optError, requestBody)).thenReturn(mockResponse);

    // Act
    final DiceValueDto response = controller.rollDice(
        request, requestBody, optSleep, optLoop, optError);

    // Assert
    assertEquals(2, response.value(), 
        "The response value should match the expected dice value");
    verify(service, times(1)).rollDice(optSleep, optLoop, optError, requestBody);
  }

  @Test
  void testListDice() {
    // Arrange
    final List<DiceEntity> mockDiceList = List.of(
        new DiceEntity(3, 1, LocalDateTime.of(2026, 3, 1, 12, 34, 56)), 
        new DiceEntity(2, 3, LocalDateTime.of(2026, 2, 1, 12, 34, 56)), 
        new DiceEntity(1, 5, LocalDateTime.of(2026, 1, 1, 12, 34, 56)));

    when(request.getRequestURL()).thenReturn(new StringBuffer(MOCK_CURRENT_URL));
    when(service.listDice()).thenReturn(mockDiceList);

    // Act
    final List<DiceEntity> result = controller.listDice(request);

    // Assert
    assertEquals(3, result.size(), "The result list size should be 3");
    verify(service, times(1)).listDice();
  }

}