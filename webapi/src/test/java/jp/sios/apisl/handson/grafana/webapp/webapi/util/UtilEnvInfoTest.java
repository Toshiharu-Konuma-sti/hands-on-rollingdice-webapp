package jp.sios.apisl.handson.grafana.webapp.webapi.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class UtilEnvInfoTest {

  @Test
  void testUtilEnvInfoBeanExists() {
    var utilEnvInfo = new UtilEnvInfo();
    assertNotNull(utilEnvInfo, "UtilEnvInfo bean should not be null");
  }

}