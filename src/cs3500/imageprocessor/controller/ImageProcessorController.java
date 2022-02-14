package cs3500.imageprocessor.controller;

/**
 * The controller contain a model and a view.
 * It gives user a way to interact with the model,
 * and use the view to display the images in the model.
 */
public interface ImageProcessorController {

  /**
   * Start running the controller with the given inputs.
   *
   * @param commands the given command line inputs
   */
  void runApp(String... commands);
}
