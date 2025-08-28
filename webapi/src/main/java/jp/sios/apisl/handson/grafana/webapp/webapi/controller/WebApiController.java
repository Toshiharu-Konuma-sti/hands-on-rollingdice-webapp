package jp.sios.apisl.handson.grafana.webapp.webapi.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import jp.sios.apisl.handson.grafana.webapp.webapi.entity.Dice;
import jp.sios.apisl.handson.grafana.webapp.webapi.service.WebApiService;
import jp.sios.apisl.handson.grafana.webapp.webapi.util.UtilEnvInfo;
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
public class WebApiController {

  private static final Logger logger = LoggerFactory.getLogger(WebApiController.class);
  private final WebApiService service;

  // {{{ public WebApiController(WebApiService service)
  /**
   * WebApiControllerのコンストラクタです。.
   *
   * @param service WebApiServiceのインスタンス
   */
  public WebApiController(WebApiService service) {
    this.service = service;
  }
  // }}}

  // {{{ public ResponseEntity<Integer> rollDice(...)
  /**
   * サイコロを振る処理を実行します。.
   *
   * <p>リクエストパラメータとして、sleep（待機時間）、loop（ループ回数）、error（エラー発生フラグ）を受け取ります。
   * これらのパラメータに基づき、サービス層でサイコロの値を生成し、結果をレスポンスとして返却します。
   * </p>
   *
   * @param request   HTTPリクエスト情報
   * @param optSleep  サイコロ処理前の待機時間（ミリ秒、オプション）
   * @param optLoop   サイコロ処理のループ回数（オプション）
   * @param optError  エラー発生フラグ（オプション）
   * @return サイコロの出目（1～6の整数値）を含むResponseEntity
   */
  @GetMapping(value = {"/roll"})
  public ResponseEntity<Integer> rollDice(
      HttpServletRequest request,
      @RequestParam("sleep") Optional<String> optSleep,
      @RequestParam("loop") Optional<String> optLoop,
      @RequestParam("error") Optional<String> optError) {
    UtilEnvInfo.logStartRequest(request);
    UtilEnvInfo.logStartClassMethod();
    logger.info("The received parameters are: sleep='{}', loop='{}' and error='{}'", optSleep, optLoop, optError);

    ResponseEntity<Integer> entity = service.rollDice(optSleep, optLoop, optError);

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
  @GetMapping(value = {"/list"})
  public List<Dice> listDice(HttpServletRequest request) {
    UtilEnvInfo.logStartRequest(request);
    UtilEnvInfo.logStartClassMethod();

    List<Dice> list = service.listDice();

    UtilEnvInfo.logFinishRequest(request);
    return list;
  }
  // }}}

}