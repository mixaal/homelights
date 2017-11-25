package net.mixaal.tools.homelights;

import com.google.common.io.Files;
import java.io.File;
import net.mixaal.tools.homelights.IConfig.MainThread;
import net.mixaal.tools.homelights.utils.CoreUtils;

/**
 * Main Application.
 */
public class HomeLightsMainApplication {

  private final HueLightsController lightsController = new HueLightsController();


  private void run() {
    final File tempDir = Files.createTempDir();
    System.out.println("  Screenshots : "+tempDir.getAbsolutePath());
    while (true) {
      try {
        final long start = System.currentTimeMillis();
        lightsController.handleLightControl(tempDir);
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
   * Run the main light controller in infinite loop.
   * Adjust the sleep time to achieve the periodic update.
   *
   * @param args command line arguments
   */
  public static void main(String args [] ) {
    CoreUtils.printConfiguration();
    final HomeLightsMainApplication application = new HomeLightsMainApplication();
    application.run();
  }



}
