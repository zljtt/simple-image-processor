package cs3500.imageprocessor.controller;

import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import cs3500.imageprocessor.commands.BrightnessCommand;
import cs3500.imageprocessor.commands.ColorTransformCommand;
import cs3500.imageprocessor.commands.CopyCommand;
import cs3500.imageprocessor.commands.FilterCommand;
import cs3500.imageprocessor.commands.FlipCommand;
import cs3500.imageprocessor.commands.ImageProcessCommand;
import cs3500.imageprocessor.commands.LoadCommand;
import cs3500.imageprocessor.commands.SaveCommand;
import cs3500.imageprocessor.model.ImageProcessorModel;
import cs3500.imageprocessor.view.ImageProcessorView;

/**
 * The controller contain a model and a view.
 * It gives user a way to interact with the model,
 * and use the view to display the images in the model.
 * It is a text-based controller which read string inputs from readable and write string outputs.
 */
public class TextBasedController implements ImageProcessorController {

  private final Map<String, Function<String[], ImageProcessCommand>> commands;
  private final Readable input;
  private final ImageProcessorModel model;
  private final ImageProcessorView view;

  /**
   * The controller constructor that take in a model and view.
   * The input readable is in default the System.in.
   *
   * @param model the model
   * @param view  the view
   */
  public TextBasedController(ImageProcessorModel model, ImageProcessorView view) {
    this(model, view, new InputStreamReader(System.in));
  }

  /**
   * The controller constructor that take in a model and view and a readable.
   * Init all commands.
   *
   * @param model the model
   * @param view  the view
   * @param input readable input
   * @throws IllegalArgumentException when either argument is null
   */
  public TextBasedController(ImageProcessorModel model, ImageProcessorView view,
                             Readable input) throws IllegalArgumentException {
    if (model == null || view == null | input == null) {
      throw new IllegalArgumentException("Invalid model, view or input");
    }
    this.model = model;
    this.view = view;
    this.input = input;
    commands = new HashMap<>();
    commands.put("save", SaveCommand::new);
    commands.put("load", LoadCommand::new);
    commands.put("copy", CopyCommand::new);
    commands.put("flip", FlipCommand::new);
    commands.put("brighten", BrightnessCommand::new);
    commands.put("filter", FilterCommand::new);
    commands.put("color-transform", ColorTransformCommand::new);
  }

  /**
   * Start running the controller with the given inputs.
   *
   * @param loc the given command line inputs
   */
  @Override
  public void runApp(String... loc) {
    if (loc.length == 0) {
      this.runApp();
    }
    for (String command : loc) {
      String[] context = Arrays.stream(command.split(" "))
          .filter((s) -> !s.isEmpty()).toArray(String[]::new);
      if (context.length > 0) {
        // get command
        String header = context[0];
        this.view.renderMessage(this.model.accept(commands.get(header)
            .apply(Arrays.copyOfRange(context, 1, context.length))).getOutputString());
      }
    }
  }

  /**
   * Start running the text-based controller.
   */
  public void runApp() {
    Scanner scanner = new Scanner(this.input);
    while (true) {
      this.view.renderMessage("Please enter command:");
      if (!scanner.hasNextLine()) {
        continue;
      }
      String[] context = Arrays.stream(scanner.nextLine().split(" "))
          .filter((s) -> !s.isEmpty()).toArray(String[]::new);
      if (context.length < 1) {
        continue;
      }
      if (context[0].equalsIgnoreCase("q") || context[0].equalsIgnoreCase("quit")) {
        return;
      }
      // get command
      String header = context[0];
      if (commands.containsKey(header)) {
        this.view.renderMessage(this.model.accept(commands.get(header)
            .apply(Arrays.copyOfRange(context, 1, context.length))).getOutputString());
      } else {
        this.view.renderMessage("Unknown command head.");
      }
    }
  }
}
