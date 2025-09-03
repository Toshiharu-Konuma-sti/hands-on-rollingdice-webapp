package jp.sios.apisl.handson.grafana.webapp.webui.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestClient;

/**
 * {@code RestClientConfigTest} クラスは、Spring Boot のテストコンテキストを利用して
 * RestClient の Bean が正しくアプリケーションコンテキストに登録されているかを検証するテストクラスです。.
 *
 * <p>このクラスは、RestClient の Bean が存在することを確認するユニットテストを提供します。
 * </p>
 */
@SpringBootTest
@SuppressWarnings("PMD.AtLeastOneConstructor")
class RestClientConfigTest {

  /**
   * Springのアプリケーションコンテキストを保持するフィールドです。
   * テスト実行時に依存関係の注入やBeanの管理を行うために使用されます。.
   */
  @Autowired
  private ApplicationContext applicationContext;

  @Test
  void testRestClientBeanExists() {
    // Verify that the RestClient bean is created and available in the application context
    final RestClient restClient = applicationContext.getBean(RestClient.class);
    assertNotNull(restClient, "RestClient bean should not be null");
  }
}
