package jp.sios.apisl.handson.rollingdice.webapp.webapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * サイコロAPIのリクエストボディを受け取るRecordです.
 *
 * @param value 強制したいサイコロの出目（ボディーが無い場合はnull）
 */
public record DiceRequest(
    @Schema(description = "サイコロの指定値", example = "3")
    @Min(value = 1, message = "サイコロの値は1以上である必要があります")
    @Max(value = 6, message = "サイコロの値は6以下である必要があります")
    Integer value
) {
}
