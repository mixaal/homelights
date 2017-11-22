package net.mixaal.tools.homelights.client;

import com.google.common.io.Files;
import java.io.File;
import net.mixaal.tools.homelights.utils.ScreenCaptureUtils;
import org.junit.Assert;
import org.junit.Test;

public class CaptureTest {

  @Test
  public void screenCapture() {
    File f = ScreenCaptureUtils.captureScreen(Files.createTempDir());
    Assert.assertTrue(f.exists());
  }

}
