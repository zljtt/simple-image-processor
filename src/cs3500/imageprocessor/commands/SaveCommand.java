package cs3500.imageprocessor.commands;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import cs3500.imageprocessor.ProcessResult;
import cs3500.imageprocessor.image.AbstractImage;
import cs3500.imageprocessor.model.ImageProcessorModel;
import cs3500.imageprocessor.view.ImageProcessorGuiView;

/**
 * The command that modify the brightness of the image.
 */
public class SaveCommand implements ImageProcessCommand {
  private final String[] context;

  /**
   * Construct a command using the context.
   *
   * @param context the additional information for the command, in other words the arguments.
   *                Take in the increment of brightness, the original image, the destination image.
   */
  public SaveCommand(String... context) {
    this.context = context;
  }

  /**
   * Construct a command using the view.
   *
   * @param view the view to get the additional context.
   */
  public SaveCommand(ImageProcessorGuiView view) {
    this.context = new String[2];

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
    AbstractImage file = model.getImage(context[1]);
    // get image from the buffer
    if (file == null) {
      return ProcessResult.IMAGE_NOT_FOUND;
    }
    return this.save(file, context[0]);
  }

  private ProcessResult save(AbstractImage image, String dir) {
    String[] str = dir.split("\\.");
    if (str.length <= 1) {
      return ProcessResult.UNSUPPORTED_FILE_FORMAT;
    }
    String format = str[str.length - 1];
    try {
      switch (format) {
        case "ppm":
          BufferedOutputStream out;
          try {
            out = new BufferedOutputStream(new FileOutputStream(dir));
          } catch (FileNotFoundException e) {
            return ProcessResult.FILE_NOT_FOUND;
          }
          // try to write image into file
          out.write(("P3" + System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
          out.write(("# Created by PPM Image Processor"
              + System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
          out.write((image.getWidth() + " " + image.getHeight()
              + System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
          out.write((image.getMaxValue() +
              System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
          for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
              out.write((image.getRed(x, y)
                  + System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
              out.write((image.getGreen(x, y)
                  + System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
              out.write((image.getBlue(x, y)
                  + System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
            }
          }
          out.flush();
          out.close();
          break;
        case "bmp":
        case "jpg":
        case "png":
          String type = image.getImageType();
          if (type.equals("ppm")) {
            type = "png";
          }
          if (!ImageIO.write(image.getImageData(), type, new File(dir))) {
            return ProcessResult.WRITE_FILE_ERROR;
          }
          break;
        default:
          return ProcessResult.UNSUPPORTED_FILE_FORMAT;
      }
    } catch (IOException | NullPointerException e) {
      return ProcessResult.WRITE_FILE_ERROR;
    }
    return ProcessResult.SUCCESS;
  }
}
