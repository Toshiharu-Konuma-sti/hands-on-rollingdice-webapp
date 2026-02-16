package jp.sios.apisl.handson.rollingdice.webapp.webapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web APIのCORS（Cross-Origin Resource Sharing）設定を行う構成クラスです。.
 *
 * <p>フロントエンド（Faro等）からのクロスオリジンアクセスを許可し、
 * 分散トレーシングに必要なヘッダー情報の伝播を可能にします。</p>
 *
 * @author Toshiharu Konuma
 */
@Configuration
@SuppressWarnings("PMD.AtLeastOneConstructor")
public class WebConfig implements WebMvcConfigurer {

  /**
   * 許可するオリジンのリスト（設定ファイルから注入）。.
   *
   * <p>設定キー: {@code app.cors.allowed-origins}<br>
   * 未設定の場合は起動時にエラーとなります。</p>
   */
  @Value("${app.cors.allowed-origins}")
  private String[] allowedOrigins;

  /**
   * CORSマッピングの設定を追加します。.
   *
   * <p>外部設定ファイルで指定されたオリジンに対し、
   * 全てのエンドポイント、主要なHTTPメソッド、および全てのヘッダー（トレースID含む）を許可します。</p>
   *
   * @param registry CORSレジストリ
   */
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins(allowedOrigins)
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true);
  }
}
