package net.mixaal.tools.homelights.client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.mixaal.tools.homelights.image.ImageProcessor;
import org.junit.Assert;
import org.junit.Test;

public class ImageProcessorTest {

  @Test
  public void processor1() throws IOException {
    final BufferedImage image = ImageIO.read(new File(this.getClass().getClassLoader().getResource("test.jpg").getFile()));
    int rgb [] = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
    final ImageProcessor im = new ImageProcessor(rgb, image.getWidth(), image.getHeight(), 3);
    float [] color1 = im.getDominantColor(image, true, 1);
    float [] color2 = im.getDominantColor(image, true, 2);
    float [] color3 = im.getDominantColor(image, true, 3);
    Assert.assertEquals(3, color1.length);
    Assert.assertEquals(3, color2.length);
    Assert.assertEquals(3, color3.length);

    Assert.assertArrayEquals(new float[] {0, 0, 0}, color1, 0.01f);
    Assert.assertArrayEquals(new float[] {0.724931f, 0.9867932f, 0.99887437f}, color2, 0.01f);
    Assert.assertArrayEquals(new float[] {0.30476463f, 0.99f, 0.99f}, color3, 0.01f);
  }



}
