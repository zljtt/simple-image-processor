package cs3500.imageprocessor.commands;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import cs3500.imageprocessor.ProcessResult;
import cs3500.imageprocessor.image.AbstractImage;
import cs3500.imageprocessor.image.CommonImage;
import cs3500.imageprocessor.model.ImageProcessorModel;
import cs3500.imageprocessor.view.ImageProcessorGuiView;

/**
 * The command that modify the brightness of the image.
 */
public class LoadCommand implements ImageProcessCommand {

  private final String[] context;

  /**
   * Construct a command using the context.
   *
   * @param context the additional information for the command, in other words the arguments.
   *                Take in the increment of brightness, the original image, the destination image.
   */
  public LoadCommand(String... context) {
    this.context = context;
  }

  /**
   * Construct a command using the view.
   *
   * @param view the view to get the additional context.
   */
  public LoadCommand(ImageProcessorGuiView view) {
    this.context = new String[2];
    final JFileChooser fileChooser = new JFileChooser(".");
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Common Images",
        "jpg", "png", "bmp", "ppm");
    fileChooser.setFileFilter(filter);
    int result = fileChooser.showOpenDialog(view.getBoard());
    if (result == JFileChooser.APPROVE_OPTION) {
      File f = fileChooser.getSelectedFile();
      context[0] = f.getAbsolutePath();
      JComponent component = view.getComponent("open-file-text");
      if (component instanceof JTextField && f.exists()) {
        String text = ((JTextField) component).getText();
        if (text.isEmpty() || text.isBlank()) {
          context[1] = f.getName();
        } else {
          context[1] = text;
        }
      }
    }
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
    AbstractImage image = null;
    try {
      image = readImage(context[0]);
    } catch (IOException | NullPointerException e) {
      return ProcessResult.FILE_NOT_FOUND;
    } catch (StringIndexOutOfBoundsException | NoSuchElementException e) {
      return ProcessResult.UNSUPPORTED_FILE_FORMAT;
    }
    if (image == null) {
      return ProcessResult.UNSUPPORTED_FILE_FORMAT;
    }
    // attempt to override with the same image name
    if (model.getImage(context[1]) != null) {
      model.getImageBuffer().put(context[1], image);
      return ProcessResult.OVERRIDE_IMAGE;
    }
    model.getImageBuffer().put(context[1], image);
    return ProcessResult.SUCCESS;
  }

  private AbstractImage readImage(String dir)
      throws IOException, NullPointerException,
      StringIndexOutOfBoundsException, NoSuchElementException {
    String[] str = dir.split("\\.");
    if (str.length <= 1) {
      return null;
    }
    String format = str[str.length - 1];
    AbstractImage image;
    switch (format) {
      case "ppm":
        // copied and modified from ImageUtil
        Scanner file_sc;
        file_sc = new Scanner(new FileInputStream(dir));
        StringBuilder builder = new StringBuilder();
        //read the file line by line, and populate a string. This will throw away any comment lines
        while (file_sc.hasNextLine()) {
          String s = file_sc.nextLine();
          if (s.charAt(0) != '#') {
            builder.append(s).append(System.lineSeparator());
          }
        }
        file_sc.close();
        //now set up the scanner to read from the string we just built
        Scanner sc = new Scanner(builder.toString());
        String token;
        token = sc.next();
        if (!token.equals("P3")) {
          return null;
        }
        int width = sc.nextInt();
        int height = sc.nextInt();
        int maxValue = sc.nextInt();
        image = new CommonImage(width, height, maxValue, BufferedImage.TYPE_INT_RGB, format);
        for (int y = 0; y < height; y++) {
          for (int x = 0; x < width; x++) {
            int r = sc.nextInt();
            int g = sc.nextInt();
            int b = sc.nextInt();
            image.setRBG(x, y, r, g, b, maxValue);
          }
        }
        sc.close();
        break;
      case "bmp":
      case "jpg":
      case "png":
        BufferedImage commonImage = ImageIO.read(new File(dir));
        image = new CommonImage(commonImage, 255, format);
        break;
      default:
        return null;
    }
    return image;
  }
}
