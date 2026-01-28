package jp.sios.apisl.handson.rollingdice.webapp.webapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import jp.sios.apisl.handson.rollingdice.webapp.webapi.dto.DiceValueDto;
import jp.sios.apisl.handson.rollingdice.webapp.webapi.entity.DiceEntity;
import jp.sios.apisl.handson.rollingdice.webapp.webapi.exception.HandsOnException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

  @Test
  void testRollDice() {
    final Optional<Integer> optSleep = Optional.empty();
    final Optional<Integer> optLoop = Optional.empty();
    final Optional<Boolean> optError = Optional.empty();
    final DiceValueDto fixedDiceRequest = null;

    when(jdbcTemplate.update(any(String.class), any(Integer.class))).thenReturn(1);

    final DiceValueDto response = webApiService.rollDice(
        optSleep, optLoop, optError, fixedDiceRequest);

    assertThat(response.value()).isBetween(1, 6);
  }

  @Test
  void testRollDiceWithSleep() {
    final Optional<Integer> optSleep = Optional.of(1);
    final Optional<Integer> optLoop = Optional.empty();
    final Optional<Boolean> optError = Optional.empty();
    final DiceValueDto fixedDiceRequest = null;

    when(jdbcTemplate.update(any(String.class), any(Integer.class))).thenReturn(1);

    final DiceValueDto response = webApiService.rollDice(
        optSleep, optLoop, optError, fixedDiceRequest);

    assertThat(response.value()).isBetween(1, 6);
  }

  @Test
  void testRollDiceWithInvalidSleep() {
    final Optional<Integer> optSleep = Optional.of(-3);
    final Optional<Integer> optLoop = Optional.empty();
    final Optional<Boolean> optError = Optional.empty();
    final DiceValueDto fixedDiceRequest = null;

    when(jdbcTemplate.update(any(String.class), any(Integer.class))).thenReturn(1);

    final DiceValueDto response = webApiService.rollDice(
        optSleep, optLoop, optError, fixedDiceRequest);

    assertThat(response.value()).isBetween(1, 6);
  }

  @Test
  void testRollDiceWithLoop() {
    final Optional<Integer> optSleep = Optional.empty();
    final Optional<Integer> optLoop = Optional.of(1);
    final Optional<Boolean> optError = Optional.empty();
    final DiceValueDto fixedDiceRequest = null;

    when(jdbcTemplate.update(any(String.class), any(Integer.class))).thenReturn(1);

    final DiceValueDto response = webApiService.rollDice(
        optSleep, optLoop, optError, fixedDiceRequest);

    assertThat(response.value()).isBetween(1, 6);
  }

  @Test
  void testRollDiceWithInvalidLoop() {
    final Optional<Integer> optSleep = Optional.empty();
    final Optional<Integer> optLoop = Optional.of(-3);
    final Optional<Boolean> optError = Optional.empty();
    final DiceValueDto fixedDiceRequest = null;

    when(jdbcTemplate.update(any(String.class), any(Integer.class))).thenReturn(1);

    final DiceValueDto response = webApiService.rollDice(
        optSleep, optLoop, optError, fixedDiceRequest);

    assertThat(response.value()).isBetween(1, 6);
  }

  @Test
  void testRollDiceWithError() {
    final Optional<Integer> optSleep = Optional.empty();
    final Optional<Integer> optLoop = Optional.empty();
    final Optional<Boolean> optError = Optional.of(true);
    final DiceValueDto fixedDiceRequest = null;

    assertThat(assertThrows(
        HandsOnException.class,
        () -> webApiService.rollDice(optSleep, optLoop, optError, fixedDiceRequest)
    )).isNotNull();
  }

  @Test
  void testRollDiceWithFixedValue() {
    final Optional<Integer> optSleep = Optional.empty();
    final Optional<Integer> optLoop = Optional.empty();
    final Optional<Boolean> optError = Optional.empty();
    final DiceValueDto fixedDiceRequest = new DiceValueDto(4);

    when(jdbcTemplate.update(any(String.class), any(Integer.class))).thenReturn(1);

    final DiceValueDto response = webApiService.rollDice(
        optSleep, optLoop, optError, fixedDiceRequest);

    assertThat(response.value()).isEqualTo(4);
  }

  @Test
  void testListDice() {
    final String expectedSql = "SELECT id, value, updated_at FROM dice ORDER BY id DESC;";
    final DiceEntity dice1 = new DiceEntity(1, 5, LocalDateTime.now());
    final List<DiceEntity> expectedRecord = Arrays.asList(dice1);

    when(jdbcTemplate.query(eq(expectedSql), any(RowMapper.class))).thenReturn(expectedRecord);

    final List<DiceEntity> diceList = webApiService.listDice();

    assertEquals(1, diceList.size(),
        "The dice list should contain exactly one element.");
    assertEquals(5, diceList.get(0).value(),
        "The dice value of the first element should be 5.");
  }

}