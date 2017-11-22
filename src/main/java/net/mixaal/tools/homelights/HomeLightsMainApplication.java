package net.mixaal.tools.homelights;

import com.google.common.io.Files;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
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

public class HomeLightsMainApplication {

  private static final String ACCESS_KEY = CoreUtils.getAccessKey();
  private static final URI hueService = UriBuilder.fromUri(CoreUtils.getServiceLocation()).build();
  private static final IHueClient client = new HueClient(hueService, ACCESS_KEY);

  private void deployColorToLight(Integer lightNo, float [] hsv) {
    if(hsv[2] == 0.0f) {
      client.lightOff(lightNo);
      return;
    }
    final int hue = (int)(65535 * hsv[0]);
    final int saturation = (int)(255 * hsv[1]);
    //final int brightness = (int)(ImageAnalyzer.BrightnessMultiplication * 255 * hsv[2]);
    Color c = new Color(Color.HSBtoRGB(hsv[0], hsv[1], hsv[2]));
    final int brightness = (int)(ImageAnalyzer.BrightnessMultiplication * ImageProcessor.Brightness(c));
    client.lightOn(lightNo, hue, saturation, brightness);
  }

  public void run(File screenshot) throws IOException {

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

  public static void main(String args [] ) {
    final HomeLightsMainApplication application = new HomeLightsMainApplication();
    final File tempDir = Files.createTempDir();
    System.out.println("tempDir="+tempDir.getAbsolutePath());
    while (true) {
      try {
        final long start = System.currentTimeMillis();
        final File screenshot = ScreenCaptureUtils.captureScreen(tempDir);
        final long screenEnd = System.currentTimeMillis();
        application.run(screenshot);
        final long now = System.currentTimeMillis();
        final long duration = now - start;
        if(duration < MainThread.SleepBetweenCaptureTime) {
          CoreUtils.sleep(MainThread.SleepBetweenCaptureTime - duration);
          //System.out.println("Duration : "+duration+"ms.");
        }
        else {
          System.out.println("Screenshot duration: "+(screenEnd - start)+"ms.");
          System.out.println("Can't make the: "+MainThread.SleepBetweenCaptureTime+"ms capture interval period! Duration : "+duration+"ms.");
        }
      }
      catch (Throwable t) {
        t.printStackTrace();
      }
    }
  }

}
