package jp.sios.apisl.handson.rollingdice.webapp.webapi.service;

import java.util.List;
import java.util.Optional;
import jp.sios.apisl.handson.rollingdice.webapp.webapi.dto.DiceDto;
import jp.sios.apisl.handson.rollingdice.webapp.webapi.entity.Dice;

/**
 * サイコロの操作に関するサービスの定義クラスです。.
 * 
 * <p>このインターフェースは、サイコロを振る処理やサイコロの一覧取得処理を提供します。</p>
 * 
 * @author Toshiharu Konuma
 */
public interface WebApiService {

  /**
   * サイコロを振り出目を返します。.
   *
   * @param optSleep サイコロを振る前にスリープする時間（秒）を指定するオプションの整数
   * @param optLoop サイコロを振る前にループで遅延する時間（秒）を指定するオプションの整数
   * @param optError エラーを発生させるかどうかを指定するオプションの真偽値
   * @param fixedDiceRequest サイコロの出目を強制する情報を持つオプションのDiceDtoオブジェクト
   * @return サイコロの出目（1～6）を含むDiceDtoオブジェクト
   */
  DiceDto rollDice(
      Optional<Integer> optSleep,
      Optional<Integer> optLoop,
      Optional<Boolean> optError,
      final DiceDto fixedDiceRequest);

  /**
   * サイコロを振った履歴を一覧で返します。.
   *
   * @return サイコロを振った履歴を保持するDiceオブジェクトのリスト
   */
  List<Dice> listDice();

}
