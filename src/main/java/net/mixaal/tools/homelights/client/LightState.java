package net.mixaal.tools.homelights.client;

public class LightState {
  private boolean on;
  private Integer hue, sat, bri;

  private LightState(boolean on, Integer hue, Integer sat, Integer bri) {
    this.hue = hue;
    this.bri = bri;
    this.sat = sat;
    this.on = on;
  }

  public static LightState lightOn(Integer hue, Integer sat, Integer bri) {
    return new LightState(true, hue, sat, bri);
  }

  public static LightState lightOff() {
    return new LightState(false, null, null, null);
  }

  public boolean isOn() {
    return on;
  }

  public void setOn(boolean on) {
    this.on = on;
  }

  public Integer getHue() {
    return hue;
  }

  public void setHue(Integer hue) {
    this.hue = hue;
  }

  public Integer getSat() {
    return sat;
  }

  public void setSat(Integer sat) {
    this.sat = sat;
  }

  public Integer getBri() {
    return bri;
  }

  public void setBri(Integer bri) {
    this.bri = bri;
  }
}
