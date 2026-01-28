package jp.sios.apisl.handson.rollingdice.webapp.webui.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import jp.sios.apisl.handson.rollingdice.webapp.webui.dto.DiceHistoryDto;
import jp.sios.apisl.handson.rollingdice.webapp.webui.service.WebUiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * WebUiControllerの単体テストを行うクラスです。.
 * 
 * <p>主にWebUiControllerのindexメソッドの動作検証を目的とし、
 * 依存するWebUiServiceやHttpServletRequestをモック化して
 * コントローラの振る舞いをテストします。
 * </p>
 * 
 * <p>Mockitoを利用して依存オブジェクトの挙動を制御し、ModelAndViewの内容やサービス呼び出しの検証を行います。
 * </p>
 */
@SuppressWarnings({"PMD.AtLeastOneConstructor", "PMD.CommentSize"})
class WebUiControllerTest {

  /**
   * Web UIのサービス層を表すフィールドです。
   * コントローラからサービス層のメソッドを呼び出すために使用します。.
   */
  @Mock
  private WebUiService service;

  /**
   * テスト対象となるWebUiControllerのインスタンス。.
   */
  @InjectMocks
  private WebUiController controller;

  /**
   * テストで使用するHTTPリクエストのモックオブジェクトです。.
   */
  private MockHttpServletRequest request;

  /**
   * テストで使用するModelAndViewオブジェクトです。.
   */
  private ModelAndView modelAndView;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    request = new MockHttpServletRequest();
    modelAndView = new ModelAndView();
  }

  @Test
  @DisplayName("全てのパラメータが指定された場合、正しくModelに値が設定されindexが返る")
  void testIndexWithAllParameters() {
    // Arrange (準備)
    final Optional<String> optSleep = Optional.of("5");
    final Optional<String> optLoop = Optional.of("3");
    final Optional<String> optError = Optional.of("false");
    final Optional<Integer> optValue = Optional.of(6);

    // モックの戻り値を定義
    final String expectedDice = "6";
    // レコードのコンストラクタ引数に合わせてダミーデータを作成 (ID, Value, Timestamp)
    final List<DiceHistoryDto> expectedList = List.of(
        new DiceHistoryDto(1, 4, "2026-01-28 10:00:00"),
        new DiceHistoryDto(2, 6, "2026-01-28 10:01:00")
    );
    final String expectedUrl = "http://localhost:8080/app?sleep=5";

    // サービスの振る舞いを定義
    when(service.callRollDiceApi(optSleep, optLoop, optError, optValue)).thenReturn(expectedDice);
    when(service.callListDiceApi()).thenReturn(expectedList);
    when(service.getCurrentUrl(request)).thenReturn(expectedUrl);

    // Act (実行)
    final ModelAndView result = controller.index(
        request, modelAndView, optSleep, optLoop, optError, optValue
    );

    // Assert (検証)
    // 1. ビュー名が "index" であること
    assertEquals("index", result.getViewName(), 
        "The view name should be 'index'.");

    // 2. Modelに必要な属性がセットされていること
    assertEquals(expectedDice, result.getModel().get("dice"), 
        "The 'dice' attribute in the model should match the expected value.");    
    assertEquals(expectedList, result.getModel().get("list"), 
        "The 'list' attribute in the model should match the expected list.");
    assertEquals(expectedUrl, result.getModel().get("cUrl"), 
        "The 'cUrl' attribute in the model should match the expected URL.");

    // 3. サービスメソッドが期待通りの引数で呼ばれたこと
    verify(service, times(1)).callRollDiceApi(optSleep, optLoop, optError, optValue);
    verify(service, times(1)).callListDiceApi();
    verify(service, times(1)).getCurrentUrl(request);
  }

  @Test
  @DisplayName("パラメータが全て空(Empty)の場合でも動作すること")
  void testIndexWithEmptyParameters() {
    // Arrange (準備)
    final Optional<String> emptyString = Optional.empty();
    final Optional<Integer> emptyInt = Optional.empty();

    final String expectedDice = "1";
    final List<DiceHistoryDto> expectedList = List.of(); // 空リスト
    final String expectedUrl = "http://localhost:8080/app";

    when(service.callRollDiceApi(emptyString, emptyString, emptyString, emptyInt))
        .thenReturn(expectedDice);
    when(service.callListDiceApi()).thenReturn(expectedList);
    when(service.getCurrentUrl(request)).thenReturn(expectedUrl);

    // Act (実行)
    final ModelAndView result = controller.index(
        request, modelAndView, emptyString, emptyString, emptyString, emptyInt
    );

    // Assert (検証)
    assertEquals("index", result.getViewName(), 
        "The view name should be 'index'.");        
    assertEquals(expectedDice, result.getModel().get("dice"), 
        "The 'dice' attribute in the model should match the expected default value.");
    assertEquals(expectedList, result.getModel().get("list"), 
        "The 'list' attribute in the model should be empty as mocked.");
    
    verify(service).callRollDiceApi(emptyString, emptyString, emptyString, emptyInt);
  }
  
}