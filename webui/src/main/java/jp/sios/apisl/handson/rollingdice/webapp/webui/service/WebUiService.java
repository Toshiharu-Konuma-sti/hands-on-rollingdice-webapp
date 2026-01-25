package jp.sios.apisl.handson.rollingdice.webapp.webui.service;

import jakarta.servlet.http.HttpServletRequest;
import jp.sios.apisl.handson.rollingdice.webapp.webui.dto.DiceHistoryDto;

import java.util.List;
import java.util.Optional;
import org.json.JSONArray;

/**
 * Webアプリケーションの制御に関するサービスの定義クラスです。.
 *
 * <p>このインターフェースは、サイコロWeb APIの呼び出しなどを処理する機能の定義を提供します。</p>
 */
public interface WebUiService {

  /**
   * サイコロWeb APIのRoll Diceを呼び出すメソッドです。.
   *
   * @param optSleep スリープ時間（秒）を表すオプショナルな文字列
   * @param optLoop  ループ時間（秒）を表すオプショナルな文字列
   * @param optError エラー発生を示すオプショナルな文字列
   * @return APIからのレスポンスボディ（文字列）
   */
  String callRollDiceApi(
      Optional<String> optSleep, Optional<String> optLoop, Optional<String> optError, Optional<Integer> fixedValue);

  /**
   * サイコロWeb APIのList Diceを呼び出すメソッドです。.
   *
   * @return サイコロWeb APIから取得したリスト情報
   */
  List<DiceHistoryDto> callListDiceApi();

  /**
   * リクエストしているURLを取得します。.
   *
   * @param request HTTPリクエスト
   * @return アクセスされたURL文字列
   */
  String getCurrentUrl(HttpServletRequest request);

}
