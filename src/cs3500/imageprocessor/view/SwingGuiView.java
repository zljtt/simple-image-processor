package cs3500.imageprocessor.view;

import java.awt.event.ActionListener;

import javax.swing.*;

import cs3500.imageprocessor.ProcessResult;
import cs3500.imageprocessor.SwingFeaturesFrame;
import cs3500.imageprocessor.model.ImageProcessorViewModel;

public class SwingGuiView implements ImageProcessorGuiView {

  private final ImageProcessorViewModel model;
  private SwingGuiFrame board;

  public SwingGuiView(ImageProcessorViewModel model) {
    this.model = model;
  }

  /**
   * Render the board to the gui.
   *
   * @param actionListener the action listener to register actions
   * @return a ProcessResult indicate the result of the rendering
   */
  @Override
  public ProcessResult renderBoard(ActionListener actionListener) {
    SwingFeaturesFrame.setDefaultLookAndFeelDecorated(false);
    this.board = new SwingGuiFrame();
    return this.board.initBoard(actionListener);
  }

  /**
   * Render the error/success message to the gui.
   *
   * @param result the message of the process result
   */
  @Override
  public void renderProcessResult(ProcessResult result) {
    if (result != ProcessResult.SUCCESS) {
      JOptionPane.showMessageDialog(this.board, result.getOutputString(), "Error",
          JOptionPane.PLAIN_MESSAGE);
    }
  }

  /**
   * Get the main board of the gui.
   *
   * @return the board
   */
  @Override
  public JFrame getBoard() {
    return this.board;
  }

  @Override
  public JComponent getComponent(String name) {
    return this.board.getComponent(name);
  }
}
