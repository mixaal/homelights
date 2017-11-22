package net.mixaal.tools.homelights.utils;

import java.io.File;
import java.util.concurrent.TimeUnit;
import net.mixaal.tools.homelights.IConfig.ScreenCapture;

/**
 * Screen capture utility.
 */
public class ScreenCaptureUtils {

  public static File captureScreen(File tempDir) {
    try {
      final File outFile = new File(tempDir, ScreenCapture.OutputFileBasename);
      final ProcessBuilder pb = new ProcessBuilder(
          ScreenCapture.Command,
          ScreenCapture.OutputTypeOption,
          ScreenCapture.OutputType,
          ScreenCapture.OutputFileOption,
          outFile.getAbsolutePath()
      );
      final Process p = pb.start();
      p.waitFor(ScreenCapture.Timeout, TimeUnit.MILLISECONDS);
      return outFile;
    }
    catch (Exception e) {e.printStackTrace();}
    return null;
  }

}
