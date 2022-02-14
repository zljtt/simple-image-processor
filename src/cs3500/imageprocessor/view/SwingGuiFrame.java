package cs3500.imageprocessor.view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import cs3500.imageprocessor.ProcessResult;
import cs3500.imageprocessor.commands.ColorTransformCommand;
import cs3500.imageprocessor.commands.FilterCommand;
import cs3500.imageprocessor.image.AbstractViewImage;

public class SwingGuiFrame extends JFrame {

  private AbstractViewImage focus;
  private JPanel mainBoard;
  private JScrollPane mainScroll;
  private ActionListener actionListener;
  private Map<String, JComponent> components;

  /**
   * Constructs a new frame that is initially invisible.
   * <p>
   * This constructor sets the component's locale property to the value
   * returned by <code>JComponent.getDefaultLocale</code>.
   *
   * @throws HeadlessException if GraphicsEnvironment.isHeadless()
   *                           returns true.
   * @see GraphicsEnvironment#isHeadless
   * @see Component#setSize
   * @see Component#setVisible
   * @see JComponent#getDefaultLocale
   */
  public SwingGuiFrame() throws HeadlessException {
    super();
    this.components = new HashMap<>();
  }

  public ProcessResult initBoard(ActionListener actionListener) {
    this.setTitle("Image Processor");
    this.setSize(800, 800);

    // settings
    this.actionListener = actionListener;
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);

    // add panels
    mainBoard = new JPanel();
    mainBoard.setLayout(new BoxLayout(mainBoard, BoxLayout.PAGE_AXIS));
    mainScroll = new JScrollPane(mainBoard);
    this.add(mainScroll);


    // display image
    JPanel imagePanel = new JPanel();
    imagePanel.setBorder(BorderFactory.createTitledBorder("Images"));
    imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.PAGE_AXIS));
    // image list
    JPanel imageListPanel = new JPanel();
    JComboBox<String> imageList = new JComboBox<>();
    imageList.addItem("No Image");
    imageList.setActionCommand("choose-image");
    imageList.addActionListener(actionListener);
    // image
    JLabel imageLabel = new JLabel();
    JScrollPane imageScroll = new JScrollPane(imageLabel);
    //imageLabel.setIcon(new ImageIcon());
    imageScroll.setPreferredSize(new Dimension(700, 500));
    this.components.put("image-label", imageLabel);
    addTo(imageScroll, imagePanel, "image-scroll-pane");
    addTo(imageList, imageListPanel, "image-combo-box");
    addTo(imageListPanel, imagePanel, "image-combo-box-panel");
    addTo(imagePanel, mainBoard, "image-panel", BorderLayout.NORTH);


    // save/load
    JPanel saveLoadPanel = new JPanel();
    saveLoadPanel.setLayout(new FlowLayout());
    saveLoadPanel.setPreferredSize(new Dimension(700, 50));
    JTextField fileOpenText = new JTextField();
    fileOpenText.setPreferredSize(new Dimension(250, 40));
    fileOpenText.setBorder(BorderFactory.createTitledBorder("Enter the preferred name of image:"));
    JButton fileOpenButton = new JButton("Open a file (and load as preferred name)");
    JButton fileSaveButton = new JButton("Save current image");
    fileOpenButton.addActionListener(actionListener);
    fileSaveButton.addActionListener(actionListener);
    fileOpenButton.setActionCommand("load");
    fileSaveButton.setActionCommand("save");
    addTo(fileOpenText, saveLoadPanel, "open-file-text");
    addTo(fileOpenButton, saveLoadPanel, "open-file-button");
    addTo(fileSaveButton, saveLoadPanel, "open-save-button");
    addTo(saveLoadPanel, mainBoard, "save-load-panel");

    // image manipulation
    JPanel operationPanel = new JPanel();
    operationPanel.setLayout(new GridBagLayout());
    operationPanel.setPreferredSize(new Dimension(700, 200));
    // lists
    JComboBox<String> filterList = new JComboBox<>();
    for (FilterCommand.FilterOperation operation : FilterCommand.FilterOperation.values()) {
      filterList.addItem(operation.getCommand());
    }
    JComboBox<String> colorTransformList = new JComboBox<>();
    for (ColorTransformCommand.ColorTransformOperation operation :
        ColorTransformCommand.ColorTransformOperation.values()) {
      colorTransformList.addItem(operation.getCommand());
    }
    JComboBox<String> directionList = new JComboBox<>();
    directionList.addItem("horizontal");
    directionList.addItem("vertical");
    JTextField brightnessText = new JTextField();
    brightnessText.setBorder(BorderFactory.createTitledBorder("Enter the brightness increment:"));
    // buttons
    JButton filterButton = new JButton("Filter");
    JButton colorTransformButton = new JButton("Color Transform");
    JButton flipButton = new JButton("Flip");
    JButton brightenButton = new JButton("Brighten");

    addTo(filterList, operationPanel, "filter-combo-box");
    addTo(colorTransformList, operationPanel, "color-transform-combo-box");
    addTo(directionList, operationPanel, "direction-combo-box");
    addTo(brightnessText, operationPanel, "brightness-text");
    addTo(operationPanel, mainBoard, "operation-panel");

    return ProcessResult.SUCCESS;
  }

  private void addTo(JComponent child, JComponent parent, String name, String position) {
    parent.add(child, position);
    this.components.put(name, child);
  }

  private void addTo(JComponent child, JComponent parent, String name, GridBagConstraints position) {
    parent.add(child, position);
    this.components.put(name, child);
  }

  private void addTo(JComponent child, JComponent parent, String name) {
    parent.add(child);
    this.components.put(name, child);
  }

  public JComponent getComponent(String name) {
    return this.components.get(name);
  }
}
