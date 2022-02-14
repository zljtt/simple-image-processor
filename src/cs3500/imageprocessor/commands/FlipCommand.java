package cs3500.imageprocessor.commands;

import cs3500.imageprocessor.ProcessResult;
import cs3500.imageprocessor.image.AbstractImage;
import cs3500.imageprocessor.model.ImageProcessorModel;
import cs3500.imageprocessor.view.ImageProcessorGuiView;

/**
 * The command that modify the brightness of the image.
 */
public class FlipCommand implements ImageProcessCommand {
  private final String[] context;

  /**
   * Construct a command using the context.
   *
   * @param context the additional information for the command, in other words the arguments.
   *                Take in the increment of brightness, the original image, the destination image.
   */
  public FlipCommand(String... context) {
    this.context = context;
  }

  /**
   * Construct a command using the view.
   *
   * @param view the view to get the additional context.
   */
  public FlipCommand(ImageProcessorGuiView view) {
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
    AbstractImage image = model.getImage(context[1]);
    if (image == null) {
      return ProcessResult.IMAGE_NOT_FOUND;
    }
    // create new image
    AbstractImage clone = image.cloneImage();
    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        if (context[0].equals("horizontal")) {
          clone.setRBG(image.getWidth() - x - 1, y,
              image.getRed(x, y),
              image.getGreen(x, y),
              image.getBlue(x, y),
              image.getAlpha(x, y));
        } else if (context[0].equals("vertical")) {
          clone.setRBG(x, image.getHeight() - y - 1,
              image.getRed(x, y),
              image.getGreen(x, y),
              image.getBlue(x, y),
              image.getAlpha(x, y));
        } else {
          return ProcessResult.INCORRECT_COMMAND_FORMAT;
        }
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
