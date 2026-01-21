package jp.sios.apisl.handson.rollingdice.webapp.webui.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jp.sios.apisl.handson.rollingdice.webapp.webui.dto.DiceRequest;
import jp.sios.apisl.handson.rollingdice.webapp.webui.util.UtilEnvInfo;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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

  /**
   * ログ出力を行うためのロガーインスタンス。
   * このサービスクラス内の各種処理における情報、警告、エラーなどのログを記録します。.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(WebUiServiceImpl.class);

  /**
   * REST APIとの通信を行うためのRestClientインスタンス。.
   */
  private final RestClient restClient;

  /**
   * Web APIのホスト名またはアドレスを保持するフィールドです。.
   */
  @Value("${handson.webapp.webapi.host}")
  private String webapiHost;

  // {{{ public WebUiServiceImpl(RestClient restClient)
  /**
   * RestClientを使用してWebUiServiceImplのインスタンスを生成します。.
   *
   * @param restClient Web UIサービスで利用するRestClientのインスタンス
   */
  public WebUiServiceImpl(final RestClient restClient) {
    this.restClient = restClient;
  }
  // }}}

  // {{{ public String callRollDiceApi(Optional<String> optSleep, Optional<String> optLoop, Op ... )
  /**
   * Roll Dice APIを呼び出すメソッドです。.
   *
   * <p>オプションでスリープ時間、ループ時間、エラー発生のパラメータを指定できます。
   * 指定されたパラメータはAPIリクエストのクエリパラメータとして付与されます。
   * </p>
   *
   * @param optSleep スリープ時間（秒）を表すオプショナルな文字列
   * @param optLoop  ループ時間（秒）を表すオプショナルな文字列
   * @param optError エラー発生を示すオプショナルな文字列
   * @param fixedValue サイコロの出目を強制するオプションの整数
   * @return APIからのレスポンスボディ（文字列）
   */
  @Override
  public String callRollDiceApi(
      final Optional<String> optSleep,
      final Optional<String> optLoop,
      final Optional<String> optError,
    final Optional<Integer> fixedValue) {
    UtilEnvInfo.logStartClassMethod();
    LOGGER.info("The received request parameters are: sleep='{}', loop='{}', error='{}' and fixedValue='{}' ", optSleep, optLoop, optError, fixedValue);

    final List<String> paramList = new ArrayList<>();
    optSleep.ifPresent(sleep -> paramList.add("sleep=" + sleep));
    optLoop.ifPresent(loop -> paramList.add("loop=" + loop));
    optError.ifPresent(error -> paramList.add("error=" + error));

    String path = "/api/dice/v1/roll";
    if (!paramList.isEmpty()) {
      path += "?" + String.join("&", paramList);
    }

    DiceRequest requestBody = null;
    if (fixedValue.isPresent()) {
      requestBody = new DiceRequest(fixedValue.get());
      LOGGER.info("The request body to send to the API is: '{}'", requestBody);
    }

    return this.callApi(path, HttpMethod.POST, requestBody, "0");
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
  @Override
  public JSONArray callListDiceApi() {
    UtilEnvInfo.logStartClassMethod();

    final String path = "/api/dice/v1/list";
    final String body = this.callApi(path, HttpMethod.GET, null, "");

    final JSONArray jsonList = new JSONArray(body);
    LOGGER.info("The object converted to json is {}", jsonList);

    return jsonList;
  }
  // }}}

  // {{{ private String callApi(String path, String body)
  /**
   * 指定されたパスに対してAPIコールを行い、レスポンスボディを文字列として返します。.
   *
   * @param path APIのエンドポイントパス
   * @param method HTTPメソッド (GET, POSTなど)
   * @param requestBody 送信するボディ (ない場合はnull)
   * @param defaultBody APIリクエストで例外発生時に返すデフォルトのレスポンスボディ
   * @return APIから取得したレスポンスボディの文字列。例外発生時はdefaultBodyを返します。
   */
  private String callApi(
      final String path,
      final HttpMethod method,
      final Object requestBody,
      final String defaultBody) {
    UtilEnvInfo.logStartClassMethod();

    final String url = "http://" + this.webapiHost + path;
    LOGGER.info("Calling API is: URL='{}', Method='{}', Body='{}'", url, method, requestBody);

    String body = defaultBody;
    try {
      var requestSpec = this.restClient.method(method).uri(url);
      if (requestBody != null) {
        requestSpec.contentType(MediaType.APPLICATION_JSON).body(requestBody);
      }
      body = requestSpec.retrieve().body(String.class);
      LOGGER.info("The value recieved from the rolldice api is: '{}'", body);
    } catch (HttpClientErrorException | HttpServerErrorException ex) {
      LOGGER.error("!!! Could not get a response from the API, because an exception was happened !!!", ex);
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
  @Override
  public String getCurrentUrl(final HttpServletRequest request) {
    UtilEnvInfo.logStartClassMethod();

    final String currentUrl = UtilEnvInfo.getCurrentUrl(request);
    LOGGER.info("The current URL is: {}", currentUrl);

    return currentUrl;
  }
  // }}}

}
