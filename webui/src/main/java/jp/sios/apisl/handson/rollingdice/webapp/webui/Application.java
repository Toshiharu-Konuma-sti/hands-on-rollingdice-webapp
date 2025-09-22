package jp.sios.apisl.handson.rollingdice.webapp.webui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * アプリケーションのエントリーポイントとなるクラスです。.
 * 
 * <p>このクラスはSpring Bootアプリケーションとして起動されます。
 * {@code main} メソッドからアプリケーション全体を起動します。
 * </p>
 *
 * @author Toshiharu Konuma
 */
@SpringBootApplication
@SuppressWarnings("PMD.UseUtilityClass")
public class Application {

  // {{{ public static void main(String[] args)
  /**
   * アプリケーションのエントリーポイントとなるメインメソッドです。
   * Spring Bootアプリケーションを起動します。.
   *
   * @param args コマンドライン引数
   */
  public static void main(final String[] args) {
    SpringApplication.run(Application.class, args);
  }
  // }}}

}
