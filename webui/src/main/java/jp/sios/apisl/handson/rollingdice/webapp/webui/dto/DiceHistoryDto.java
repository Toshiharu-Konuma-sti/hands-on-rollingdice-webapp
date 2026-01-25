package jp.sios.apisl.handson.rollingdice.webapp.webui.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * サイコロの出目履歴を扱うデータクラスです。.
 *
 * <p>出目履歴には一意の履歴ID、出目の値、振った日時が含まれます。</p>
 *
 * @param id        出目の履歴ID（一意の識別子）
 * @param value     サイコロの出目（1～6の整数）
 * @param updatedAt  サイコロを振った日時
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record DiceHistoryDto(
  Integer id,
  Integer value,
  String updatedAt
) {
}