package cs3500.imageprocessor.commands;

import cs3500.imageprocessor.ProcessResult;
import cs3500.imageprocessor.image.AbstractImage;
import cs3500.imageprocessor.model.ImageProcessorModel;
import cs3500.imageprocessor.view.ImageProcessorGuiView;

/**
 * The command that modify the brightness of the image.
 */
public class CopyCommand implements ImageProcessCommand {

  private final String[] context;

  /**
   * Construct a command using the context.
   *
   * @param context the additional information for the command, in other words the arguments.
   *                Take in the increment of brightness, the original image, the destination image.
   */
  public CopyCommand(String... context) {
    this.context = context;
  }

  /**
   * Construct a command using the view.
   *
   * @param view the view to get the additional context.
   */
  public CopyCommand(ImageProcessorGuiView view) {
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
    if (context.length < 2) {
      return ProcessResult.NOT_ENOUGH_COMMAND_ELEMENT;
    }
    AbstractImage image = model.getImage(context[0]);
    if (image == null) {
      return ProcessResult.IMAGE_NOT_FOUND;
    }
    // create new image
    AbstractImage clone = image.cloneImage();
    // attempt to override with the same image name
    if (model.getImage(context[1]) != null) {
      model.getImageBuffer().put(context[1], clone);
      return ProcessResult.OVERRIDE_IMAGE;
    }
    model.getImageBuffer().put(context[1], clone);
    return ProcessResult.SUCCESS;
  }
}
