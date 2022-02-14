package cs3500.imageprocessor.commands;

import cs3500.imageprocessor.ProcessResult;
import cs3500.imageprocessor.image.AbstractImage;
import cs3500.imageprocessor.model.ImageProcessorModel;
import cs3500.imageprocessor.view.ImageProcessorGuiView;

/**
 * The command that apply a matrix filter to the image.
 */
public class FilterCommand implements ImageProcessCommand {

  private final String[] context;

  /**
   * Construct a command using the context.
   *
   * @param context the additional information for the command, in other words the arguments.
   *                Take in the increment of brightness, the original image, the destination image.
   */
  public FilterCommand(String... context) {
    this.context = context;
  }


  /**
   * Construct a command using the view.
   *
   * @param view the view to get the additional context.
   */
  public FilterCommand(ImageProcessorGuiView view) {
    this.context = new String[2];
  }

  /**
   * Process the according command, according to the given context of the command.
   *
   * @param model the model to process the command
   * @return the result of the process.
   */
  @Override
  public ProcessResult process(ImageProcessorModel model) {
    if (context.length < 3) {
      return ProcessResult.NOT_ENOUGH_COMMAND_ELEMENT;
    }
    FilterOperation operation = FilterOperation.getOperation(context[0]);
    if (operation == null) {
      return ProcessResult.INCORRECT_COMMAND_FORMAT;
    }
    AbstractImage image = model.getImage(context[1]);
    if (image == null) {
      return ProcessResult.IMAGE_NOT_FOUND;
    }
    AbstractImage clone = image.cloneImage();
    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        int[] filtered = this.applyMatrix(image, x, y, operation.getMatrix());
        clone.setRBG(x, y,
            Math.max(Math.min(filtered[0], image.getMaxValue()), 0),
            Math.max(Math.min(filtered[1], image.getMaxValue()), 0),
            Math.max(Math.min(filtered[2], image.getMaxValue()), 0),
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

  private int[] applyMatrix(AbstractImage image, int x, int y, float[][] matrix) {
    float filteredRed = 0;
    float filteredGreen = 0;
    float filteredBlue = 0;
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix.length; j++) {
        int tempX = x - matrix.length / 2 + i;
        int tempY = y - matrix.length / 2 + j;
        if (tempX >= 0 && tempX < image.getWidth() &&
            tempY >= 0 && tempY < image.getHeight()) {
          int red = image.getRed(tempX, tempY);
          int green = image.getGreen(tempX, tempY);
          int blue = image.getBlue(tempX, tempY);
          filteredRed += red * matrix[i][j];
          filteredGreen += green * matrix[i][j];
          filteredBlue += blue * matrix[i][j];
        }
      }
    }
    return new int[]{(int) filteredRed, (int) filteredGreen, (int) filteredBlue};
  }

  /**
   * The different type of filter operations, with a given string input.
   */
  public enum FilterOperation {
    BLUR("blur", new float[][]{
        {0.0625f, 0.125f, 0.0625f},
        {0.125f, 0.25f, 0.125f},
        {0.0625f, 0.125f, 0.0625f}
    }),
    SHARPEN("sharpen", new float[][]{
        {-0.125f, -0.125f, -0.125f, -0.125f, -0.125f},
        {-0.125f, 0.25f, 0.25f, 0.25f, -0.125f},
        {-0.125f, 0.25f, 1, 0.25f, -0.125f},
        {-0.125f, 0.25f, 0.25f, 0.25f, -0.125f},
        {-0.125f, -0.125f, -0.125f, -0.125f, -0.125f}
    });

    // the command line input to find the operation.
    private final String command;
    // the applying matrix
    float[][] matrix;

    FilterOperation(String command, float[][] matrix) {
      this.matrix = matrix;
      this.command = command;
    }

    /**
     * Get the string that this operation appears in the command.
     *
     * @return a string
     */
    public String getCommand() {
      return command;
    }

    /**
     * Get the matrix for calculate filtering.
     *
     * @return the matrix
     */
    public float[][] getMatrix() {
      return matrix;
    }

    /**
     * Find the operation type base on the command.
     *
     * @param command the command string
     * @return a GreyscaleOperation according to the command.
     */
    public static FilterOperation getOperation(String command) {
      for (FilterOperation op : values()) {
        if (op.getCommand().equals(command)) {
          return op;
        }
      }
      return null;
    }
  }
}
