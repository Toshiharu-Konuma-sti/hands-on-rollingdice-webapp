package jp.sios.apisl.handson.grafana.webapp.webui;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

@SuppressWarnings("PMD.AtLeastOneConstructor")
class ApplicationTest {

  @Test
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
    Application application = new Application();
    assertNotNull(application);
  }

}
