package cs3500.imageprocessor.commands;

import cs3500.imageprocessor.ProcessResult;
import cs3500.imageprocessor.model.ImageProcessorModel;

/**
 * The command interface with a method process that takes either model or view,
 * used by the controller to access different operation easily.
 */
public interface ImageProcessCommand {
  /**
   * Process the according command, according to the given context that inits the command.
   *
   * @param model the mode to process the command
   * @return the result of the process.
   */
  ProcessResult process(ImageProcessorModel model);
}
