package cs3500.imageprocessor.view;

import cs3500.imageprocessor.ProcessResult;
import cs3500.imageprocessor.commands.ImageDisplayCommand;

/**
 * The view prints out the messages to user and display the images in a processor.
 */
public interface ImageProcessorView {

  /**
   * Render the message to the gui or console.
   *
   * @param message the message
   * @return a ProcessResult indicate the result of the rendering
   */
  ProcessResult renderMessage(String message);

  /**
   * Find the image in the processor and render the image to the gui or console.
   *
   * @param filename the image name
   * @return a ProcessResult indicate the result of the rendering
   */
  ProcessResult renderImage(String filename);

  /**
   * Render all images in the processor to the gui or console.
   *
   * @return a ProcessResult indicate the result of the rendering
   */
  ProcessResult renderAllImage();
}
