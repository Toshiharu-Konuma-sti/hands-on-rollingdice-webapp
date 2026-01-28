package jp.sios.apisl.handson.rollingdice.webapp.webapi;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * OpenAPI仕様書を自動生成するためのクラスを提供します。.
 * 
 * <p>このクラスは、Spring Bootアプリケーションの起動時に
 * {@code /v3/api-docs}エンドポイントからOpenAPI仕様書(JSON形式)を取得し、
 * 指定されたディレクトリにファイルとして出力します。</p>
 * 
 * <p>生成されたOpenAPI仕様書は、API設計書の自動生成や
 * クライアントコードの自動生成などに利用できます。</p>
 */
@SpringBootTest
@AutoConfigureMockMvc
@SuppressWarnings("PMD.CommentSize")
class OpenApiGeneratorTest {

  /**
   * ログ出力に使用されます。.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(OpenApiGeneratorTest.class);

  /**
   * 実際のHTTPサーバーを起動せずにHTTPリクエストとレスポンスをシミュレートします。.
   */
  @Autowired
  private MockMvc mockMvc;

  /**
   * {@link OpenApiGeneratorTest}のコンストラクタです。
   * このクラスのインスタンスを生成する際に呼び出されます。.
   */
  public OpenApiGeneratorTest() {
    // Constructor for OpenApiGeneratorTest
  }

  @Test
  @SuppressWarnings("PMD.LawOfDemeter")
  void generateOpenApiJson() throws Exception {
    // デフォルトのパス /v3/api-docs を叩いてJSONを取得
    final String jsonContent = mockMvc.perform(get("/v3/api-docs"))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);

    // 出力先ディレクトリ (build.gradleの設定に合わせる)
    final Path outputDir = Paths.get("build/docs/openapi");
    if (!Files.exists(outputDir)) {
      Files.createDirectories(outputDir);
    }

    // ファイル書き出し
    final Path outputPath = outputDir.resolve("openapi.json");
    Files.write(outputPath, jsonContent.getBytes(StandardCharsets.UTF_8));

    assertTrue(Files.size(outputPath) > 0, "OpenAPI file should not be empty");
  
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Generated OpenAPI JSON at: {}", outputPath.toAbsolutePath());
    }
  }
}