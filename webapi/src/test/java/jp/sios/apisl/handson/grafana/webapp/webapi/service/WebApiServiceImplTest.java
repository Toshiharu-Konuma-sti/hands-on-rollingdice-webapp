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

@SuppressWarnings("PMD.LawOfDemeter")
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

    assertEquals(200, response.getStatusCode().value(), "Status code should be 200 on success");
    assertTrue(response.getBody() >= 1 && response.getBody() <= 6, "Dice value should be between 1 and 6");
  }

  @Test
  void testRollDiceWithSleep() {
    final Optional<String> optSleep = Optional.of("100");
    final Optional<String> optLoop = Optional.empty();
    final Optional<String> optError = Optional.empty();

    final ResponseEntity<Integer> response = webApiService.rollDice(optSleep, optLoop, optError);

    assertEquals(200, response.getStatusCode().value(), "Status code should be 200 when sleep parameter is provided");
    assertTrue(response.getBody() >= 1 && response.getBody() <= 6, "Dice value should be between 1 and 6 when sleep parameter is provided");
  }

  @Test
  void testRollDiceWithInvalidSleep() {
    final Optional<String> optSleep = Optional.of("invalid");
    final Optional<String> optLoop = Optional.empty();
    final Optional<String> optError = Optional.empty();

    final ResponseEntity<Integer> response = webApiService.rollDice(optSleep, optLoop, optError);

    assertEquals(200, response.getStatusCode().value(), "Status code should be 200 when invalid sleep parameter is provided");
    assertTrue(response.getBody() >= 1 && response.getBody() <= 6, "Dice value should be between 1 and 6 when invalid sleep parameter is provided");
  }

  @Test
  void testRollDiceWithLoop() {
    final Optional<String> optSleep = Optional.empty();
    final Optional<String> optLoop = Optional.of("100");
    final Optional<String> optError = Optional.empty();

    final ResponseEntity<Integer> response = webApiService.rollDice(optSleep, optLoop, optError);

    assertEquals(200, response.getStatusCode().value(), "Status code should be 200 when loop parameter is provided");
    assertTrue(response.getBody() >= 1 && response.getBody() <= 6, "Dice value should be between 1 and 6 when loop parameter is provided");
  }

  @Test
  void testRollDiceWithInvalidLoop() {
    final Optional<String> optSleep = Optional.empty();
    final Optional<String> optLoop = Optional.of("invalid");
    final Optional<String> optError = Optional.empty();

    final ResponseEntity<Integer> response = webApiService.rollDice(optSleep, optLoop, optError);

    assertEquals(200, response.getStatusCode().value(), "Status code should be 200 when invalid loop parameter is provided");
    assertTrue(response.getBody() >= 1 && response.getBody() <= 6, "Dice value should be between 1 and 6 when invalid loop parameter is provided");
  }

  @Test
  void testRollDiceWithError() {
    final Optional<String> optSleep = Optional.empty();
    final Optional<String> optLoop = Optional.empty();
    final Optional<String> optError = Optional.of("true");

    final ResponseEntity<Integer> response = webApiService.rollDice(optSleep, optLoop, optError);

    assertEquals(500, response.getStatusCode().value(), "Status code should be 500 when error parameter is provided");
    assertEquals(0, response.getBody(), "Dice value should be 0 when error parameter is provided");
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

    assertEquals(1, diceList.size(), "Dice list size should be 1");
    assertEquals(5, diceList.get(0).value(), "Dice value should be 5");
  }

}