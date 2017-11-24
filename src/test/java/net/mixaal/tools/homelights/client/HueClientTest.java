package net.mixaal.tools.homelights.client;

import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import net.mixaal.tools.homelights.utils.CoreUtils;
import org.junit.Test;

public class HueClientTest {

  private static final String ACCESS_KEY = CoreUtils.getAccessKey();
  private static final URI hueService = UriBuilder.fromUri(CoreUtils.getServiceLocation()).build();
  private static final IHueClient client = new HueClient(hueService, ACCESS_KEY);

  @Test
  public void lightOnOff() throws InterruptedException {
    client.lightOff(1);
    client.lightOn(1, 65_000, 200, 100);
    Thread.sleep(1_000);
    client.lightOn(1, 0, 0, 25);
    Thread.sleep(2_000);
    client.lightOn(1, 120, 0, 1);
    Thread.sleep(1_000);
    client.lightOff(1);
  }

}
