package jp.sios.apisl.handson.grafana.webapp.webui.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import jp.sios.apisl.handson.grafana.webapp.webui.service.WebUiService;
import jp.sios.apisl.handson.grafana.webapp.webui.util.UtilEnvInfo;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Web UI コントローラークラス。.
 *
 * <p>このクラスは、Webアプリケーションのルートパス（"/"）へのリクエストを処理します。
 * サービスクラス {@link WebUiService} を利用して、Dice APIの呼び出しや
 * 必要な情報の取得を行い、ビューにデータを渡します。
 * </p>
 *
 * <p>主な機能:</p>
 * <ul>
 *   <li>リクエストパラメータ（sleep, loop, error）の受け取りとログ出力</li>
 *   <li>Dice APIの呼び出しと結果の取得</li>
 *   <li>ビューへのデータ設定と画面遷移</li>
 * </ul>
 *
 * @author Toshiharu Konuma
 */
@Controller
public class WebUiController {

  private static final Logger logger = LoggerFactory.getLogger(WebUiController.class);
  private final WebUiService service;

  // {{{ public WebUiController(WebUiService service)
  /**
   * WebUiServiceのインスタンスを受け取り、WebUiControllerを初期化します。.
   *
   * @param service Web UIのサービスロジックを提供するWebUiServiceのインスタンス
   */
  public WebUiController(WebUiService service) {
    this.service = service;
  }
  // }}}

  // {{{ public ModelAndView index(...)
  /**
   * ルートパス（"/"）へのリクエストを処理するコントローラメソッドです。.
   *
   * <p>リクエストパラメータとして "sleep"、"loop"、"error" を受け取り、サービスクラスを利用して
   * Dice APIの呼び出しやリスト取得、現在のURLの取得を行います。取得した情報をModelAndViewに格納し、
   * "index" ビューを返します。
   * </p>
   *
   * @param request   HTTPリクエストオブジェクト
   * @param model     ModelAndViewオブジェクト
   * @param optSleep  "sleep" パラメータ（オプション）
   * @param optLoop   "loop" パラメータ（オプション）
   * @param optError  "error" パラメータ（オプション）
   * @return          "index" ビュー名を持つModelAndViewオブジェクト
   */
  @RequestMapping(value = {"/"})
  public ModelAndView index(
      HttpServletRequest request, ModelAndView model,
      @RequestParam("sleep") Optional<String> optSleep,
      @RequestParam("loop") Optional<String> optLoop,
      @RequestParam("error") Optional<String> optError) {

    UtilEnvInfo.logStartRequest(request);
    UtilEnvInfo.logStartClassMethod();
    logger.info("The received request parameters are: sleep='{}', loop='{}' and error='{}'", optSleep, optLoop, optError);

    String dice = this.service.callRollDiceApi(optSleep, optLoop, optError);
    JSONArray diceList = this.service.callListDiceApi();
    String currentUrl = this.service.getCurrentUrl(request);

    model.addObject("dice", dice);
    model.addObject("list", diceList);
    model.addObject("cUrl", currentUrl);

    model.setViewName("index");

    UtilEnvInfo.logFinishRequest(request);
    return model;
  }
  // }}}

}