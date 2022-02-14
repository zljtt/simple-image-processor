package cs3500.imageprocessor.commands;

import cs3500.imageprocessor.ProcessResult;
import cs3500.imageprocessor.image.AbstractImage;
import cs3500.imageprocessor.model.ImageProcessorModel;
import cs3500.imageprocessor.view.ImageProcessorGuiView;

/**
 * The command that modify the brightness of the image.
 */
public class BrightnessCommand implements ImageProcessCommand {

  private final String[] context;

  /**
   * Construct a command using the context.
   *
   * @param context the additional information for the command, in other words the arguments.
   *                Take in the increment of brightness, the original image, the destination image.
   */
  public BrightnessCommand(String... context) {
    this.context = context;
  }

  /**
   * Construct a command using the view.
   *
   * @param view the view to get the additional context.
   */
  public BrightnessCommand(ImageProcessorGuiView view) {
    this.context = new String[2];
  }

  /**
   * Process to modify the brightness, according to the given context of the command.
   *
   * @param model the model to process the command
   * @return the result of the process.
   */
  @Override
  public ProcessResult process(ImageProcessorModel model) {
    if (context.length < 3) {
      return ProcessResult.NOT_ENOUGH_COMMAND_ELEMENT;
    }
    int increment;
    try {
      increment = Integer.parseInt(context[0]);
    } catch (NumberFormatException e) {
      return ProcessResult.INCORRECT_COMMAND_FORMAT;
    }
    AbstractImage image = model.getImage(context[1]);
    if (image == null) {
      return ProcessResult.IMAGE_NOT_FOUND;
    }
    // create new image
    AbstractImage clone = image.cloneImage();
    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        clone.setRBG(x, y,
            Math.max(Math.min(image.getRed(x, y) + increment, image.getMaxValue()), 0),
            Math.max(Math.min(image.getGreen(x, y) + increment, image.getMaxValue()), 0),
            Math.max(Math.min(image.getBlue(x, y) + increment, image.getMaxValue()), 0),
            image.getAlpha(x, y));
      }
    }
    // attempt to override with the same image name
    if (model.getImage(context[2]) != null) {
      model.getImageBuffer().put(context[2], clone);
      return ProcessResult.OVERRIDE_IMAGE;
    }
    model.getImageBuffer().put(context[2], clone);
    return ProcessResult.SUCCESS;
  }
}
