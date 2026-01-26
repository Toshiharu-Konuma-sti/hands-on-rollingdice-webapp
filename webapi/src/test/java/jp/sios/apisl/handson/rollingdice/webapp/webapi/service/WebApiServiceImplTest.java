package jp.sios.apisl.handson.rollingdice.webapp.webapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import jp.sios.apisl.handson.rollingdice.webapp.webapi.entity.DiceEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

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
// @SuppressWarnings("PMD.LawOfDemeter")
@SuppressWarnings("PMD.CommentSize")
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

/*
  @Test
  void testRollDice() {
    final Optional<Integer> optSleep = Optional.empty();
    final Optional<Integer> optLoop = Optional.empty();
    final Optional<Boolean> optError = Optional.empty();
    final Optional<Integer> optFixedValue = Optional.empty();

    final ResponseEntity<String> response = webApiService.rollDice(optSleep, optLoop, optError, optFixedValue);

    assertThat(response.getStatusCode()).as("Status code should be 200 on success").isEqualTo(HttpStatus.OK);
    assertThat(Integer.parseInt(response.getBody())).as("Dice value should be between 1 and 6").isBetween(1, 6);
  }

  @Test
  void testRollDiceWithSleep() {
    final Optional<Integer> optSleep = Optional.of(3);
    final Optional<Integer> optLoop = Optional.empty();
    final Optional<Boolean> optError = Optional.empty();
    final Optional<Integer> optFixedValue = Optional.empty();

    final ResponseEntity<String> response = webApiService.rollDice(optSleep, optLoop, optError, optFixedValue);

    assertThat(response.getStatusCode()).as("Status code should be 200 when sleep parameter is provided").isEqualTo(HttpStatus.OK);
    assertThat(Integer.parseInt(response.getBody())).as("Dice value should be between 1 and 6 when sleep parameter is provided").isBetween(1, 6);
  }

  @Test
  void testRollDiceWithInvalidSleep() {
    final Optional<Integer> optSleep = Optional.of(-3);
    final Optional<Integer> optLoop = Optional.empty();
    final Optional<Boolean> optError = Optional.empty();
    final Optional<Integer> optFixedValue = Optional.empty();

    final ResponseEntity<String> response = webApiService.rollDice(optSleep, optLoop, optError, optFixedValue);

    assertThat(response.getStatusCode()).as("Status code should be 200 when invalid sleep parameter is provided").isEqualTo(HttpStatus.OK);
    assertThat(Integer.parseInt(response.getBody())).as("Dice value should be between 1 and 6 when invalid sleep parameter is provided").isBetween(1, 6);
  }

  @Test
  void testRollDiceWithLoop() {
    final Optional<Integer> optSleep = Optional.empty();
    final Optional<Integer> optLoop = Optional.of(3);
    final Optional<Boolean> optError = Optional.empty();
    final Optional<Integer> optFixedValue = Optional.empty();

    final ResponseEntity<String> response = webApiService.rollDice(optSleep, optLoop, optError, optFixedValue);

    assertThat(response.getStatusCode()).as("Status code should be 200 when loop parameter is provided").isEqualTo(HttpStatus.OK);
    assertThat(Integer.parseInt(response.getBody())).as("Dice value should be between 1 and 6 when loop parameter is provided").isBetween(1, 6);
  }

  @Test
  void testRollDiceWithInvalidLoop() {
    final Optional<Integer> optSleep = Optional.empty();
    final Optional<Integer> optLoop = Optional.of(-3);
    final Optional<Boolean> optError = Optional.empty();
    final Optional<Integer> optFixedValue = Optional.empty();

    final ResponseEntity<String> response = webApiService.rollDice(optSleep, optLoop, optError, optFixedValue);

    assertThat(response.getStatusCode()).as("Status code should be 200 when invalid loop parameter is provided").isEqualTo(HttpStatus.OK);
    assertThat(Integer.parseInt(response.getBody())).as("Dice value should be between 1 and 6 when invalid loop parameter is provided").isBetween(1, 6);
  }

  @Test
  void testRollDiceWithError() {
    final Optional<Integer> optSleep = Optional.empty();
    final Optional<Integer> optLoop = Optional.empty();
    final Optional<Boolean> optError = Optional.of(true);
    final Optional<Integer> optFixedValue = Optional.empty();

    final ResponseEntity<String> response = webApiService.rollDice(optSleep, optLoop, optError, optFixedValue);

    assertThat(response.getStatusCode()).as("Status code should be 500 when error parameter is provided").isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(response.getBody()).as("Dice value should be 0 when error parameter is provided").isEqualTo("0");
  }

  @Test
  void testRollDiceWithFixedValue() {
    final Optional<Integer> optSleep = Optional.empty();
    final Optional<Integer> optLoop = Optional.empty();
    final Optional<Boolean> optError = Optional.empty();
    final Optional<Integer> optFixedValue = Optional.of(4);

    final ResponseEntity<String> response = webApiService.rollDice(optSleep, optLoop, optError, optFixedValue);

    assertThat(response.getStatusCode()).as("Status code should be 200 when fixed value is provided").isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).as("Dice value should match the fixed value").isEqualTo("4");
  }

  @Test
  void testListDice() {
    final String expectedSql = "SELECT id, value, updated_at FROM dice ORDER BY id DESC;";
    final Dice dice1 = new Dice(1, 5, LocalDateTime.now());
    final List<Dice> expectedRecord = Arrays.asList(dice1);

    when(jdbcTemplate.query(eq(expectedSql), any(RowMapper.class))).thenReturn(expectedRecord);

    final List<Dice> diceList = webApiService.listDice();

    assertEquals(1, diceList.size(), "Dice list size should be 1");
    assertEquals(5, diceList.get(0).value(), "Dice value should be 5");
  }
*/

}
