package jp.sios.apisl.handson.rollingdice.webapp.webapi.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@code OpenApiConfigTest} クラスは、{@link OpenApiConfig} クラスのユニットテストを提供します。.
 *
 * <p>各メソッドが期待通りに動作するかを検証します。
 * </p>
 * <ul>
 *   <li>customOpenApiメソッドの正常系動作の検証</li>
 * </ul>
 *
 * @author Toshiharu Konuma
 */
class OpenApiConfigTest {

  @Test
  @DisplayName("customOpenApiメソッドが期待通りの設定値を持つOpenAPIオブジェクトを返却すること")
  void testCustomOpenApi() {
    // 1. テスト対象クラスのインスタンス化
    // (これでコンストラクタのカバレッジも通ります)
    OpenApiConfig config = new OpenApiConfig();

    // 2. メソッドの実行
    OpenAPI result = config.customOpenApi();

    // 3. 検証 (Assertions)

    // OpenAPIオブジェクト自体の検証
    assertNotNull(result, "OpenAPIインスタンスがnullであってはなりません");

    // Infoセクションの検証
    Info info = result.getInfo();
    assertNotNull(info, "Infoセクションがnullであってはなりません");
    assertEquals("Dice Rolling API", info.getTitle());
    assertEquals("このAPIは、サイコロを振る機能とその履歴を閲覧する機能を提供します。<br>ハンズオンやデモでの利用を想定しています。", info.getDescription());
    assertEquals("0.0.1", info.getVersion());

    // Contactセクションの検証
    Contact contact = info.getContact();
    assertNotNull(contact, "Contactセクションがnullであってはなりません");
    assertEquals("API Support", contact.getName());
    assertEquals("https://www.example.com/support", contact.getUrl());
    assertEquals("support@example.com", contact.getEmail());

    // Tagsセクションの検証
    List<Tag> tags = result.getTags();
    assertNotNull(tags, "Tagsリストがnullであってはなりません");
    assertEquals(1, tags.size(), "タグの登録数は1つである必要があります");

    Tag tag = tags.get(0);
    assertEquals("web-api-controller", tag.getName());
    assertEquals("Web API用コントローラーに関する操作", tag.getDescription());
  }

}
