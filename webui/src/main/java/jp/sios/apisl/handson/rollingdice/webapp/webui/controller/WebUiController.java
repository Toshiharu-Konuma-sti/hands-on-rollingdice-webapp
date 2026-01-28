package jp.sios.apisl.handson.rollingdice.webapp.webui.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import jp.sios.apisl.handson.rollingdice.webapp.webui.dto.DiceHistoryDto;
import jp.sios.apisl.handson.rollingdice.webapp.webui.service.WebUiService;
import jp.sios.apisl.handson.rollingdice.webapp.webui.util.UtilEnvInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * サイコロWebアプリケーションのエンドポイントを管理するクラスです。.
 *
 * <p>Webアプリケーションへのリクエストを処理します。
 * サービスクラス {@link WebUiService} を利用してビジネスロジックを処理して、
 * ビューにデータを渡します。</p>
 * <ul>
 *   <li>ルートパス（"/"）</li>
 * </ul>
 *
 * @author Toshiharu Konuma
 */
@Controller
@SuppressWarnings({"PMD.CommentSize"})
public class WebUiController {

  /**
   * ログ出力を行うためのロガーインスタンス。
   * このコントローラークラス内の処理状況やエラー情報を記録します。.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(WebUiController.class);
  
  /**
   * Web UIのサービスロジックを提供するWebUiServiceのインスタンス。.
   */
  private final WebUiService service;

  // {{{ public WebUiController(WebUiService service)
  /**
   * {@link WebUiService}のインスタンスを受け取り、WebUiControllerを初期化します。.
   *
   * @param service Webアプリケーションのサービスを提供する{@link WebUiService}のインスタンス
   */
  public WebUiController(final WebUiService service) {
    this.service = service;
  }
  // }}}

  // {{{ public ModelAndView index(...)
  /**
   * ルートパス（"/"）へのリクエストを処理するコントローラメソッドです。.
   *
   * <p>リクエストパラメータとして "sleep"、"loop"、"error"、"value" を受け取り、
   * サービスクラスを利用してサイコロを振って出目履歴の一覧を取得します。
   * 取得した情報をModelAndViewに格納し、"index" ビューを返します。</p>
   *
   * @param request   HTTPリクエストオブジェクト
   * @param model     ModelAndViewオブジェクト
   * @param optSleep  "sleep" パラメータ（オプション）
   * @param optLoop   "loop" パラメータ（オプション）
   * @param optError  "error" パラメータ（オプション）
   * @param optValue  "value" パラメータ（オプション）
   * @return          "index" ビュー名を持つModelAndViewオブジェクト
   */
  @RequestMapping({"/"})
  public ModelAndView index(
      final HttpServletRequest request,
      final ModelAndView model,
      @RequestParam("sleep") final Optional<String> optSleep,
      @RequestParam("loop") final Optional<String> optLoop,
      @RequestParam("error") final Optional<String> optError,
      @RequestParam("value") final Optional<Integer> optValue) {

    UtilEnvInfo.logStartRequest(request);
    UtilEnvInfo.logStartClassMethod();
    LOGGER.info(
        "The received request parameters are: sleep='{}', loop='{}', error='{}' and value='{}'",
        optSleep, optLoop, optError, optValue);

    final String dice = this.service.callRollDiceApi(optSleep, optLoop, optError, optValue);
    final List<DiceHistoryDto> diceList = this.service.callListDiceApi();
    final String currentUrl = this.service.getCurrentUrl(request);

    model.addObject("dice", dice);
    model.addObject("list", diceList);
    model.addObject("cUrl", currentUrl);

    model.setViewName("index");

    UtilEnvInfo.logFinishRequest(request);
    return model;
  }
  // }}}

}
