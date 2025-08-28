package jp.sios.apisl.handson.grafana.webapp.webui;

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
public class Application {

  // {{{ public Application() {
  /**
   * アプリケーションクラスのため、インスタンス化は行いません。.
   */
  public Application() {
    // A default constructor.
  }
  // }}}

  // {{{ public static void main(String[] args)
  /**
   * アプリケーションのエントリーポイントとなるメインメソッドです。
   * Spring Bootアプリケーションを起動します。.
   *
   * @param args コマンドライン引数
   */
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
  // }}}

}
