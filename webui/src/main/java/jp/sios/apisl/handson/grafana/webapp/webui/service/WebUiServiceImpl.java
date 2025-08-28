package jp.sios.apisl.handson.grafana.webapp.webui.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import jp.sios.apisl.handson.grafana.webapp.webui.util.UtilEnvInfo;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

/**
 * WebUiServiceImplは、Web UI層からWeb APIへのリクエストを仲介するサービス実装クラスです。.
 *
 * <p>Dice APIの呼び出しや、サイコロリストの取得、現在のURLの取得などの機能を提供します。
 * RestClientを利用して外部APIと通信し、必要に応じてリクエストパラメータを組み立ててAPIを呼び出します。
 * </p>
 * <ul>
 *   <li>Dice APIの呼び出し（roll, list）</li>
 *   <li>API呼び出し時のログ出力</li>
 *   <li>現在のリクエストURLの取得</li>
 * </ul>
 *
 * @author Toshiharu Konuma
 */
@Service
public class WebUiServiceImpl implements WebUiService {

  private static final Logger logger = LoggerFactory.getLogger(WebUiServiceImpl.class);
  private final RestClient restClient;

  @Value("${handson.webapp.webapi.host}")
  private String webapiHost;

  // {{{ public WebUiServiceImpl(RestClient restClient)
  /**
   * RestClientを使用してWebUiServiceImplのインスタンスを生成します。.
   *
   * @param restClient Web UIサービスで利用するRestClientのインスタンス
   */
  public WebUiServiceImpl(RestClient restClient) {
    this.restClient = restClient;
  }
  // }}}

  // {{{ public String callRollDiceApi(Optional<String> optSleep, Optional<String> optLoop, Op ... )
  /**
   * Roll Dice APIを呼び出すメソッドです。.
   *
   * <p>オプションでスリープ時間、ループ回数、エラー発生のパラメータを指定できます。
   * 指定されたパラメータはAPIリクエストのクエリパラメータとして付与されます。
   * </p>
   *
   * @param optSleep スリープ時間（ミリ秒）を表すオプショナルな文字列
   * @param optLoop  ループ回数を表すオプショナルな文字列
   * @param optError エラー発生を示すオプショナルな文字列
   * @return APIからのレスポンスボディ（文字列）
   */
  public String callRollDiceApi(
      Optional<String> optSleep, Optional<String> optLoop,  Optional<String> optError) {
    UtilEnvInfo.logStartClassMethod();
    logger.info("The received request parameters are: sleep='{}', loop='{}' and error='{}'", optSleep, optLoop, optError);

    List<String> paramList = new ArrayList<String>();
    optSleep.ifPresent(sleep -> paramList.add("sleep=" + sleep));
    optLoop.ifPresent(loop -> paramList.add("loop=" + loop));
    optError.ifPresent(error -> paramList.add("error=" + error));
    String path = "/api/dice/v1/roll";
    if (paramList.size() > 0) {
      path += "?" + String.join("&", paramList);
    }

    String body = this.callApi(path, "0");

    return body;
  }
  // }}}

  // {{{ public JSONArray callListDiceApi()
  /**
   * Dice APIのリスト取得エンドポイントを呼び出し、結果をJSONArrayとして返します。.
   *
   * <p>このメソッドは、/api/dice/v1/list エンドポイントに対してAPIコールを行い、
   * 取得したレスポンスボディをJSONArrayに変換して返却します。
   * </p>
   *
   * @return Dice APIから取得したリスト情報のJSONArray
   */
  public JSONArray callListDiceApi() {
    UtilEnvInfo.logStartClassMethod();

    String path = "/api/dice/v1/list";
    String body = this.callApi(path, "");

    JSONArray jsonList = new JSONArray(body);
    logger.info("The object converted to json is {}", jsonList);

    return jsonList;
  }
  // }}}

  // {{{ private String callApi(String path, String body)
  /**
   * 指定されたパスに対してAPIコールを行い、レスポンスボディを文字列として返します。.
   *
   * @param path APIのエンドポイントパス
   * @param body APIリクエストで例外発生時にデフォルトで返すレスポンスボディに該当する値
   * @return APIから取得したレスポンスボディの文字列
   * @throws HttpClientErrorException クライアントエラーが発生した場合
   * @throws HttpServerErrorException サーバーエラーが発生した場合
   */
  private String callApi(String path, String body) {
    UtilEnvInfo.logStartClassMethod();

    String url = "http://" + this.webapiHost + path;
    logger.info("The URL to call the API is: '{}'", url);

    try {
      body = this.restClient.get().uri(url).retrieve().body(String.class);
      logger.info("The value recieved from the rolldice api is: '{}'", body);
    } catch (HttpClientErrorException | HttpServerErrorException ex) {
      logger.error("!!! Could not get a response from the API, because an exception was happened: '{}' !!!", (Object[]) ex.getStackTrace());
    }

    return body;
  }
  // }}}

  // {{{ public String getCurrentUrl(HttpServletRequest request)
  /**
   * 現在のリクエストからURLを取得します。.
   *
   * @param request 現在のHTTPリクエスト
   * @return 現在のURL文字列
   */
  public String getCurrentUrl(HttpServletRequest request) {
    UtilEnvInfo.logStartClassMethod();

    String currentUrl = UtilEnvInfo.getCurrentUrl(request);
    logger.info("The current URL is: {}", currentUrl);

    return currentUrl;
  }
  // }}}

}
