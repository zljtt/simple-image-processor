package cs3500.imageprocessor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import cs3500.imageprocessor.controller.ImageProcessorController;
import cs3500.imageprocessor.controller.SwingGuiController;
import cs3500.imageprocessor.controller.TextBasedController;
import cs3500.imageprocessor.model.ImageProcessorModel;
import cs3500.imageprocessor.model.ImageProcessorModelImpl;
import cs3500.imageprocessor.view.ImageProcessorGuiView;
import cs3500.imageprocessor.view.ImageProcessorView;
import cs3500.imageprocessor.view.SwingGuiView;
import cs3500.imageprocessor.view.TextBasedView;

/**
 * The processor Main, run this file to start and test a new PPM Image processor.
 */
public class ImageProcessor {

  /**
   * The main function for app run.
   *
   * @param args the pass in arguments
   */
  public static void main(String[] args) throws IOException {
    ImageProcessorModel model = new ImageProcessorModelImpl();
    if (args.length > 0) {
      ImageProcessorView view = new TextBasedView(model);
      ImageProcessorController controller = new TextBasedController(model, view);
      if (args[0].equals("-text") && args.length == 1) {
        controller.runApp();
      } else if (args[0].equals("-file") && args.length == 2) {
        Scanner file_sc = null;
        try {
          file_sc = new Scanner(new FileInputStream(args[1]));
        } catch (FileNotFoundException e) {
          view.renderMessage(ProcessResult.FILE_NOT_FOUND.getOutputString());
        }
        List<String> commands = new ArrayList<>();
        while (file_sc != null && file_sc.hasNextLine()) {
          String next = file_sc.nextLine();
          if (next.split(" ").length > 1) {
            commands.add(next);
          }
        }
        controller.runApp(commands.toArray(new String[0]));
      } else {
        view.renderMessage("Unknown command line input.");
      }
    } else {
      ImageProcessorGuiView view = new SwingGuiView(model);
      ImageProcessorController controller = new SwingGuiController(model, view);
      controller.runApp();
    }
  }
}
