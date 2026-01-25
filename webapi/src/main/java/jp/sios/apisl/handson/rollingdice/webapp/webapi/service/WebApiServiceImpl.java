package jp.sios.apisl.handson.rollingdice.webapp.webapi.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import jp.sios.apisl.handson.rollingdice.webapp.webapi.dto.DiceDto;
import jp.sios.apisl.handson.rollingdice.webapp.webapi.entity.DiceEntity;
import jp.sios.apisl.handson.rollingdice.webapp.webapi.exception.HandsOnException;
import jp.sios.apisl.handson.rollingdice.webapp.webapi.util.UtilEnvInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * サイコロの操作に関するサービスの実装クラスです。.
 *
 * <p>このクラスは、サイコロを振ったり履歴を一覧で返す処理を提供します。</p>
 * <ul>
 *   <li>rollDiceメソッドでサイコロを振り、結果をデータベースに保存します。</li>
 *   <li>sleepメソッドで指定時間のスリープを行います。</li>
 *   <li>loopメソッドで指定時間ループをしながらファイル読み込みを繰り返します。</li>
 *   <li>errorメソッドで意図的に例外を発生させます。</li>
 *   <li>listDiceメソッドで保存されたサイコロの出目履歴を一覧で取得します。</li>
 * </ul>
 * <p>デバッグや運用時のトラブルシューティングを容易にするため、詳細なログ出力や例外制御を行っています。</p>
 *
 * @author Toshiharu Konuma
 */
@Service
@SuppressWarnings("PMD.CommentSize")
public class WebApiServiceImpl implements WebApiService {

  /**
   * ループ内で使用される設定ファイル「application.yml」のパスを表す定数です。.
   */
  private static final String FILE_PATH_IN_LOOP = "application.yml";

  /**
   * ログ出力を行うためのロガーインスタンスです。
   * このサービスクラス内の処理状況やエラー情報を記録します。.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(WebApiServiceImpl.class);

  /**
   * データベース操作を行うためのJdbcTemplateインスタンス。
   * SQLクエリの実行やデータの取得・更新などに使用します。.
   */
  private final JdbcTemplate jdbcTemplate;

  // {{{ public WebApiServiceImpl(JdbcTemplate jdbcTemplate)
  /**
   * WebApiServiceImplのコンストラクタです。.
   * 指定されたJdbcTemplateを使用してインスタンスを初期化します。
   *
   * @param jdbcTemplate データベース操作に使用するJdbcTemplate
   */
  public WebApiServiceImpl(final JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }
  // }}}

  // {{{ public ResponseEntity<Integer> rollDice(...)
  /**
   * サイコロを振り出目を返します。.
   *
   * <p>オプションでスリープ時間、ループ時間、エラー発生の有無を指定して処理の挙動を制御できます。
   * エラーが発生した場合はHTTP 500（INTERNAL_SERVER_ERROR）を返却し、
   * 正常時はサイコロを振って出た出目（1～6）をHTTP 200（OK）で返却します。
   * なお、リクエストボディーで出目が指定された場合は、その値を出目に使用します。
   * </p>
   *
   * @param optSleep サイコロを振る前にスリープする時間（秒）を指定するオプションの整数
   * @param optLoop サイコロを振る前にループで遅延する時間（秒）を指定するオプションの整数
   * @param optError エラーを発生させるかどうかを指定するオプションの真偽値
   * @param fixedDiceRequest サイコロの出目を強制する情報を持つオプションの{@link DiceDto}オブジェクト
   * @return サイコロの出目（1～6）を含む{@link DiceDto}オブジェクト
   */
  @Override
  public DiceDto rollDice(
      final Optional<Integer> optSleep, 
      final Optional<Integer> optLoop, 
      final Optional<Boolean> optError,
      final DiceDto fixedDiceRequest) {

    UtilEnvInfo.logStartClassMethod();
    LOGGER.info(
        "The received parameters are: sleep='{}', loop='{}', error='{}' and fixedDiceRequest='{}'", 
        optSleep, optLoop, optError, fixedDiceRequest);

    this.sleep(optSleep);
    this.loop(optLoop);
    this.error(optError);

    final int resultValue;
    if (fixedDiceRequest != null && fixedDiceRequest.value() != null) {
      resultValue = fixedDiceRequest.value();
      LOGGER.info("The fixed value of dice is: '{}'", resultValue);
    } else {
      resultValue = this.roll();
    }
    this.insertDice(resultValue);
    return new DiceDto(resultValue);
  }
  // }}}

  // {{{ private void sleep(Optional<Integer> optSleep)
  @SuppressWarnings("PMD.GuardLogStatement")
  private void sleep(final Optional<Integer> optSleep) {

    UtilEnvInfo.logStartClassMethod();

    optSleep.ifPresent(sleepSeconds -> {
      if (sleepSeconds <= 0) {
        LOGGER.warn(
            "The processing of sleep was skipped, "
            + "because the value of parameter was not a positive integer: '{}'",
            sleepSeconds);
        return;
      }
      LOGGER.warn("!!! Starting sleep for: {} seconds !!!", String.format("%.2f", (double) sleepSeconds));
      try {
        final long sleepMillis = sleepSeconds * 1000L;
        Thread.sleep(sleepMillis);
        LOGGER.warn("!!! Sleep finished !!!");
      } catch (InterruptedException ex) {
        LOGGER.error("The exception was happened with sleep()", ex);
        Thread.currentThread().interrupt();
        throw new HandsOnException("Interrupted during sleep", ex);
      }
    });
  }
  // }}}

