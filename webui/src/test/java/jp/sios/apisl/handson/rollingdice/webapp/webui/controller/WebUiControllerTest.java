package jp.sios.apisl.handson.rollingdice.webapp.webui.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import jp.sios.apisl.handson.rollingdice.webapp.webui.service.WebUiService;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;

/**
 * WebUiControllerの単体テストを行うクラスです。.
 * 
 * <p>主にWebUiControllerのindexメソッドの動作検証を目的とし、依存するWebUiServiceやHttpServletRequestをモック化して
 * コントローラの振る舞いをテストします。
 * </p>
 * 
 * <p>Mockitoを利用して依存オブジェクトの挙動を制御し、ModelAndViewの内容やサービス呼び出しの検証を行います。
 * </p>
 */
@SuppressWarnings("PMD.AtLeastOneConstructor")
class WebUiControllerTest {

  /**
   * Web UIのサービス層を表すフィールドです。
   * コントローラからサービス層のメソッドを呼び出すために使用します。.
   */
  @Mock
  private WebUiService service;

  /**
   * テストで使用するHTTPリクエストオブジェクト。
   * モック化されたリクエスト情報を保持します。.
   */
  @Mock
  private HttpServletRequest request;

  /**
   * テスト対象となるWebUiControllerのインスタンス。.
   */
  @InjectMocks
  private WebUiController controller;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testIndex() {
    // Arrange
    final ModelAndView model = new ModelAndView();
    final Optional<String> optSleep = Optional.of("1000");
    final Optional<String> optLoop = Optional.of("5");
    final Optional<String> optError = Optional.of("false");
    final Optional<Integer> optFixedValue = Optional.empty();

    final String mockDice = "6";
    final JSONArray mockDiceList = new JSONArray("[1, 2, 3, 4, 5, 6]");
    final String mockCurrentUrl = "http://localhost:8080";

    when(service.callRollDiceApi(optSleep, optLoop, optError, optFixedValue)).thenReturn(mockDice);
    when(service.callListDiceApi()).thenReturn(mockDiceList);
    when(request.getRequestURL()).thenReturn(new StringBuffer(mockCurrentUrl));
    when(service.getCurrentUrl(request)).thenReturn(mockCurrentUrl);

    // Act
    final ModelAndView result = controller.index(request, model, optSleep, optLoop, optError);

    // Assert
    assertEquals("index", result.getViewName(), "View name should be 'index'");
    assertEquals(mockDice, result.getModel().get("dice"), "Dice value should match the mockDice");
    assertEquals(mockDiceList, result.getModel().get("list"), "Dice list should match the mockDiceList");
    assertEquals(mockCurrentUrl, result.getModel().get("cUrl"), "Current URL should match the mockCurrentUrl");

    verify(service).callRollDiceApi(optSleep, optLoop, optError, optFixedValue);
    verify(service).callListDiceApi();
    verify(service).getCurrentUrl(request);
  }

}

