package net.mixaal.tools.homelights.client;


/**
 * Basic philips hue client.
 */
public interface IHueClient {

  /**
   * Turn the light off.
   *
   * @param lightNo light number.
   */
  void lightOff(final Integer lightNo);

  /**
   * Turn on the light and set the color.
   *
   * @param lightNo light number
   * @param hue color hue
   * @param saturation color saturation
   * @param brightness color brightness
   */
  void lightOn(final Integer lightNo, final Integer hue, final Integer saturation, final Integer brightness);

}
