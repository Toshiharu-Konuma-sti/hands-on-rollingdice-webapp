package jp.sios.apisl.handson.rollingdice.webapp.webui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * サイコロWebアプリケーションの起動クラスです。.
 * 
 * <p>Spring Bootアプリケーションを起動するための{@code main}メソッドを提供します。</p>
 *
 * @author Toshiharu Konuma
 */
@SpringBootApplication
@SuppressWarnings("PMD.UseUtilityClass")
public class Application {

  // {{{ public static void main(String[] args)
  /**
   * アプリケーションのエントリーポイントとなるメインメソッドです。.
   *
   * @param args コマンドライン引数
   */
  public static void main(final String[] args) {
    SpringApplication.run(Application.class, args);
  }
  // }}}

}
