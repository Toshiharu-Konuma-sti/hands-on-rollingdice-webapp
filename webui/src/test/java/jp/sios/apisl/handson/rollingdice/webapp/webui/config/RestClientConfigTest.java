package jp.sios.apisl.handson.rollingdice.webapp.webui.config;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

/**
 * {@code RestClientConfigTest} クラスは、{@link RestClientConfig} の設定クラスに対する
 * ユニットテストを提供します。.
 *
 * <p>主に {@code restClient} メソッドが正しく {@link RestClient} インスタンスを生成し返却するかを検証します。
 * Mockito を用いて依存オブジェクトのモック化を行い、ビルダーの動作や返却値の検証を行います。
 * </p>
 */
@SuppressWarnings({"PMD.AtLeastOneConstructor", "PMD.TooManyStaticImports"})
class RestClientConfigTest {

  @Test
  void testRestClientBeanReturnsBuiltRestClient() {
    // Arrange
    final RestClient.Builder mockBuilder = mock(RestClient.Builder.class);
    final RestClient mockRestClient = mock(RestClient.class);
    when(mockBuilder.build()).thenReturn(mockRestClient);

    final RestClientConfig config = new RestClientConfig();

    // Act
    final RestClient result = config.restClient(mockBuilder);

    // Assert
    assertSame(
        mockRestClient, result,
        "The returned RestClient should be the same instance as the mockRestClient.");
    verify(mockBuilder, times(1)).build();
  }

}