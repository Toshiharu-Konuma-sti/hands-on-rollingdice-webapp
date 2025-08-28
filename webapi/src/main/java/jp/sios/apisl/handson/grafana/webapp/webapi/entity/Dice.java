package jp.sios.apisl.handson.grafana.webapp.webapi.entity;

import java.time.LocalDateTime;

/**
 * サイコロの情報を表すレコードクラスです。.
 *
 * <p>各サイコロには一意のID、出目の値、最終更新日時が含まれます。</p>
 *
 * @param id        サイコロの一意識別子
 * @param value     サイコロの出目の値
 * @param updateAt  サイコロ情報の最終更新日時
 */
public record Dice(int id, int value, LocalDateTime updateAt) {
}
