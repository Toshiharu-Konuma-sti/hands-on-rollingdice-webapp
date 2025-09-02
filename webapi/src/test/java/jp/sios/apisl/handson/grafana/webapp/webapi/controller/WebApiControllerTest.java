package jp.sios.apisl.handson.grafana.webapp.webapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import jp.sios.apisl.handson.grafana.webapp.webapi.entity.Dice;
import jp.sios.apisl.handson.grafana.webapp.webapi.service.WebApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

@SuppressWarnings("PMD.TooManyStaticImports")
class WebApiControllerTest {

  @Mock
  private WebApiService service;

  @Mock
  private HttpServletRequest request;

  @InjectMocks
  private WebApiController controller;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testRollDice() {
    // Arrange
    final String mockCurrentUrl = "http://localhost:8080";
    final Optional<String> optSleep = Optional.of("1000");
    final Optional<String> optLoop = Optional.of("5");
    final Optional<String> optError = Optional.empty();
    final Integer mockDice = 6;
    final ResponseEntity<Integer> mockResponse = ResponseEntity.ok(mockDice);

    when(request.getRequestURL()).thenReturn(new StringBuffer(mockCurrentUrl));
    when(service.rollDice(optSleep, optLoop, optError)).thenReturn(mockResponse);

    // Act
    final ResponseEntity<Integer> response = controller.rollDice(request, optSleep, optLoop, optError);

    // Assert
    assertNotNull(response, "Response should not be null");
    assertEquals(mockDice, response.getBody(), "The response body should match the expected dice value");
    verify(service, times(1)).rollDice(optSleep, optLoop, optError);
  }

  @Test
  void testListDice() {
    // Arrange
    final String mockCurrentUrl = "http://localhost:8080";
    final List<Dice> mockDiceList = List.of(new Dice(3, 1, LocalDateTime.of(2025, 3, 1, 12, 34, 56)), new Dice(2, 3, LocalDateTime.of(2025, 2, 1, 12, 34, 56)), new Dice(1, 5, LocalDateTime.of(2025, 1, 1, 12, 34, 56)));

    when(request.getRequestURL()).thenReturn(new StringBuffer(mockCurrentUrl));
    when(service.listDice()).thenReturn(mockDiceList);

    // Act
    final List<Dice> result = controller.listDice(request);

    // Assert
    assertNotNull(result, "The result list should not be null");
    assertEquals(3, result.size(), "The result list size should be 3");
    verify(service, times(1)).listDice();
  }
}