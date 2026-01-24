package jp.sios.apisl.handson.rollingdice.webapp.webui.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * サイコロの出目情報を扱うデータクラスです.
 *
 * @param value サイコロの出目（1～6の整数）
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record DiceDto(Integer value) {	
}
