package cs3500.imageprocessor.commands;

import cs3500.imageprocessor.ProcessResult;
import cs3500.imageprocessor.image.AbstractImage;
import cs3500.imageprocessor.model.ImageProcessorModel;
import cs3500.imageprocessor.view.ImageProcessorGuiView;

/**
 * The command that apply a matrix filter to the image.
 */
public class ColorTransformCommand implements ImageProcessCommand {

  private final String[] context;

  /**
   * Construct a command using the context.
   *
   * @param context the additional information for the command, in other words the arguments.
   *                Take in the increment of brightness, the original image, the destination image.
   */
  public ColorTransformCommand(String... context) {
    this.context = context;
  }

  /**
   * Construct a command using the view.
   *
   * @param view the view to get the additional context.
   */
  public ColorTransformCommand(ImageProcessorGuiView view) {
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
    AbstractImage image = model.getImage(context[1]);
    if (image == null) {
      return ProcessResult.IMAGE_NOT_FOUND;
    }
    AbstractImage clone = image.cloneImage();
    ColorTransformOperation operation = ColorTransformOperation.getOperation(context[0]);
    if (operation == null) {
      return ProcessResult.INCORRECT_COMMAND_FORMAT;
    }
    switch (operation) {
      case VALUE:
        for (int y = 0; y < image.getHeight(); y++) {
          for (int x = 0; x < image.getWidth(); x++) {
            int max = Math.max(image.getRed(x, y),
                Math.max(image.getGreen(x, y), image.getBlue(x, y)));
            clone.setRBG(x, y, max, max, max, image.getAlpha(x, y));
          }
        }
        break;
      case INTENSITY:
        for (int y = 0; y < image.getHeight(); y++) {
          for (int x = 0; x < image.getWidth(); x++) {
            int avg = (int) Math.floor((double) (image.getRed(x, y) + image.getGreen(x, y)
                + image.getBlue(x, y)) / 3);
            clone.setRBG(x, y, avg, avg, avg, image.getAlpha(x, y));
          }
        }
        break;
      case RED:
      case GREEN:
      case BLUE:
      case LUMA:
      case SEPIA:
        for (int y = 0; y < image.getHeight(); y++) {
          for (int x = 0; x < image.getWidth(); x++) {
            double[] filtered = this.applyMatrix(new int[]{image.getRed(x, y),
                image.getGreen(x, y), image.getBlue(x, y)}, operation.getMatrix());
            clone.setRBG(x, y,
                (int) Math.max(Math.min(filtered[0], image.getMaxValue()), 0),
                (int) Math.max(Math.min(filtered[1], image.getMaxValue()), 0),
                (int) Math.max(Math.min(filtered[2], image.getMaxValue()), 0),
                image.getAlpha(x, y));
          }
        }
        break;
      default:
        return ProcessResult.INCORRECT_COMMAND_FORMAT;
    }

    // attempt to override with the same image name
    if (model.getImage(context[2]) != null) {
      model.getImageBuffer().put(context[2], clone);
      return ProcessResult.OVERRIDE_IMAGE;
    }
    model.getImageBuffer().

        put(context[2], clone);
    return ProcessResult.SUCCESS;
  }

  private double[] applyMatrix(int[] rgb, double[][] matrix) {
    double[] filtered = new double[3];
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        filtered[i] += rgb[j] * matrix[i][j];
      }
    }
    /*
        filtered[0] = (int) (rgb[0] * matrix[0][0] + rgb[1] * matrix[0][1] + rgb[2] * matrix[0][2]);
    filtered[1] = (int) (rgb[0] * matrix[1][0] + rgb[1] * matrix[1][1] + rgb[2] * matrix[1][2]);
    filtered[2] = (int) (rgb[0] * matrix[2][0] + rgb[1] * matrix[2][1] + rgb[2] * matrix[2][2]);
     */
    return filtered;
  }


  /**
   * The different type of filter operations, with a given string input.
   */
  public enum ColorTransformOperation {
    RED("red", new double[][]{
        {1, 0, 0},
        {1, 0, 0},
        {1, 0, 0}
    }),
    GREEN("green", new double[][]{
        {0, 1, 0},
        {0, 1, 0},
        {0, 1, 0}
    }),
    BLUE("blue", new double[][]{
        {0, 0, 1},
        {0, 0, 1},
        {0, 0, 1}
    }),
    VALUE("value"),
    INTENSITY("intensity"),
    LUMA("luma", new double[][]{
        {0.2126, 0.7152, 0.0722},
        {0.2126, 0.7152, 0.0722},
        {0.2126, 0.7152, 0.0722}
    }),
    SEPIA("sepia", new double[][]{
        {0.393, 0.769, 0.189},
        {0.349, 0.686, 0.168},
        {0.272, 0.534, 0.131}
    });

    // the command line input to find the operation.
    private final String command;
    // the applying matrix
    double[][] matrix;

    ColorTransformOperation(String command, double[][] matrix) {
      this.matrix = matrix;
      this.command = command;
    }

    ColorTransformOperation(String command) {
      this.matrix = null;
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
    public double[][] getMatrix() {
      return matrix;
    }

    /**
     * Find the operation type base on the command.
     *
     * @param command the command string
     * @return a GreyscaleOperation according to the command.
     */
    public static ColorTransformOperation getOperation(String command) {
      for (ColorTransformOperation op : values()) {
        if (op.getCommand().equals(command)) {
          return op;
        }
      }
      return null;
    }
  }
}
