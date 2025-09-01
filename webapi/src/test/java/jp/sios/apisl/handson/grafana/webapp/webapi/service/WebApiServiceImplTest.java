package jp.sios.apisl.handson.grafana.webapp.webapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import jp.sios.apisl.handson.grafana.webapp.webapi.entity.Dice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;

class WebApiServiceImplTest {

  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private WebApiServiceImpl webApiService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testRollDice() {
    final Optional<String> optSleep = Optional.empty();
    final Optional<String> optLoop = Optional.empty();
    final Optional<String> optError = Optional.empty();

    final ResponseEntity<Integer> response = webApiService.rollDice(optSleep, optLoop, optError);

    assertEquals(200, response.getStatusCode().value());
    assertTrue(response.getBody() >= 1 && response.getBody() <= 6);
  }

  @Test
  void testRollDice_WithSleep() {
    final Optional<String> optSleep = Optional.of("100");
    final Optional<String> optLoop = Optional.empty();
    final Optional<String> optError = Optional.empty();

    final ResponseEntity<Integer> response = webApiService.rollDice(optSleep, optLoop, optError);

    assertEquals(200, response.getStatusCode().value());
    assertTrue(response.getBody() >= 1 && response.getBody() <= 6);
  }

  @Test
  void testRollDice_WithInvalidSleep() {
    final Optional<String> optSleep = Optional.of("invalid");
    final Optional<String> optLoop = Optional.empty();
    final Optional<String> optError = Optional.empty();

    final ResponseEntity<Integer> response = webApiService.rollDice(optSleep, optLoop, optError);

    assertEquals(200, response.getStatusCode().value());
    assertTrue(response.getBody() >= 1 && response.getBody() <= 6);
  }

  @Test
  void testRollDice_WithLoop() {
    final Optional<String> optSleep = Optional.empty();
    final Optional<String> optLoop = Optional.of("100");
    final Optional<String> optError = Optional.empty();

    final ResponseEntity<Integer> response = webApiService.rollDice(optSleep, optLoop, optError);

    assertEquals(200, response.getStatusCode().value());
    assertTrue(response.getBody() >= 1 && response.getBody() <= 6);
  }

  @Test
  void testRollDice_WithInvalidLoop() {
    final Optional<String> optSleep = Optional.empty();
    final Optional<String> optLoop = Optional.of("invalid");
    final Optional<String> optError = Optional.empty();

    final ResponseEntity<Integer> response = webApiService.rollDice(optSleep, optLoop, optError);

    assertEquals(200, response.getStatusCode().value());
    assertTrue(response.getBody() >= 1 && response.getBody() <= 6);
  }

  @Test
  void testRollDice_WithError() {
    final Optional<String> optSleep = Optional.empty();
    final Optional<String> optLoop = Optional.empty();
    final Optional<String> optError = Optional.of("true");

    final ResponseEntity<Integer> response = webApiService.rollDice(optSleep, optLoop, optError);

    assertEquals(500, response.getStatusCode().value());
    assertEquals(0, response.getBody());
  }

  @Test
  void testListDice() {
    final Map<String, Object> record = new ConcurrentHashMap<>();
    record.put("id", 1);
    record.put("value", 5);
    record.put("updated_at", LocalDateTime.now());
    final List<Map<String, Object>> mockResult = new ArrayList<>();
    mockResult.add(record);

    when(jdbcTemplate.queryForList(anyString())).thenReturn(mockResult);

    final List<Dice> diceList = webApiService.listDice();

    assertEquals(1, diceList.size());
    assertEquals(5, diceList.get(0).value());
  }

}