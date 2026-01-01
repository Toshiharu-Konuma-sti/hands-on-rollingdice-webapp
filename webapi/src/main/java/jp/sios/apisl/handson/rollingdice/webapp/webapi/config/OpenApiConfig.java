package jp.sios.apisl.handson.rollingdice.webapp.webapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI（Swagger）ドキュメントの全般的な設定を行う構成クラスです。.
 *
 * <p>API仕様書のタイトル、説明、バージョン情報、連絡先などのメタデータ定義を行います。</p>
 *
 * @author Toshiharu Konuma
 */
@Configuration
@SuppressWarnings("PMD.AtLeastOneConstructor")
public class OpenApiConfig {

  /**
   * カスタマイズされたOpenAPI定義を返します。.
   *
   * <p>API全体のタイトル、説明、バージョン、連絡先情報、および主要なタグの定義を設定しています。</p>
   *
   * @return アプリケーション固有の設定を含む{@link OpenAPI}インスタンス
   */
  @Bean
  public OpenAPI customOpenApi() {
    return new OpenAPI()
        // info セクションの設定 (title, description, version, contact)
        .info(new Info()
            .title("Dice Rolling API")
            .description("このAPIは、サイコロを振る機能とその履歴を閲覧する機能を提供します。<br>ハンズオンやデモでの利用を想定しています。")
            .version("0.0.1")
            .contact(new Contact()
                .name("API Support")
                .url("https://www.example.com/support")
                .email("support@example.com")))
        .tags(List.of(
            new Tag()
                .name("web-api-controller")
                .description("Web API用コントローラーに関する操作")
        ));
  }

}
