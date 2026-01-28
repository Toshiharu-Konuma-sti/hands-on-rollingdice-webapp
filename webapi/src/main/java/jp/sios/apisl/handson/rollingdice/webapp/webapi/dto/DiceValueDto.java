package jp.sios.apisl.handson.rollingdice.webapp.webapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * サイコロの出目情報を扱うデータクラスです。.
 * 
 * <p>APIのリクエスト、およびレスポンスで共通利用します。</p>
 *
 * @param value サイコロの出目（1～6の整数）
 */
@Schema(description = "サイコロの出目情報")
public record DiceValueDto(
    @Schema(description = "サイコロの出目（1～6の整数）", example = "3")
    @Min(value = 1, message = "The dice value must be 1 or greater.")
    @Max(value = 6, message = "The dice value must be 6 or less.")
    Integer value
) {
}
