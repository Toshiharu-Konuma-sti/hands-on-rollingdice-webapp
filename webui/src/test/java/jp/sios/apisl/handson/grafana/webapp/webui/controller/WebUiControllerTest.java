package jp.sios.apisl.handson.grafana.webapp.webui.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import jp.sios.apisl.handson.grafana.webapp.webui.service.WebUiService;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;

class WebUiControllerTest {

  @Mock
  private WebUiService service;

  @Mock
  private HttpServletRequest request;

  @InjectMocks
  private WebUiController controller;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testIndex() {
    // Arrange
    ModelAndView model = new ModelAndView();
    Optional<String> optSleep = Optional.of("1000");
    Optional<String> optLoop = Optional.of("5");
    Optional<String> optError = Optional.of("false");

    String mockDice = "6";
    JSONArray mockDiceList = new JSONArray("[1, 2, 3, 4, 5, 6]");
    String mockCurrentUrl = "http://localhost:8080";

    when(service.callRollDiceApi(optSleep, optLoop, optError)).thenReturn(mockDice);
    when(service.callListDiceApi()).thenReturn(mockDiceList);
    when(request.getRequestURL()).thenReturn(new StringBuffer(mockCurrentUrl));
    when(service.getCurrentUrl(request)).thenReturn(mockCurrentUrl);

    // Act
    ModelAndView result = controller.index(request, model, optSleep, optLoop, optError);

    // Assert
    assertEquals("index", result.getViewName());
    assertEquals(mockDice, result.getModel().get("dice"));
    assertEquals(mockDiceList, result.getModel().get("list"));
    assertEquals(mockCurrentUrl, result.getModel().get("cUrl"));

    verify(service).callRollDiceApi(optSleep, optLoop, optError);
    verify(service).callListDiceApi();
    verify(service).getCurrentUrl(request);
  }

}

