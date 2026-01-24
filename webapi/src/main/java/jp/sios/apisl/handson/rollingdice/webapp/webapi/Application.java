package jp.sios.apisl.handson.rollingdice.webapp.webapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * サイコロWeb APIの起動クラスです。.
 * 
 * <p>Spring Bootアプリケーションを起動するためのmainメソッドを提供します。
 * </p>
 */
@SpringBootApplication
@SuppressWarnings("PMD.UseUtilityClass")
public class Application {

  // {{{ public static void main(String[] args)
  /**
   * アプリケーションのエントリーポイントとなるメインメソッドです。.
   * Spring Bootアプリケーションを起動します。
   *
   * @param args コマンドライン引数
   */
  public static void main(final String[] args) {
    SpringApplication.run(Application.class, args);
  }
  // }}}

}
