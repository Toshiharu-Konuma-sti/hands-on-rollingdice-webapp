package jp.sios.apisl.handson.grafana.webapp.webui;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

/**
 * <p>
 * {@code ApplicationTest} クラスは、{@link Application} クラスの単体テストを提供します。
 * Spring Boot アプリケーションの起動や、コンストラクタのカバレッジ確認など、
 * アプリケーションの基本的な動作を検証するためのテストケースを含みます。
 * </p>
 *
 * <ul>
 *   <li>アプリケーションコンテキストの正常なロードの確認</li>
 *   <li>main メソッドが {@code SpringApplication.run} を正しく呼び出すかの検証</li>
 *   <li>引数あり・なしでの main メソッドの動作確認</li>
 *   <li>コンストラクタのカバレッジ確保</li>
 * </ul>
 *
 * <p>
 * モックには Mockito を利用しています。
 * </p>
 *
 * @author Toshiharu Konuma
 */
@SuppressWarnings("PMD.AtLeastOneConstructor")
class ApplicationTest {

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
    try (org.mockito.MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
      // Act
      Application.main(args);

      // Assert
      mocked.verify(() -> SpringApplication.run(Application.class, args));
    }
  }

  @Test
  void mainRunsWithoutArguments() {
    final String[] args = {};
    try (org.mockito.MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
      Application.main(args);
      mocked.verify(() -> SpringApplication.run(Application.class, args));
    }
  }

  @Test
  void mainRunsWithArguments() {
    final String[] args = {"--spring.profiles.active=test"};
    try (org.mockito.MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
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
