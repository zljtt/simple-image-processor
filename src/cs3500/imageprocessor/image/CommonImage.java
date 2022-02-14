package cs3500.imageprocessor.image;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Objects;

/**
 * It indicates a type of image data type used in the data storage in the processor.
 */
public class CommonImage implements AbstractImage {

  private final int maxValue;
  private final BufferedImage imageData;
  private final String fileType;

  /**
   * Constructs a blank common image of one of the predefined
   * image types. The rgb need to be added later.
   * The {@code ColorSpace} for the image is the
   * default sRGB space.
   *
   * @param width         width of the created image
   * @param height        height of the created image
   * @param maxValue      max value of the created image
   * @param imageDataType the format type of this image
   * @param fileType      the file type of this image, such as ppm, jpg
   */
  public CommonImage(int width, int height, int maxValue, int imageDataType, String fileType) {
    this.imageData = new BufferedImage(width, height, imageDataType);
    this.fileType = fileType;
    this.maxValue = maxValue;
  }

  /**
   * Constructs a common image of one of the predefined
   * image types by using a buffered image
   * The {@code ColorSpace} for the image is the
   * default sRGB space.
   *
   * @param image    height of the created image
   * @param maxValue max value of the created image
   * @param fileType the file type of this image, such as ppm, jpg
   */
  public CommonImage(BufferedImage image, int maxValue, String fileType) {
    this.imageData = image;
    this.fileType = fileType;
    this.maxValue = maxValue;
  }

  /**
   * Get the width of the image.
   *
   * @return the width
   */
  @Override
  public int getWidth() {
    return this.imageData.getWidth();
  }

  /**
   * Get the height of the image.
   *
   * @return the height
   */
  @Override
  public int getHeight() {
    return this.imageData.getHeight();
  }

  /**
   * Get the max value of the image.
   *
   * @return the max value
   */
  @Override
  public int getMaxValue() {
    return this.maxValue;
  }

  /**
   * Get the red value of the given pixel position.
   *
   * @param x x position
   * @param y y position
   * @return the red value
   */
  @Override
  public int getRed(int x, int y) {
    return this.imageData.getColorModel()
        .getRed(this.imageData.getWritableTile(x, y).getDataElements(x, y, null));
  }

  /**
   * Get the green value of the given pixel position.
   *
   * @param x x position
   * @param y y position
   * @return the green value
   */
  @Override
  public int getGreen(int x, int y) {
    return this.imageData.getColorModel()
        .getGreen(this.imageData.getWritableTile(x, y).getDataElements(x, y, null));
  }

  /**
   * Get the blue value of the given pixel position.
   *
   * @param x x position
   * @param y y position
   * @return the blue value
   */
  @Override
  public int getBlue(int x, int y) {
    return this.imageData.getColorModel()
        .getBlue(this.imageData.getRaster().getDataElements(x, y, null));
  }

  /**
   * Get the alpha value of the given pixel position.
   *
   * @param x x position
   * @param y y position
   * @return the alpha value
   */
  @Override
  public int getAlpha(int x, int y) {
    return this.imageData.getColorModel()
        .getAlpha(this.imageData.getRaster().getDataElements(x, y, null));
  }

  /**
   * Get the file type of this image.
   *
   * @return the file type
   */
  @Override
  public String getImageType() {
    return this.fileType;
  }

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
  @Override
  public void setRBG(int x, int y, int r, int g, int b, int a) {
    this.imageData.setRGB(x, y, (a << 24) | r << 16 | g << 8 | b);
  }

  /**
   * Get the data of this image.
   *
   * @return the image data
   */
  @Override
  public BufferedImage getImageData() {
    return this.imageData;
  }

  /**
   * Clone this image with another name.
   *
   * @return the cloned image
   */
  @Override
  public CommonImage cloneImage() {
    CommonImage clone = new CommonImage(this.getWidth(), this.getHeight(), this.getMaxValue(),
        this.imageData.getType(), this.getImageType());
    Graphics graphics = clone.imageData.createGraphics();
    graphics.drawImage(this.imageData, 0, 0, null);
    return clone;
  }

  /**
   * Two images are equals as they contain same data of the image.
   *
   * @param another another image to compare
   * @return whether they are the same
   */
  @Override
  public boolean equals(Object another) {
    if (!(another instanceof CommonImage)) {
      return false;
    }
    CommonImage image = (CommonImage) another;
    return this.getWidth() == image.getWidth() && this.getHeight() == image.getHeight()
        && this.getMaxValue() == image.getMaxValue()
        && this.getImageType().equals(image.getImageType())
        && Arrays.equals(this.imageData.getRGB(0, 0, this.getWidth(),
            this.getHeight(), null, 0, this.getWidth()),
        image.imageData.getRGB(0, 0, image.getWidth(),
            image.getHeight(), null, 0, image.getWidth()));
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(this.getWidth(), this.getHeight(), this.getMaxValue(),
        this.getImageType());
    return 31 * result + Arrays.hashCode(this.imageData.getRGB(0, 0, this.getWidth(),
        this.getHeight(), null, 0, this.getWidth()));
  }

  @Override
  public String toString() {
    return "CommonImage{" +
        "maxValue=" + maxValue +
        ", imageData=" + imageData +
        ", fileType='" + fileType + '\'' +
        '}';
  }
}
