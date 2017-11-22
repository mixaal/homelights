package net.mixaal.tools.homelights;

/**
 * Main configuration.
 */
public interface IConfig {

  /**
   * Deployed light ids.
   */
  interface Lights {
    Integer[] Deploy = new Integer[]{1, 2, 3, 5};
  }

  /**
   * Screen capture command configuration.
   * This one works on mac os.
   */
  interface ScreenCapture {
    String Command = "/usr/sbin/screencapture";
    String OutputTypeOption = "-t";
    String OutputType = "jpg";
    String OutputFileOption = "-x";
    String OutputFileBasename = "captured.jpg";
    long Timeout = 5_000;
  }

  interface MainThread {
    long SleepBetweenCaptureTime = 1_000;
  }

  /**
   * Image analyzer module.
   */
  interface ImageAnalyzer {

    /**
     * Discard certain hue colors?
     */
    boolean ApplyThreshold = true;
    /**
     * Adjust overall brightness.
     */
    Float BrightnessMultiplication = 0.36f;
  }

  /**
   * Hue REST Client configuration.
   */
  interface HueClient {

    /**
     * jersey client debigging.
     */
    boolean DebugMessages = false;
  }

  /**
   * Configuration properties.
   */
  interface SystemProperties {
    String HUE_ACCESS_KEY="home.lights.accessKey";
    String BRIDGE_IP = "home.lights.bridge.ip";
  }

}
