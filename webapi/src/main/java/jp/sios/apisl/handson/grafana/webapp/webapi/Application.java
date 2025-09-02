package jp.sios.apisl.handson.grafana.webapp.webapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * アプリケーションのエントリーポイントとなるクラスです。.
 * 
 * <p>Spring Bootアプリケーションを起動するためのmainメソッドを提供します。
 * </p>
 */
@SpringBootApplication
public class Application {
/*
  private Application() {
    // Prevents instantiation.
  }
*/
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