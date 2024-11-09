package com.narlock.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

public class ImageUtils {

  public static BufferedImage readImage(String imagePath) {
    // return default image for null imagePath
    if (imagePath == null) {
      try {
        return ImageIO.read(
            Objects.requireNonNull(ImageUtils.class.getResource("/defaultImage.png")));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    // otherwise attempt to return
    try {
      return ImageIO.read(new File(imagePath)); // Load from absolute file path
    } catch (IOException e) {
      System.out.println("Image from " + imagePath + " was not found. Loading default image.");
      try {
        return ImageIO.read(
            Objects.requireNonNull(ImageUtils.class.getResourceAsStream("/defaultImage.png")));
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }

    System.err.println("Failed to load image. See stack trace.");
    throw new RuntimeException("Failed to load image from path: " + imagePath);
  }

  public static BufferedImage scaleImage(BufferedImage image, int widthHeight) {
    BufferedImage scaledImage = new BufferedImage(widthHeight, widthHeight, image.getType());
    Graphics2D g2 = scaledImage.createGraphics();
    g2.drawImage(image, 0, 0, widthHeight, widthHeight, null);
    g2.dispose();
    return scaledImage;
  }
}
