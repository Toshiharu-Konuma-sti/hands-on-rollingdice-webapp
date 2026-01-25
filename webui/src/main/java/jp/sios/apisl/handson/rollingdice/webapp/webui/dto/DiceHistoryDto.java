package jp.sios.apisl.handson.rollingdice.webapp.webui.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DiceHistoryDto(
  Integer id,
  Integer value,
  String updatedAt
) {
}