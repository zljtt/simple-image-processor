package cs3500.imageprocessor.commands;

import cs3500.imageprocessor.ProcessResult;
import cs3500.imageprocessor.model.ImageProcessorModel;
import cs3500.imageprocessor.view.ImageProcessorGuiView;

public interface ImageDisplayCommand extends ImageProcessCommand {
  /**
   * Process the according command, according to the given context that inits the command.
   *
   * @param model the mode to process the command
   * @return the result of the process.
   */
  ProcessResult processWithGui(ImageProcessorModel model, ImageProcessorGuiView view);

}
