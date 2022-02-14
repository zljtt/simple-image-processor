package cs3500.imageprocessor.view;

import java.awt.event.ActionListener;

import javax.swing.*;

import cs3500.imageprocessor.ProcessResult;

/**
 * The view prints out the messages to user and display the images in a processor.
 */
public interface ImageProcessorGuiView {

  /**
   * Render the board to the gui.
   *
   * @param actionListener the action listener to register actions
   * @return a ProcessResult indicate the result of the rendering
   */
  ProcessResult renderBoard(ActionListener actionListener);

  /**
   * Render the error/success message to the gui.
   *
   * @param result the message of the process result
   */
  void renderProcessResult(ProcessResult result);

  /**
   * Get the main board of the gui.
   *
   * @return the board
   */
  JFrame getBoard();

  JComponent getComponent(String name);

}
