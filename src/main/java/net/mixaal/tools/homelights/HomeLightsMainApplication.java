package net.mixaal.tools.homelights;

import com.google.common.io.Files;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.ws.rs.core.UriBuilder;
import net.mixaal.tools.homelights.IConfig.ImageAnalyzer;
import net.mixaal.tools.homelights.IConfig.Lights;
import net.mixaal.tools.homelights.IConfig.MainThread;
import net.mixaal.tools.homelights.client.HueClient;
import net.mixaal.tools.homelights.client.IHueClient;
import net.mixaal.tools.homelights.image.ImageProcessor;
import net.mixaal.tools.homelights.utils.CoreUtils;
import net.mixaal.tools.homelights.utils.ScreenCaptureUtils;

/**
 * Main Application.
 */
public class HomeLightsMainApplication {

  private static final String ACCESS_KEY = CoreUtils.getAccessKey();
  private static final URI hueService = UriBuilder.fromUri(CoreUtils.getServiceLocation()).build();
  private static final IHueClient client = new HueClient(hueService, ACCESS_KEY);

  /**
   * Before turning the light off, consult the {@link Lights} turn off interval.
   */
  private static long dimmLightTime[] = new long[Lights.Deploy.length];


  /**
   * Main body of the light control.
   * 1) Take the desktop screenshot and save it under temp directory
   * 2) Analyze the image and take the most relevant colors
   * 3) Deploy the colors to the configured lights
   *
   * @param tempDir temporary directory where the screenshot is saved
   * @throws IOException
   */
  public void run(File tempDir) throws IOException {
    final File screenshot = ScreenCaptureUtils.captureScreen(tempDir);
    final BufferedImage image = ImageIO.read(screenshot);
    final int w = image.getWidth();
    final int h = image.getHeight();
    int rgb[] = image.getRGB(0, 0, w, h, null, 0, w );
    final ImageProcessor imageProcessor = new ImageProcessor(
        rgb, w, h,
        Lights.Deploy.length
    );

    for(int partNo = 1; partNo<=Lights.Deploy.length; ++partNo) {
      float[] color = imageProcessor.getDominantColor(image, ImageAnalyzer.ApplyThreshold, partNo);
      deployColorToLight(Lights.Deploy[partNo-1], color);
    }

  }

  /**
   * Run the main  route {@link HomeLightsMainApplication#run(File)} forever.
   * Adjust the sleep time to achieve the periodic update.
   *
   * @param args command line arguments
   */
  public static void main(String args [] ) {
    CoreUtils.printConfiguration();
    final HomeLightsMainApplication application = new HomeLightsMainApplication();
    final File tempDir = Files.createTempDir();
    System.out.println("tempDir="+tempDir.getAbsolutePath());
    while (true) {
      try {
        final long start = System.currentTimeMillis();
        application.run(tempDir);
        final long now = System.currentTimeMillis();
        final long duration = now - start;
        if(duration < MainThread.SleepBetweenCaptureTime) {
          CoreUtils.sleep(MainThread.SleepBetweenCaptureTime - duration);
        }
        else {
          System.out.println("Can't make the: "+MainThread.SleepBetweenCaptureTime+"ms capture interval period! Duration : "+duration+"ms.");
        }
      }
      catch (Throwable t) {
        t.printStackTrace();
      }
    }
  }

  /**
   * Deploy the hsb color to light id using the {@link IHueClient#lightOn(Integer, Integer, Integer, Integer)}
   * method.
   *
   * @param lightNo light id
   * @param hsv color to deploy
   */
  private void deployColorToLight(Integer lightNo, float [] hsv) {
    int lightIdx = Arrays.binarySearch(Lights.Deploy, lightNo);
    if(hsv[2] == 0.0f) {
      final long now = System.currentTimeMillis();
      /**
       * In case this is the first time in a row we want to turn the light off
       * set the time of the first attempt.
       */
      if(dimmLightTime[lightIdx] == 0) {
        dimmLightTime[lightIdx] = System.currentTimeMillis();
      }
      final long duration = now - dimmLightTime[lightIdx];
      if(duration>Lights.TurnOffTimeout) {
        /**
         * Tuen the light off when we exceeded the timeout.
         */
        client.lightOff(lightNo);
      }
      return;
    }
    else {
      /**
       * Reset the timer.
       */
      dimmLightTime[lightIdx] = 0L;
    }
    final int hue = (int)(65535 * hsv[0]);
    final int saturation = (int)(255 * hsv[1]);
    final int brightness = (int)(ImageAnalyzer.BrightnessMultiplication * 255 * hsv[2]);
    client.lightOn(lightNo, hue, saturation, brightness);
  }

}
