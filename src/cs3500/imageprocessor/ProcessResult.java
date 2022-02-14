package cs3500.imageprocessor;

/**
 * Indicates the result of the image processing.
 * All ProcessResult contains a message, which can be easily accessed by the controller
 * and be printed out.
 */
public enum ProcessResult {

  SUCCESS("Process succeeds."),
  FILE_NOT_FOUND("File not found in the given location."),
  UNSUPPORTED_FILE_FORMAT("The given file format is not supported."),
  WRITE_FILE_ERROR("Error writing file into the location."),
  IMAGE_NOT_FOUND("Image not found in the processor."),
  OVERRIDE_IMAGE("Process succeeds, the image has been overridden by the new one."),
  INCORRECT_COMMAND_FORMAT("Unsupported command format."),
  PRINT_MESSAGE_ERROR("Unable to print message."),
  NOT_ENOUGH_COMMAND_ELEMENT("Not enough entries in the command.");

  private String out;

  ProcessResult(String out) {
    this.out = out;
  }

  /**
   * Get the output message when of this result.
   *
   * @return the output message
   */
  public String getOutputString() {
    return out;
  }
}
