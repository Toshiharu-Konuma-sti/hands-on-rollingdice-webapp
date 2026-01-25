package jp.sios.apisl.handson.rollingdice.webapp.webapi.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * サイコロの出目履歴を扱うレコードクラスです。.
 *
 * <p>出目履歴には一意の履歴ID、出目の値、振った日時が含まれます。</p>
 *
 * @param id        出目の履歴ID（一意の識別子）
 * @param value     サイコロの出目（1～6の整数）
 * @param updateAt  サイコロを振った日時
 */
@Schema(description = "サイコロの出目履歴情報")
public record DiceEntity(
    @Schema(description = "出目の履歴ID（一意の識別子）", example = "12")
    int id,
    @Schema(description = "サイコロの出目（1～6の整数）", example = "3")
    int value,
    @Schema(description = "サイコロを振った日時", example = "2026-04-01T12:34:56")
    LocalDateTime updatedAt
) {
}
