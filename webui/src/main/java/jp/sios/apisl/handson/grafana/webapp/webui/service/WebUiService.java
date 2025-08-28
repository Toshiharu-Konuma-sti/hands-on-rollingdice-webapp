package jp.sios.apisl.handson.grafana.webapp.webui.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.json.JSONArray;

/**
 * WebUiServiceインターフェースは、Web UI層から利用されるサービスの定義を提供します。.
 *
 * <p>主にDice APIの呼び出しや、現在のリクエストURLの取得などの機能を提供します。
 * 実装クラスは、APIとの連携やリクエスト処理の詳細を担います。</p>
 */
public interface WebUiService {

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
      Optional<String> optSleep, Optional<String> optLoop, Optional<String> optError);

  /**
   * Dice APIのリスト取得エンドポイントを呼び出し、結果をJSONArrayとして返します。.
   *
   * <p>このメソッドは、/api/dice/v1/list エンドポイントに対してAPIコールを行い、
   * 取得したレスポンスボディをJSONArrayに変換して返却します。
   * </p>
   *
   * @return Dice APIから取得したリスト情報のJSONArray
   */
  public JSONArray callListDiceApi();

  /**
   * 現在のリクエストからURLを取得します。.
   *
   * @param request 現在のHTTPリクエスト
   * @return 現在のURL文字列
   */
  public String getCurrentUrl(HttpServletRequest request);

}
