package jp.sios.apisl.handson.rollingdice.webapp.webapi.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@code OpenApiConfigTest} クラスは、{@link OpenApiConfig} クラスのユニットテストを提供します。.
 *
 * @author Toshiharu Konuma
 */
class OpenApiConfigTest {

  /**
   * テスト対象のメソッドから返却されるOpenAPIインスタンス.
   */
  private OpenAPI result;

  /**
   * OpenApiConfigTestのコンストラクタです。
   * このクラスのテストインスタンスを初期化します。.
   */
  public OpenApiConfigTest() {
    // Constructor for WebApiServiceImplTest
  }

  /**
   * 各テストメソッド実行前のセットアップを行います.
   * テスト対象クラスをインスタンス化し、検証対象のOpenAPIオブジェクトを生成します。
   */
  @BeforeEach
  void setUp() {
    // 1. テスト対象クラスのインスタンス化
    final OpenApiConfig config = new OpenApiConfig();
    // 2. メソッドの実行結果をフィールドに保持
    this.result = config.customOpenApi();
  }

  @Test
  @DisplayName("customOpenApiメソッドがnullではないOpenAPIオブジェクトを返却すること")
  void testResultIsNotNull() {
    assertNotNull(this.result, "OpenAPIインスタンスがnullであってはなりません");
  }

  @Test
  @DisplayName("OpenAPIオブジェクトにInfoセクションが含まれていること")
  void testInfoSectionIsNotNull() {
    assertNotNull(result.getInfo(), "Infoセクションがnullであってはなりません");
  }

  @Test
  @DisplayName("Infoセクションのタイトルが期待値と一致すること")
  @SuppressWarnings("PMD.LawOfDemeter")
  void testTitleIsCorrect() {
    assertEquals("Dice Rolling API", result.getInfo().getTitle(), "タイトルが期待値と一致すること");
  }

}
