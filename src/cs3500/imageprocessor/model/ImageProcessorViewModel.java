package cs3500.imageprocessor.model;

import java.util.Map;

import cs3500.imageprocessor.image.AbstractViewImage;

/**
 * The view model of the image processor, provide only readable methods that do not modify
 * original images.
 */
public interface ImageProcessorViewModel {


  /**
   * Get all images stored in the processor.
   *
   * @return an array of image
   */
  Map<String, AbstractViewImage> getAllImages();

  /**
   * Get the readable images.
   *
   * @param name name of the image
   * @return an AbstractViewImage
   */
  AbstractViewImage getViewImage(String name);

  /**
   * Return the name of this processor.
   *
   * @return the processor name
   */
  String getProcessorName();

}
