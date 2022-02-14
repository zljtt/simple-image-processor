package cs3500.imageprocessor.model;


import java.util.HashMap;
import java.util.Map;

import cs3500.imageprocessor.commands.ImageProcessCommand;
import cs3500.imageprocessor.ProcessResult;
import cs3500.imageprocessor.image.AbstractImage;
import cs3500.imageprocessor.image.AbstractViewImage;


/**
 * The model controls the logic and functions of the image processor. It provides different ways to
 * process the image, including saving, loading and modifying images. It also contains a list of
 * images uploaded by the user in the buffer, waiting to be saved into file system.
 * All function that modify images return a ProcessResult enum, which indicate the result of running
 * the function with the given arguments.
 */
public class ImageProcessorModelImpl implements ImageProcessorModel {

  private final Map<String, AbstractImage> imageBuffer;

  /**
   * Construct the model and init the image list to hold all loaded images.
   */
  public ImageProcessorModelImpl() {
    imageBuffer = new HashMap<>();
  }

  /**
   * Accept the given command, and delegate to process the command.
   *
   * @param command command
   * @return the result of running the command
   */
  @Override
  public ProcessResult accept(ImageProcessCommand command) {
    return command.process(this);
  }

  /**
   * Get all images stored in the processor.
   *
   * @return a set of image
   */
  @Override
  public Map<String, AbstractImage> getImageBuffer() {
    return imageBuffer;
  }

  /**
   * Get the image from all images stored processor.
   *
   * @param image the name of the image.
   * @return the image
   */
  @Override
  public AbstractImage getImage(String image) {
    return this.imageBuffer.get(image);
  }

  /**
   * Get all images stored in the processor.
   *
   * @return an array of image
   */
  @Override
  public Map<String, AbstractViewImage> getAllImages() {
    Map<String, AbstractViewImage> viewImages = new HashMap<>();
    for (String image : this.imageBuffer.keySet()) {
      viewImages.put(image, this.imageBuffer.get(image));
    }
    return viewImages;
  }

  /**
   * Return the name of this processor.
   *
   * @return the processor name
   */
  @Override
  public String getProcessorName() {
    return "Image Processor";
  }


}
