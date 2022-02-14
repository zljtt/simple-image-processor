package cs3500.imageprocessor.image;

import java.awt.image.BufferedImage;

/**
 * It indicates a type of image, that is used in the data storage in the processor.
 */
public interface AbstractImage extends AbstractViewImage {

  /**
   * Add the rbg into the given location of pixel in the image.
   *
   * @param x x position
   * @param y y position
   * @param r red value
   * @param g green value
   * @param b blue value
   * @param a alpha value
   */
  void setRBG(int x, int y, int r, int g, int b, int a);
  
  /**
   * Get the data of this image.
   *
   * @return the image data
   */
  BufferedImage getImageData();
}
