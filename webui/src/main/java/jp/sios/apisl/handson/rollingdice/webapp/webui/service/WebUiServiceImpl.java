package jp.sios.apisl.handson.rollingdice.webapp.webui.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import jp.sios.apisl.handson.rollingdice.webapp.webui.dto.DiceValueDto;
import jp.sios.apisl.handson.rollingdice.webapp.webui.dto.DiceHistoryDto;
import jp.sios.apisl.handson.rollingdice.webapp.webui.util.UtilEnvInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClient;

/**
 * Webアプリケーションの制御に関するサービスの実装クラスです。.
 *
 * <p>このクラスは、サイコロWeb APIの呼び出しなどを処理する機能の実装を提供します。</p>
 * <ul>
 *   <li>サイコロWeb APIの呼び出し（roll, list）</li>
 *   <li>リクエストしているURLの取得</li>
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
   * サイコロWeb APIのRoll Diceを呼び出すメソッドです。.
   *
   * <p>オプションでスリープ時間、ループ時間、エラー発生の有無と出目を強制するパラメータを指定できます。
   * 指定されたパラメータは、APIリクエストのクエリパラメータやリクエストボディーとして送信します。
   * </p>
   *
   * @param optSleep スリープ時間（秒）を表すオプショナルな文字列
   * @param optLoop  ループ時間（秒）を表すオプショナルな文字列
   * @param optError エラー発生を示すオプショナルな文字列
   * @param fixedValue サイコロの出目を強制するオプションの整数
   * @return サイコロを振った結果の出目（文字列）
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

    DiceValueDto requestBody = null;
    if (fixedValue.isPresent()) {
      requestBody = new DiceValueDto(fixedValue.get());
      LOGGER.info("The request body to send to the API is: '{}'", requestBody);
    }

    final DiceValueDto response = this.callApi(path, HttpMethod.POST, requestBody, DiceValueDto.class);

    String returnValue = "0";
    if (response != null && response.value() != null) {
      returnValue = String.valueOf(response.value());
    }

    return returnValue;
  }
  // }}}

  // {{{ public JSONArray callListDiceApi()
  /**
   * サイコロWeb APIのList Diceを呼び出すメソッドです。.
   *
   * <p>このメソッドは、サイコロWeb APIのList Diceをコールし、
   * 取得したレスポンスボディを{@link DiceHistoryDto}のリストで返却します。
   * </p>
   *
   * @return Dice APIから取得したリスト情報のJSONArray
   */
  @Override
  public List<DiceHistoryDto> callListDiceApi() {
    UtilEnvInfo.logStartClassMethod();

    final String path = "/api/dice/v1/list";
    List<DiceHistoryDto> list = this.callApi(path, HttpMethod.GET, null, new ParameterizedTypeReference<List<DiceHistoryDto>>() {});

    if (list == null) {
      list = Collections.emptyList();
    }

    return list;
  }
  // }}}

  // {{{ private <T> T callApi(...)
  /**
   * 指定されたパスに対してAPIコールを行い、レスポンスボディを指定のクラスで返します。.
   *
   * @param path APIのエンドポイントパス
   * @param method HTTPメソッド (GET, POSTなど)
   * @param requestBody 送信するボディ (送信するデータが無い場合はnull)
   * @param responseType レスポンスをマッピングするクラス
   * @return APIから取得したレスポンスボディ
   */
  private <T> T callApi(String path, HttpMethod method, Object requestBody, Class<T> responseType) {
    return callApi(path, method, requestBody, ParameterizedTypeReference.forType(responseType));
  }
  // }}}

  // {{{ private <T> T callApi(...)
  /**
   * 指定されたパスに対してAPIコールを行い、レスポンスボディを指定のクラスで返します。.
   *
   * @param path APIのエンドポイントパス
   * @param method HTTPメソッド (GET, POSTなど)
   * @param requestBody 送信するボディ (送信するデータが無い場合はnull)
   * @param responseType レスポンスをマッピングするクラス
   * @return APIから取得したレスポンスボディ
   */
  private <T> T callApi(
      final String path,
      final HttpMethod method,
      final Object requestBody,
      final ParameterizedTypeReference<T> responseType) {
    UtilEnvInfo.logStartClassMethod();

    final String url = "http://" + this.webapiHost + path;
    LOGGER.info("Calling API is: URL='{}', Method='{}', Body='{}'", url, method, requestBody);

    T response = null;
    try {
      var requestSpec = this.restClient.method(method).uri(url);
      if (requestBody != null) {
        requestSpec.contentType(MediaType.APPLICATION_JSON).body(requestBody);
      }
      response = requestSpec.retrieve().body(responseType);
      LOGGER.info("The value recieved from the rolldice api is: '{}'", response);
    } catch (RestClientException ex) {
      LOGGER.error("!!! Could not get a response from the API, because an exception was happened !!!", ex);
    }

    return response;
  }
  // }}}

  // {{{ public String getCurrentUrl(HttpServletRequest request)
  /**
   * リクエストしているURLを取得します。.
   *
   * @param request 送信されてきたHTTPリクエスト
   * @return リクエストしているURL文字列
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
