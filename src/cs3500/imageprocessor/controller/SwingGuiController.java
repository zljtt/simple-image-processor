package cs3500.imageprocessor.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import cs3500.imageprocessor.commands.BrightnessCommand;
import cs3500.imageprocessor.commands.ColorTransformCommand;
import cs3500.imageprocessor.commands.CopyCommand;
import cs3500.imageprocessor.commands.FilterCommand;
import cs3500.imageprocessor.commands.FlipCommand;
import cs3500.imageprocessor.commands.ImageProcessCommand;
import cs3500.imageprocessor.commands.LoadCommand;
import cs3500.imageprocessor.commands.SaveCommand;
import cs3500.imageprocessor.image.AbstractImage;
import cs3500.imageprocessor.image.AbstractViewImage;
import cs3500.imageprocessor.model.ImageProcessorModel;
import cs3500.imageprocessor.view.ImageProcessorGuiView;

public class SwingGuiController implements ImageProcessorController, ActionListener {
  private final Map<String, Function<ImageProcessorGuiView, ImageProcessCommand>> commands;
  private final ImageProcessorModel model;
  private final ImageProcessorGuiView view;

  /**
   * The controller constructor that take in a model and view.
   * The input readable is in default the System.in.
   *
   * @param model the model
   * @param view  the gui view
   */
  public SwingGuiController(ImageProcessorModel model, ImageProcessorGuiView view) {
    if (model == null || view == null) {
      throw new IllegalArgumentException("Invalid model, view or input");
    }
    this.model = model;
    this.view = view;
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
   * @param commands the given command line inputs
   */
  @Override
  public void runApp(String... commands) {
    this.view.renderProcessResult(this.view.renderBoard(this));
    this.view.getBoard().revalidate();
  }

  /**
   * Invoked when an action occurs.
   *
   * @param event the event to be processed
   */
  @Override
  public void actionPerformed(ActionEvent event) {
    String key = event.getActionCommand();
    if (event.getActionCommand().equals("choose-image") && event.getSource() instanceof JComboBox) {
      JComboBox<String> box = (JComboBox<String>) event.getSource();
      AbstractImage image = this.model.getImage((String) box.getSelectedItem());
      JComponent label = this.view.getComponent("image-label");
      if (image != null && label instanceof JLabel) {
        ((JLabel) label).setIcon(new ImageIcon(image.getImageData()));
      }
      //comboboxDisplay.setText("You selected: " + (String) );
    } else {
      Function<ImageProcessorGuiView, ImageProcessCommand> command = this.commands.get(key);
      if (command != null) {
        this.view.renderProcessResult(command.apply(this.view).process(this.model));
        this.refreshImageList();
      }
    }
  }

  private void refreshImageList() {
    // refresh image list
    JComponent temp = temp = this.view.getComponent("image-combo-box");
    if (temp instanceof JComboBox) {
      JComboBox<String> images = (JComboBox<String>) temp;
      images.removeAllItems();
      for (String image : this.model.getAllImages().keySet()) {
        images.addItem(image);
      }
    }
    this.view.getBoard().revalidate();
  }
}