  // {{{ private void loop(Optional<Integer> optLoop)
  @SuppressWarnings("PMD.GuardLogStatement")
  private void loop(final Optional<Integer> optLoop) {

    UtilEnvInfo.logStartClassMethod();

    optLoop.ifPresent(loopSeconds -> {
      if (loopSeconds <= 0) {
        LOGGER.warn(
            "The processing of loop was skipped, "
            + "because the value of parameter was not a positive integer: '{}'",
            loopSeconds);
        return;
      }

      final double totalSeconds = loopSeconds;
      LOGGER.warn("!!! Starting loop for: {} seconds !!!", String.format("%.2f", totalSeconds));

      final long durationMillis = loopSeconds * 1000L;
      final long startTime = System.currentTimeMillis();
      final long endTime = startTime + durationMillis;

      final long logIntervalMillis = durationMillis / 5;
      long nextLogTime = startTime + logIntervalMillis;

      String line = null;
      int executionCount = 0;

      while (System.currentTimeMillis() < endTime) {
        line = this.readFile(FILE_PATH_IN_LOOP);
        executionCount++;

        final long currentTime = System.currentTimeMillis();
        if (currentTime >= nextLogTime && currentTime < endTime) {
          final double elapsedSeconds = (currentTime - startTime) / 1000.0;
          LOGGER.warn(
              "The progress of loop is: {}/{} seconds (loop count: {})",
              String.format("%.2f", elapsedSeconds),
              String.format("%.2f", totalSeconds),
              String.format("%,d", executionCount));
          nextLogTime += logIntervalMillis;
        }
      }
      LOGGER.warn(
          "!!! Loop finished !!! (Total executions: {}) : The read text is: '{}'",
          String.format("%,d", executionCount), line);

    });
  }
  // }}}

  // {{{ private String readFile(String filePath)
  private String readFile(final String filePath) {
    String line = null;
    try (InputStream inputStream = new ClassPathResource(filePath).getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
      LOGGER.debug("Successfully loaded a file.");
      line = reader.readLine();
      LOGGER.debug("Read line: {}", line);
    } catch (IOException ex) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("Failed to load a file: '{}'", ex.getMessage());
      }
    }
    
    return line;
  }
  // }}}

  // {{{ private void error(Optional<Boolean> optError)
  private void error(final Optional<Boolean> optError) {
    UtilEnvInfo.logStartClassMethod();

    if (optError.isPresent() && optError.get()) {
      LOGGER.error(
          "!!! Intentional exception triggered: '{}' !!!", 
          "HandsOnException");
      throw new HandsOnException("Intentional error triggered by request parameter.");
    }
  }
  // }}}

  // {{{ private int roll()
  private int roll() {
    UtilEnvInfo.logStartClassMethod();

    final int value = this.getRandomNumber(1, 6);
    LOGGER.info("The value of dice is: '{}'", value);

    return value;
  }
  // }}}

  // {{{ private int getRandomNumber(int min, int max)
  private int getRandomNumber(final int min, final int max) {
    UtilEnvInfo.logStartClassMethod();
    return ThreadLocalRandom.current().nextInt(min, max + 1);
  }
  // }}}

  // {{{ private void insertDice(int value)
  private void insertDice(final int value) {
    UtilEnvInfo.logStartClassMethod();

    final String sql = "INSERT INTO dice(value) VALUES(?)";
    LOGGER.info("The sql to execute is: '{}'. And the value to give is: '{}'", sql, value);
    final int number = this.jdbcTemplate.update(sql, value);
    LOGGER.info("The record count of the executed sql is: '{}'", number);
  }
  // }}}

  // {{{ public List<Dice> listDice()
  /**
   * サイコロを振った履歴を一覧で返します。.
   *
   * <p>diceテーブルから全レコードをIDの降順で取得し、
   * {@link DiceEntity}オブジェクトのリストで返却します。
   * </p>
   *
   * @return サイコロを振った履歴を保持する{@link DiceEntity}オブジェクトのリスト
   */
  @Override
  @SuppressWarnings("PMD.GuardLogStatement")
  public List<DiceEntity> listDice() {
    UtilEnvInfo.logStartClassMethod();

    final String sql = "SELECT id, value, updated_at FROM dice ORDER BY id DESC;";
    LOGGER.info("The sql to execute is '{}'", sql);

    final List<DiceEntity> list = this.jdbcTemplate.query(sql, (rs, rowNum) -> 
        new DiceEntity(
          rs.getInt("id"),
          rs.getInt("value"),
          rs.getObject("updated_at", LocalDateTime.class)
        )
    );
    LOGGER.info("The record count of the executed sql is: '{}'", list.size());

    return list;
  }
  // }}}

}
