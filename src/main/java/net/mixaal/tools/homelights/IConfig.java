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
    long TurnOffTimeout = 2_500;
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
     * Discard brightness below 0.35?
     */
    float BrightnessThreshold = 0.35f;

    /**
     * Discard de-saturated colors?
     */
    float SaturationThreshold = 0.35f;

    /**
     * Adjust overall brightness.
     */
   // Float BrightnessMultiplication = 0.36f;
    Float BrightnessMultiplication = 0.8f;

    /**
     * Adjust saturation of low-saturated images.
     */
    Float SaturationMultiplication = 100.0f;


    /**
     * Turn the light off if the average brightness is below this value
     */
    Float AverageBrightnessTresholdToTurnTheLightOff = 0.1f;
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
    String MOVIE_MODE = "home.lights.movie.mode";
    String CONF_PROPERTY = "home.lights.config.file";
  }

}
