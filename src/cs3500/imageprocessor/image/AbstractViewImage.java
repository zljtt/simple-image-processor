package cs3500.imageprocessor.image;

/**
 * The abstract image that only provides readable methods.
 * Disallow user to modify the image.
 */
public interface AbstractViewImage {

  /**
   * Get the width of the image.
   *
   * @return the width
   */
  int getWidth();

  /**
   * Get the height of the image.
   *
   * @return the height
   */
  int getHeight();

  /**
   * Get the max value of the image.
   *
   * @return the max value
   */
  int getMaxValue();

  /**
   * Get the red value of the given pixel position.
   *
   * @param x x position
   * @param y y position
   * @return the red value
   */
  int getRed(int x, int y);

  /**
   * Get the green value of the given pixel position.
   *
   * @param x x position
   * @param y y position
   * @return the green value
   */
  int getGreen(int x, int y);

  /**
   * Get the blue value of the given pixel position.
   *
   * @param x x position
   * @param y y position
   * @return the blue value
   */
  int getBlue(int x, int y);

  /**
   * Get the alpha value of the given pixel position.
   *
   * @param x x position
   * @param y y position
   * @return the alpha value
   */
  int getAlpha(int x, int y);


  /**
   * Get the file type of this image.
   *
   * @return the file type
   */
  String getImageType();


  /**
   * Clone this image.
   *
   * @param <T> the image type
   * @return the cloned image
   */
  <T extends AbstractImage> T cloneImage();

}
