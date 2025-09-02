package jp.sios.apisl.handson.grafana.webapp.webapi;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

class ApplicationTest {

  @Test
  void contextLoads() {
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
    Application application = new Application();
    assertNotNull(application);
  }

}
