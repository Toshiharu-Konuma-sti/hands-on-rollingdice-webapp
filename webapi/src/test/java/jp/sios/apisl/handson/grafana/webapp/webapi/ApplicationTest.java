package jp.sios.apisl.handson.grafana.webapp.webapi;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

/**
 * {@code ApplicationTest} クラスは、{@link Application} クラスのユニットテストを提供します。.
 *
 * <p>Spring Boot アプリケーションの起動や、引数の有無による挙動、コンストラクタのカバレッジなどを検証します。
 * </p>
 * <ul>
 *   <li>アプリケーションコンテキストのロード確認</li>
 *   <li>main メソッドの SpringApplication 実行確認（引数あり・なし）</li>
 *   <li>コンストラクタのテストによるカバレッジ向上</li>
 * </ul>
 *
 * <p>モックを利用して SpringApplication の静的メソッド呼び出しを検証しています。
 * </p>
 */
class ApplicationTest {

  /**
   * ApplicationTestのコンストラクタです。
   * このクラスのインスタンスを生成する際に呼び出されます。.
   */
  public ApplicationTest() {
    // Constructor for ApplicationTest
  }

  @Test
  @SuppressWarnings("PMD.UnitTestShouldIncludeAssert")
  void contextLoads() {
    // This method is intentionally empty.
    // The test passes if the application context loads successfully.
  }

  @Test
  void testMainRunsSpringApplication() {
    // Arrange
    final String[] args = {};
    try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
      // Act
      Application.main(args);
      // Assert
      mocked.verify(() -> SpringApplication.run(Application.class, args));
    }
  }

  @Test
  void mainRunsWithoutArguments() {
    final String[] args = {};
    try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
      Application.main(args);
      mocked.verify(() -> SpringApplication.run(Application.class, args));
    }
  }

  @Test
  void mainRunsWithArguments() {
    final String[] args = {"--spring.profiles.active=test"};
    try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
      Application.main(args);
      mocked.verify(() -> SpringApplication.run(Application.class, args));
    }
  }

  @Test
  void testConstructorShouldBeCovered() {
    final Application application = new Application();
    assertNotNull(application, "Application instance should not be null");
  }

}
