package cs3500.imageprocessor.model;

import java.util.Map;

import cs3500.imageprocessor.ProcessResult;
import cs3500.imageprocessor.commands.ImageProcessCommand;
import cs3500.imageprocessor.image.AbstractImage;
import cs3500.imageprocessor.image.AbstractViewImage;

/**
 * The model controls the logic and functions of the image processor. It provides different ways to
 * * process the image, including saving, loading and modifying images. It also contains a list of
 * images uploaded by the user in the buffer, waiting to be saved into file system.
 * All function that modify images return a ProcessResult enum,
 * which indicate the result of running the function with the given arguments.
 */
public interface ImageProcessorModel extends ImageProcessorViewModel {

  /**
   * Get all images stored in the processor.
   *
   * @return a set of image
   */
  Map<String, AbstractImage> getImageBuffer();

  /**
   * Get the image from all images stored processor.
   *
   * @param image the name of the image.
   * @return the image
   */
  AbstractImage getImage(String image);

  /**
   * Get the readable images.
   *
   * @param name name of the image
   * @return an AbstractViewImage
   */
  @Override
  default AbstractViewImage getViewImage(String name) {
    return this.getImage(name);
  }

  /**
   * Accept the given command, and delegate to process the command.
   *
   * @param command command
   * @return the result of running the command
   */
  ProcessResult accept(ImageProcessCommand command);
}
