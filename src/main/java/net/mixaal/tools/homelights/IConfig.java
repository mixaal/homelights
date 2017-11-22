package net.mixaal.tools.homelights;

public interface IConfig {

  interface Lights {
    Integer[] Deploy = new Integer[]{1, 2, 3, 5};
  }

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

  interface ImageAnalyzer {
    boolean ApplyThreshold = true;
    Float BrightnessMultiplication = 0.36f;
  }

  interface HueClient {
    boolean DebugMessages = false;
  }

  interface SystemProperties {
    String HUE_ACCESS_KEY="home.lights.accessKey";
    String BRIDGE_IP = "home.lights.bridge.ip";
  }

}
