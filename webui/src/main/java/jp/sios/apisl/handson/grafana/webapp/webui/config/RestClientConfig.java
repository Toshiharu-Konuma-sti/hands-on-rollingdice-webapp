package jp.sios.apisl.handson.grafana.webapp.webui.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * RestClientの設定を行うコンフィグレーションクラスです。.
 *
 * <p>このクラスはSpringのDIコンテナにRestClientのBeanを登録します。
 * RestClientは外部APIとの通信など、HTTPリクエストを行う際に利用されます。
 * </p>
 */
@Configuration
public class RestClientConfig {

  // {{{ public RestClientConfig() {
  /**
   * ユーティリティクラスのため、インスタンス化は行いません。.
   */
  public RestClientConfig() {
    // A default constructor.
  }
  // }}}

  // {{{ public RestClient restClient(RestClient.Builder restClientBuilder)
  /**
   * RestClientのインスタンスを生成して返します。.
   *
   * @param restClientBuilder RestClientのビルダーインスタンス
   * @return 構築されたRestClientインスタンス
   */
  @Bean
  public RestClient restClient(RestClient.Builder restClientBuilder) {
    RestClient restClient = restClientBuilder.build();
    return restClient;
  }
  // }}}

}
