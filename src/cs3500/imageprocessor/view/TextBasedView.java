package cs3500.imageprocessor.view;


import java.io.IOException;

import cs3500.imageprocessor.ProcessResult;
import cs3500.imageprocessor.image.AbstractViewImage;
import cs3500.imageprocessor.model.ImageProcessorViewModel;

/**
 * The view prints out the messages to user and display the images in a processor.
 * Since we don't need to actually display the image using a gui,
 * current it is only a test-based view, printing out some messages, the image's name,
 * width and height to an appendable.
 */
public class TextBasedView implements ImageProcessorView {

  private final Appendable destination;
  private final ImageProcessorViewModel model;

  /**
   * It constructs a text based view according to the given model and appendable output.
   * It uses a default output which is the system.out.
   *
   * @param model the model
   * @throws IllegalArgumentException if either model or appendable is null
   */
  public TextBasedView(ImageProcessorViewModel model) throws IllegalArgumentException {
    this(model, System.out);
  }

  /**
   * It constructs a text based view according to the given model and appendable output.
   *
   * @param model      the model
   * @param appendable the output source
   * @throws IllegalArgumentException if either model or appendable is null
   */
  public TextBasedView(ImageProcessorViewModel model, Appendable appendable)
      throws IllegalArgumentException {
    if (model == null || appendable == null) {
      throw new IllegalArgumentException("Model or Appendable is null");
    }
    this.destination = appendable;
    this.model = model;
  }


  /**
   * Render the message to the gui or console.
   *
   * @param message the message
   * @return a ProcessResult indicate the result of the rendering
   */
  @Override
  public ProcessResult renderMessage(String message) {
    try {
      destination.append(message + System.lineSeparator());
      return ProcessResult.SUCCESS;
    } catch (IOException e) {
      return ProcessResult.PRINT_MESSAGE_ERROR;
    }
  }

  /**
   * Render all images in the processor to the gui or console.
   *
   * @return a ProcessResult indicate the result of the rendering
   */
  @Override
  public ProcessResult renderAllImage() {
    this.renderMessage("Listing All Images: ");
    for (String key : model.getAllImages().keySet()) {
      this.renderImage(key);
    }
    return ProcessResult.SUCCESS;
  }

  /**
   * Find the image in the processor and render the image to the gui or console.
   *
   * @param filename the image name
   * @return a ProcessResult indicate the result of the rendering
   */
  @Override
  public ProcessResult renderImage(String filename) {
    AbstractViewImage image = this.model.getViewImage(filename);
    if (image == null) {
      return ProcessResult.IMAGE_NOT_FOUND;
    }
    return this.renderMessage(filename + ": Width " + image.getWidth() + " | Height "
        + image.getHeight() + " | File Type " + image.getImageType());
  }
}
