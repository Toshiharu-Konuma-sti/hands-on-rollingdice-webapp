package jp.sios.apisl.handson.rollingdice.webapp.webui.dto;

/**
 * APIへ送信するリクエストボディ用レコード.
 *
 * @param value 強制したいサイコロの出目
 */
public record DiceRequest(Integer value) {	
}
