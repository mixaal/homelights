package net.mixaal.tools.homelights.client;

import com.google.gson.Gson;
import java.net.URI;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.mixaal.tools.homelights.IConfig;
import org.glassfish.jersey.filter.LoggingFilter;

public class HueClient implements IHueClient {

  private interface Resources {

    String API = "api";
    String LIGHTS = "lights";
    String STATE = "state";
  }

  private final String accessKey;
  private final URI location;
  private final Client client;
  private final Gson serializer = new Gson();

  public HueClient(final URI location, final String accessKey) {
    this.location = location;
    this.accessKey = accessKey;
    this.client = IConfig.HueClient.DebugMessages
        ? ClientBuilder.newClient().register(new LoggingFilter(Logger.getLogger("OutboundRequestResponse"), true))
        : ClientBuilder.newClient();
    ;
  }

  private WebTarget lightTarget(final Integer lightNo) {
    return client.target(location).path(Resources.API).path(accessKey).path(Resources.LIGHTS)
        .path(lightNo.toString()).path(Resources.STATE);
  }

  private Response lightState(final Integer lightNo, final String message) {
    return lightTarget(lightNo).request().put(
        Entity.entity(message, MediaType.APPLICATION_JSON_TYPE));
  }


  @Override
  public void lightOff(final Integer lightNo) {
    lightState(lightNo, serializer.toJson(LightState.lightOff()));
  }


  @Override
  public void lightOn(final Integer lightNo, final Integer hue, final Integer saturation, final Integer brightness) {
    final String body = serializer.toJson(LightState.lightOn(hue, saturation, brightness));
    lightState(lightNo, body);
  }
}
