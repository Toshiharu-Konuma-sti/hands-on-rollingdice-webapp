package jp.sios.apisl.handson.rollingdice.webapp.webapi.service;

import java.util.List;
import java.util.Optional;
import jp.sios.apisl.handson.rollingdice.webapp.webapi.entity.Dice;
import org.springframework.http.ResponseEntity;

/**
 * WebApiServiceインターフェースは、サイコロの操作に関するWeb APIサービスの機能を定義します。.
 * このインターフェースは、サイコロを振る処理やサイコロの一覧取得処理を提供します。
 */
public interface WebApiService {

  /**
   * サイコロを振る処理を実行します。.
   *
   * @param optSleep サイコロを振る前にスリープする時間（秒）を指定するオプションの整数
   * @param optLoop サイコロを振る前にループで遅延する時間（秒）を指定するオプションの整数
   * @param optError エラーを発生させるかどうかを指定するオプションの真偽値
   * @param fixedValue サイコロの出目を強制するオプションの整数
   * @return サイコロの出目を含むレスポンスエンティティ
   */
  ResponseEntity<String> rollDice(
      Optional<Integer> optSleep,
      Optional<Integer> optLoop,
      Optional<Boolean> optError,
      Optional<Integer> fixedValue);

  /**
   * サイコロ出目の一覧を取得します。.
   *
   * @return Diceオブジェクトのリスト
   */
  List<Dice> listDice();

}
