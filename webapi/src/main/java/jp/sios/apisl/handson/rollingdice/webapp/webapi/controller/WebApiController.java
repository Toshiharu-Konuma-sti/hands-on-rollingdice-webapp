package jp.sios.apisl.handson.rollingdice.webapp.webapi.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import jp.sios.apisl.handson.rollingdice.webapp.webapi.entity.Dice;
import jp.sios.apisl.handson.rollingdice.webapp.webapi.service.WebApiService;
import jp.sios.apisl.handson.rollingdice.webapp.webapi.util.UtilEnvInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * サイコロAPIのエンドポイントを提供するコントローラクラスです。.
 * 
 * <ul>
 *   <li>サイコロを振るAPI（/api/dice/v1/roll）</li>
 *   <li>サイコロの一覧を取得するAPI（/api/dice/v1/list）</li>
 * </ul>
 *
 * <p>各エンドポイントでリクエスト情報のログ出力や、サービス層への処理委譲を行います。
 * </p>
 *
 * @author Toshiharu Konuma
 */
@RestController
@RequestMapping("/api/dice/v1")
@OpenAPIDefinition(
    info = @Info(
        title = "Dice Rolling API",
        version = "0.0.1",
        description = "このAPIは、サイコロを振る機能とその履歴を閲覧する機能を提供します。"
            + "<br>ハンズオンやデモでの利用を想定しています。"
    )
)
@SuppressWarnings("PMD.CommentSize")
public class WebApiController {

  /**
   * ロガーインスタンス。WebApiControllerクラスのログ出力に使用します。.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(WebApiController.class);

  /**
   * WebApiServiceのインスタンスを保持するフィールドです。
   * このサービスを利用してWeb APIの各種処理を実行します。.
   */
  private final WebApiService service;

  // {{{ public WebApiController(WebApiService service)
  /**
   * WebApiControllerのコンストラクタです。.
   *
   * @param service WebApiServiceのインスタンス
   */
  public WebApiController(final WebApiService service) {
    this.service = service;
  }
  // }}}

  // {{{ public ResponseEntity<Integer> rollDice(...)
  /**
   * サイコロを振る処理を実行します。.
   *
   * <p>リクエストパラメータとして、sleep（待機時間）、loop（ループ時間）、error（エラー発生フラグ）を受け取ります。
   * これらのパラメータに基づき、サービス層でサイコロの値を生成し、結果をレスポンスとして返却します。
   * </p>
   *
   * @param request   HTTPリクエスト情報
   * @param optSleep  サイコロ処理前に意図的に遅延させる待機時間（秒、オプション）
   * @param optLoop   サイコロ処理前に意図的に遅延させるループ時間（秒、オプション）
   * @param optError  エラー発生フラグ（オプション）
   * @return サイコロの出目（1～6の整数値）を含むResponseEntity
   */
  @GetMapping({"/roll"})
  @Operation(summary = "サイコロを振ります。", 
      description = "リクエストパラメータとして、"
          + "sleep（待機時間）、loop（ループ時間）、error（エラー発生フラグ）を受け取ります。"
          + "これらのパラメータに基づき、サイコロの値を生成し、結果をレスポンスとして返却します。")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "サイコロの出目（1～6の整数値）",
          content = @Content(mediaType = "text/plain",
              schema = @Schema(implementation = Integer.class, example = "5"))),
      @ApiResponse(responseCode = "500", description = "errorパラメータが指定された場合など、サーバ内部でエラーが発生",
          content = @Content)
  })
  public ResponseEntity<String> rollDice(
      final HttpServletRequest request,
      @Parameter(description = "処理を意図的に遅延させる時間（秒）", example = "10")
      @RequestParam(name = "sleep", required = false) final Optional<Integer> optSleep,
      @Parameter(description = "処理を意図的に遅延させるループ時間（秒）", example = "15")
      @RequestParam(name = "loop", required = false) final Optional<Integer> optLoop,
      @Parameter(description = "エラー発生フラグ", example = "true")
      @RequestParam(name = "error", required = false) final Optional<Boolean> optError) {

    UtilEnvInfo.logStartRequest(request);
    UtilEnvInfo.logStartClassMethod();
    LOGGER.info(
        "The received parameters are: sleep='{}', loop='{}' and error='{}'",
        optSleep, optLoop, optError);

    final ResponseEntity<String> entity = service.rollDice(optSleep, optLoop, optError);

    UtilEnvInfo.logFinishRequest(request);
    return entity;
  }
  // }}}

  // {{{ public List<Dice> listDice(HttpServletRequest request)
  /**
   * サイコロのリストを取得します。.
   *
   * <p>このメソッドは、サービス層からサイコロ（Dice）オブジェクトのリストを取得し、返却します。
   * リクエストの開始と終了時にログを出力します。</p>
   *
   * @param request HTTPリクエスト情報
   * @return サイコロ（Dice）オブジェクトのリスト
   */
  @GetMapping({"/list"})
  @Operation(summary = "サイコロのリストを取得します。", description = "サイコロの出目リストを返却します。")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "サイコロの出目リスト",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = Dice.class))),
      @ApiResponse(responseCode = "500", description = "サーバ内部でエラーが発生",
          content = @Content)
  })
  public List<Dice> listDice(final HttpServletRequest request) {

    UtilEnvInfo.logStartRequest(request);
    UtilEnvInfo.logStartClassMethod();

    final List<Dice> list = service.listDice();

    UtilEnvInfo.logFinishRequest(request);
    return list;
  }
  // }}}

}
