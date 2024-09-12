package com.narlock.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

public class ImageUtils {

  public static BufferedImage readImage(String imagePath) {
    try {
      return ImageIO.read(Objects.requireNonNull(ImageUtils.class.getResourceAsStream(imagePath)));
    } catch (IOException | NullPointerException e) {
      try {
        System.out.println("Image from " + imagePath + " was not found. Loading default image.");
        return ImageIO.read(
            Objects.requireNonNull(ImageUtils.class.getResource("/defaultImage.png")));
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
