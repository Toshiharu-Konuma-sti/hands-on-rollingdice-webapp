package jp.sios.apisl.handson.rollingdice.webapp.webapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OpenApiGeneratorTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void generateOpenApiJson() throws Exception {
    // デフォルトのパス /v3/api-docs を叩いてJSONを取得
    String jsonContent = mockMvc.perform(get("/v3/api-docs"))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);

    // 出力先ディレクトリ (build.gradleの設定に合わせる)
    Path outputDir = Paths.get("build/docs/openapi");
    if (!Files.exists(outputDir)) {
      Files.createDirectories(outputDir);
    }

    // ファイル書き出し
    Path outputPath = outputDir.resolve("openapi.json");
    Files.write(outputPath, jsonContent.getBytes(StandardCharsets.UTF_8));
    
    System.out.println("Generated OpenAPI JSON at: " + outputPath.toAbsolutePath());
  }
}