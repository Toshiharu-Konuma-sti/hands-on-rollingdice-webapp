package jp.sios.apisl.handson.rollingdice.webapp.webapi.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@code OpenApiConfigTest} クラスは、{@link OpenApiConfig} クラスのユニットテストを提供します。.
 *
 * @author Toshiharu Konuma
 */
class OpenApiConfigTest {

  /**
   * OpenApiConfigTestのコンストラクタです。
   * このクラスのテストインスタンスを初期化します。.
   */
  public OpenApiConfigTest() {
    // Constructor for WebApiServiceImplTest
  }

  @Test
  @DisplayName("customOpenApiメソッドが期待通りの設定値を持つOpenAPIオブジェクトを返却すること")
  @SuppressWarnings("PMD.LawOfDemeter")
  void testCustomOpenApi() {

    // 1. テスト対象クラスのインスタンス化
    // (これでコンストラクタのカバレッジも通ります)
    final OpenApiConfig config = new OpenApiConfig();

    // 2. メソッドの実行
    final OpenAPI result = config.customOpenApi();

    // 3. 検証 (Assertions)

    // OpenAPIオブジェクト自体の検証
    assertNotNull(result, "OpenAPIインスタンスがnullであってはなりません");

    // Infoセクションの検証
    final Info info = result.getInfo();
    assertNotNull(info, "Infoセクションがnullであってはなりません");
    assertEquals("Dice Rolling API", info.getTitle(), "タイトルが期待値と一致すること");
  }

}
