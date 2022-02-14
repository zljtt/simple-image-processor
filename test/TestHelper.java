import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.Scanner;

import cs3500.imageprocessor.controller.TextBasedController;
import cs3500.imageprocessor.model.ImageProcessorModel;
import cs3500.imageprocessor.view.ImageProcessorView;
import cs3500.imageprocessor.view.TextBasedView;

/**
 * Helper functions for testing.
 */
public class TestHelper {

  /**
   * Whether the two files in the given locations are same image.
   *
   * @param location1 the location of first ppm image
   * @param location2 the location of second ppm image
   * @return a boolean indicating whether they are the same
   */
  public static boolean samePPM(String location1, String location2) {
    Scanner file_sc1;
    Scanner file_sc2;
    try {
      file_sc1 = new Scanner(new FileInputStream(location1));
      file_sc2 = new Scanner(new FileInputStream(location2));
    } catch (FileNotFoundException e) {
      return false;
    }
    StringBuilder builder1 = new StringBuilder();
    StringBuilder builder2 = new StringBuilder();
    //read the file line by line, and populate a string. This will throw away any comment lines
    while (file_sc1.hasNextLine()) {
      String s = file_sc1.nextLine();
      if (s.charAt(0) != '#') {
        builder1.append(s).append(System.lineSeparator());
      }
    }
    while (file_sc2.hasNextLine()) {
      String s = file_sc2.nextLine();
      if (s.charAt(0) != '#') {
        builder2.append(s).append(System.lineSeparator());
      }
    }
    return builder1.toString().equals(builder2.toString());
  }

  /**
   * Assess whether the running the given command in the controller will give the success output
   * in the given string builder.
   *
   * @param model   the given model
   * @param command the command line
   * @return whether the command succeeds
   */
  public static boolean runCommandSuccess(ImageProcessorModel model, String command) {
    StringBuilder output_source = new StringBuilder();
    ImageProcessorView view = new TextBasedView(model, output_source);
    new TextBasedController(model, view, new StringReader(command)).runApp();
    return output_source.toString().equals("Please enter command:\r\n"
        + "Process succeeds.\r\n"
        + "Please enter command:\r\n");
  }


  /**
   * Return the result of running the given command in the controller.
   *
   * @param model   the given model
   * @param command the command line
   * @return whether the command output
   */
  public static String runCommand(ImageProcessorModel model, String command) {
    StringBuilder output_source = new StringBuilder();
    ImageProcessorView view = new TextBasedView(model, output_source);
    new TextBasedController(model, view, new StringReader(command)).runApp();
    return output_source.toString().split(System.lineSeparator())[1] + System.lineSeparator();
  }
}
