package jp.sios.apisl.handson.rollingdice.webapp.webapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * サイコロの出目情報を扱うデータクラスです.
 * <p>APIのリクエスト、およびレスポンスで共通利用します。</p>
 *
 * @param value サイコロの出目（1～6の整数）
 */
public record DiceDto(
    @Schema(description = "サイコロの出目（1～6の整数）", example = "3")
    @Min(value = 1, message = "サイコロの値は1以上である必要があります")
    @Max(value = 6, message = "サイコロの値は6以下である必要があります")
    Integer value
) {
}
