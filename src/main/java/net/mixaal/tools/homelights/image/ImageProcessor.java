package net.mixaal.tools.homelights.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import net.mixaal.tools.homelights.IConfig;
import net.mixaal.tools.homelights.IConfig.ImageAnalyzer;
import net.mixaal.tools.homelights.utils.CoreUtils;

/**
 * Basic image processor. Based on the following source code:
 *
 * https://github.com/carstena/game-to-philips-hue/blob/master/src/com/philips/lighting/ImageProcessor.java
 *
 * Changelog:
 *   - fixed hue/color bin computation
 *   - brightness calculation based on average overall image area brightness
 *   - bw movies brightness correctly calculated (light not turned off)
 */
public class ImageProcessor {

  /**
   * Number of slices in the image.
   */
  private final int totalParts;
  private final int rgb[];
  private final int width, height;

  public ImageProcessor(
      final int rgb[],
      final int w,
      final int h,
      final int totalParts
  ) {
    this.rgb = rgb;
    this.width = w;
    this.height = h;
    this.totalParts = totalParts;
  }

  public float[] getDominantColor(BufferedImage image,
      boolean applyThreshold, int part) {

    // Keep track of how many times a hue in a given bin appears
    // in the image.
    // Hue values range [0 .. 360), so dividing by 10, we get 36
    // bins.
    int[] colorBins = new int[36];

    // The bin with the most colors. Initialize to -1 to prevent
    // accidentally
    // thinking the first bin holds the dominant color.
    int maxBin = -1;

    // Keep track of sum hue/saturation/value per hue bin, which
    // we'll use to
    // compute an average to for the dominant color.
    float[] sumHue = new float[36];
    float[] sumSat = new float[36];
    float[] sumVal = new float[36];
    float[] hsv = new float[3];
    float[] sumBrightness = new float[36];

    for (int i = 0; i < sumHue.length; ++i) {
      sumHue[i] = sumSat[i] = sumVal[i] = sumBrightness[i] = 0.0f;
    }

    int part_width = (int) Math.floor(width / totalParts);
    int col_nr = part_width * part - part_width;

    long allSamples = 0;
    float overallBrightness = 0.0f;
    for (int row = 0; row < height; row++) {
      for (int col = col_nr; col < (part_width * part); col++) {
        ++allSamples;

        Color c = new Color(rgb[row * width + col]);

        /**
         * Compute overall Brightness.
         */
        float I = Brightness(c) / 255.0f;
        overallBrightness += I;
        //System.out.println("c=" + c + "I=" + I + "  35*I=" + (35 * I));
        sumBrightness[(int) (35 * I)]++;
        hsv = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(),
            null);

        /**
         * By-default discard low-saturated or low-brightness pixels
         * for hue computation.
         */
        if (applyThreshold && (hsv[1] <= ImageAnalyzer.SaturationThreshold
            || hsv[2] <= ImageAnalyzer.BrightnessThreshold)) {
          continue;
        }

        // We compute the dominant color by putting colors
        // in bins based on their hue.
        int bin = (int) Math.floor(hsv[0] * 35.0f);

        // Update the sum hue/saturation/value for this bin.
        sumHue[bin] = sumHue[bin] + hsv[0];
        sumSat[bin] = sumSat[bin] + hsv[1];
        sumVal[bin] = sumVal[bin] + hsv[2];

        // Increment the number of colors in this bin.
        colorBins[bin]++;

        // Keep track of the bin that holds the most colors.
        if (maxBin < 0 || colorBins[bin] > colorBins[maxBin]) {
          maxBin = bin;
        }
      }
    }

    float averageBrightness = overallBrightness / allSamples;
    // maxBin may never get updated if the image holds only transparent
    // and/or black/white pixels.
    if (maxBin < 0) {
      return new float[]{0.0f, 0.0f, averageBrightness};
    }

    // Return a color with the average hue/saturation/value of
    // the bin with the most colors.
    hsv[0] = sumHue[maxBin] / colorBins[maxBin];
    hsv[1] = sumSat[maxBin] / colorBins[maxBin];
    hsv[2] = averageBrightness;

    /**
     * If the part of the image is too dark turn the light off.
     */
    if (hsv[2] < ImageAnalyzer.AverageBrightnessTresholdToTurnTheLightOff) {
      hsv[0] = hsv[1] = hsv[2] = 0.0f;
    }

    return hsv;
  }

  /**
   * See http://alienryderflex.com/hsp.html
   */
  public static int Brightness(Color c) {
    return (int) Math.sqrt(c.getRed() * c.getRed() * .241 + c.getGreen()
        * c.getGreen() * .691 + c.getBlue() * c.getBlue() * .068);
  }

}
