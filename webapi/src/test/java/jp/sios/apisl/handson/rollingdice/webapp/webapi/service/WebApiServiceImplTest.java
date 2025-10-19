package jp.sios.apisl.handson.rollingdice.webapp.webapi.service;

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
import jp.sios.apisl.handson.rollingdice.webapp.webapi.entity.Dice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * {@code WebApiServiceImplTest} クラスは、{@link WebApiServiceImpl} のユニットテストを提供します。.
 *
 * <p>ダイスを振るAPIの正常系・異常系の動作や、DBからダイス情報を取得する処理のテストを行います。
 * </p>
 * <ul>
 *   <li>rollDiceメソッドの各種パラメータ（sleep, loop, error）に対する挙動を検証します。</li>
 *   <li>listDiceメソッドがDBから正しくデータを取得できるかを検証します。</li>
 * </ul>
 *
 * <p>Mockitoを利用して依存コンポーネント（JdbcTemplate）のモック化を行い、テストの独立性を保っています。
 * </p>
 *
 * @author Toshiharu Konuma
 */
@SuppressWarnings("PMD.LawOfDemeter")
class WebApiServiceImplTest {

  /**
   * データベース操作を行うためのJdbcTemplateインスタンス。
   * SQLクエリの実行やデータ取得などに使用します。.
   */
  @Mock
  private JdbcTemplate jdbcTemplate;

  /**
   * テスト対象となるWebApiServiceImplのインスタンス。.
   */
  @InjectMocks
  private WebApiServiceImpl webApiService;

  /**
   * WebApiServiceImplTestのコンストラクタです。
   * このクラスのテストインスタンスを初期化します。.
   */
  public WebApiServiceImplTest() {
    // Constructor for WebApiServiceImplTest
  }

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testRollDice() {
    final Optional<Integer> optSleep = Optional.empty();
    final Optional<Integer> optLoop = Optional.empty();
    final Optional<Boolean> optError = Optional.empty();

    final ResponseEntity<String> response = webApiService.rollDice(optSleep, optLoop, optError);

    assertEquals(200, response.getStatusCode().value(), "Status code should be 200 on success");
    assertTrue(Integer.parseInt(response.getBody()) >= 1 && Integer.parseInt(response.getBody()) <= 6, "Dice value should be between 1 and 6");
  }

  @Test
  void testRollDiceWithSleep() {
    final Optional<Integer> optSleep = Optional.of(100);
    final Optional<Integer> optLoop = Optional.empty();
    final Optional<Boolean> optError = Optional.empty();

    final ResponseEntity<String> response = webApiService.rollDice(optSleep, optLoop, optError);

    assertEquals(200, response.getStatusCode().value(), "Status code should be 200 when sleep parameter is provided");
    assertTrue(Integer.parseInt(response.getBody()) >= 1 && Integer.parseInt(response.getBody()) <= 6, "Dice value should be between 1 and 6 when sleep parameter is provided");
  }

  @Test
  void testRollDiceWithInvalidSleep() {
    final Optional<Integer> optSleep = Optional.of(-100);
    final Optional<Integer> optLoop = Optional.empty();
    final Optional<Boolean> optError = Optional.empty();

    final ResponseEntity<String> response = webApiService.rollDice(optSleep, optLoop, optError);

    assertEquals(200, response.getStatusCode().value(), "Status code should be 200 when invalid sleep parameter is provided");
    assertTrue(Integer.parseInt(response.getBody()) >= 1 && Integer.parseInt(response.getBody()) <= 6, "Dice value should be between 1 and 6 when invalid sleep parameter is provided");
  }

  @Test
  void testRollDiceWithLoop() {
    final Optional<Integer> optSleep = Optional.empty();
    final Optional<Integer> optLoop = Optional.of(100);
    final Optional<Boolean> optError = Optional.empty();

    final ResponseEntity<String> response = webApiService.rollDice(optSleep, optLoop, optError);

    assertEquals(200, response.getStatusCode().value(), "Status code should be 200 when loop parameter is provided");
    assertTrue(Integer.parseInt(response.getBody()) >= 1 && Integer.parseInt(response.getBody()) <= 6, "Dice value should be between 1 and 6 when loop parameter is provided");
  }

  @Test
  void testRollDiceWithInvalidLoop() {
    final Optional<Integer> optSleep = Optional.empty();
    final Optional<Integer> optLoop = Optional.of(-100);
    final Optional<Boolean> optError = Optional.empty();

    final ResponseEntity<String> response = webApiService.rollDice(optSleep, optLoop, optError);

    assertEquals(200, response.getStatusCode().value(), "Status code should be 200 when invalid loop parameter is provided");
    assertTrue(Integer.parseInt(response.getBody()) >= 1 && Integer.parseInt(response.getBody()) <= 6, "Dice value should be between 1 and 6 when invalid loop parameter is provided");
  }

  @Test
  void testRollDiceWithError() {
    final Optional<Integer> optSleep = Optional.empty();
    final Optional<Integer> optLoop = Optional.empty();
    final Optional<Boolean> optError = Optional.of(true);

    final ResponseEntity<String> response = webApiService.rollDice(optSleep, optLoop, optError);

    assertEquals(500, response.getStatusCode().value(), "Status code should be 500 when error parameter is provided");
    assertEquals("0", response.getBody(), "Dice value should be 0 when error parameter is provided");
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