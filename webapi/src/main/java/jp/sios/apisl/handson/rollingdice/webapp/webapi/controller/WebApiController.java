package jp.sios.apisl.handson.rollingdice.webapp.webapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import jp.sios.apisl.handson.rollingdice.webapp.webapi.dto.DiceValueDto;
import jp.sios.apisl.handson.rollingdice.webapp.webapi.entity.DiceEntity;
import jp.sios.apisl.handson.rollingdice.webapp.webapi.service.WebApiService;
import jp.sios.apisl.handson.rollingdice.webapp.webapi.util.UtilEnvInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * サイコロWeb APIのエンドポイントを管理するクラスです。.
 * 
 * <ul>
 *   <li>サイコロを振るAPI（/api/dice/v1/roll）</li>
 *   <li>サイコロを振った履歴を一覧で取得するAPI（/api/dice/v1/list）</li>
 * </ul>
 *
 * <p>各エンドポイントではサービス層への処理委譲を行います。
 * </p>
 *
 * @author Toshiharu Konuma
 */
@RestController
@RequestMapping("/api/dice/v1")
@Tag(name = "web-api-controller")
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
   * サイコロを振る処理を扱います。.
   *
   * <p>通常はサイコロを振った結果の出目を返却しますが、リクエストボディに出目が指定されている場合には、
   * 振らずにその値を出目として採用します。
   * また、リクエストパラメータ（sleep, loop, error）を指定することで処理の挙動を制御します。</p>
   *
   * @param request   HTTPリクエスト情報
   * @param requestBody サイコロの出目を強制する場合に使用するリクエストボディ
   * @param optSleep  サイコロを振る前に意図的に遅延させる待機時間（秒、オプション）
   * @param optLoop   サイコロを振る前に意図的に遅延させるループ時間（秒、オプション）
   * @param optError  サイコロを振らずにエラーを発生させるフラグ（boolean、オプション）
   * @return サイコロの出目（1～6）を含む{@link DiceValueDto}オブジェクト
   */
  @PostMapping({"/roll"})
  @Operation(operationId = "Roll Dice",
      summary = "サイコロを振ります。", 
      description = "通常はサイコロを振った結果の出目を返却しますが、リクエストボディに出目が指定されている場合には、振らずにその値を出目として採用します。また、リクエストパラメータ（sleep, loop, error）を指定することで処理の挙動を制御します。")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "リクエストが正常に処理",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = DiceValueDto.class))),
      @ApiResponse(responseCode = "500", description = "errorパラメータが指定されて例外が発生、もしくはサーバ内部でエラーが発生",
          content = @Content)
  })
  public DiceValueDto rollDice(
      final HttpServletRequest request,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "サイコロの出目を強制したい場合に送信", required = false)
      @RequestBody(required = false) @Validated final DiceValueDto requestBody,
      @Parameter(description = "サイコロを振る前に意図的に遅延させる待機時間（秒）", example = "10")
      @RequestParam(name = "sleep", required = false) final Optional<Integer> optSleep,
      @Parameter(description = "サイコロを振る前に意図的に遅延させるループ時間（秒）", example = "15")
      @RequestParam(name = "loop", required = false) final Optional<Integer> optLoop,
      @Parameter(description = "サイコロを振らずにエラーを発生させるフラグ", example = "true")
      @RequestParam(name = "error", required = false) final Optional<Boolean> optError) {

    UtilEnvInfo.logStartRequest(request);
    UtilEnvInfo.logStartClassMethod();
    LOGGER.info(
        "The received parameters are: body='{}', sleep='{}', loop='{}' and error='{}'",
        requestBody, optSleep, optLoop, optError);

    final DiceValueDto responseDto = service.rollDice(optSleep, optLoop, optError, requestBody);

    UtilEnvInfo.logFinishRequest(request);
    return responseDto;
  }
  // }}}

  // {{{ public List<Dice> listDice(HttpServletRequest request)
  /**
   * サイコロを振った履歴を一覧で取得する処理を扱います。.
   *
   * <p>サイコロの出目履歴を、振った日時が新しい順（降順）で返却します。</p>
   *
   * @param request HTTPリクエスト情報
   * @return サイコロを振った履歴を保持する{@link DiceEntity}オブジェクトのリスト
   */
  @GetMapping({"/list"})
  @Operation(operationId = "List Dice",
      summary = "サイコロを振った履歴を一覧で取得します。",
      description = "サイコロの出目履歴を、振った日時が新しい順（降順）で返却します。")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "リクエストが正常に処理",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = DiceEntity.class))),
      @ApiResponse(responseCode = "500", description = "サーバ内部でエラーが発生",
          content = @Content)
  })
  public List<DiceEntity> listDice(final HttpServletRequest request) {

    UtilEnvInfo.logStartRequest(request);
    UtilEnvInfo.logStartClassMethod();

    final List<DiceEntity> list = service.listDice();

    UtilEnvInfo.logFinishRequest(request);
    return list;
  }
  // }}}

}
