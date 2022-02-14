import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import cs3500.imageprocessor.commands.LoadCommand;
import cs3500.imageprocessor.controller.TextBasedController;
import cs3500.imageprocessor.model.ImageProcessorModel;
import cs3500.imageprocessor.model.ImageProcessorModelImpl;
import cs3500.imageprocessor.view.ImageProcessorView;
import cs3500.imageprocessor.view.TextBasedView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for a text-based view.
 */
public class TextBasedControllerTest {

  // test constructors of the controller
  @Test
  public void testConstructors() throws IOException {
    ImageProcessorModel model = new ImageProcessorModelImpl();
    StringBuilder builder = new StringBuilder();
    TextBasedView view = new TextBasedView(model, builder);
    TextBasedController controller1 = new TextBasedController(model, view,
        new StringReader("q"));
    TextBasedController controller2 = new TextBasedController(model, view);
    controller1.runApp();
    assertEquals("Please enter command:\r\n", builder.toString());
  }

  // test constructor
  // null model
  @Test(expected = IllegalArgumentException.class)
  public void testConstructor1() {
    new TextBasedController(null, new TextBasedView(new ImageProcessorModelImpl(),
        new StringBuilder()), new StringReader("q"));
  }

  // test constructor
  // null view
  @Test(expected = IllegalArgumentException.class)
  public void testConstructor2() {
    new TextBasedController(new ImageProcessorModelImpl(), null, new StringReader("q"));
  }

  // test constructor
  // null reader
  @Test(expected = IllegalArgumentException.class)
  public void testConstructor3() {
    ImageProcessorModel model = new ImageProcessorModelImpl();
    new TextBasedController(model, new TextBasedView(model,
        new StringBuilder()), null);
  }

  // test the quiting the method runApp of ImageProcessorModel
  @Test
  public void testRunAppQuit() {
    ImageProcessorModel model = new ImageProcessorModelImpl();
    StringBuilder output_source = new StringBuilder();
    ImageProcessorView view = new TextBasedView(model, output_source);
    new TextBasedController(model, view, new StringReader("q")).runApp();
    assertEquals("Please enter command:\r\n", output_source.toString());
    output_source.setLength(0);
    new TextBasedController(model, view, new StringReader("quit")).runApp();
    assertEquals("Please enter command:\r\n", output_source.toString());
    output_source.setLength(0);
    new TextBasedController(model, view, new StringReader("Q")).runApp();
    assertEquals("Please enter command:\r\n", output_source.toString());
  }

  // test the method runApp with command line of ImageProcessorModel
  @Test
  public void testRunAppWithCommandLine() {
    ImageProcessorModel model = new ImageProcessorModelImpl();
    StringBuilder output_source = new StringBuilder();
    ImageProcessorView view = new TextBasedView(model, output_source);
    new TextBasedController(model, view).runApp(
        "load Koala.ppm koala",
        "load Koala.jpg koala-jpg",
        "load Koala.png koala-png",
        "load Koala.bmp koala-bmp",
        "color-transform red koala koala-red",
        "color-transform green koala koala-green",
        "color-transform blue koala koala-blue",
        "color-transform intensity koala koala-intensity",
        "color-transform value koala koala-value",
        "color-transform luma koala koala-luma",
        "color-transform sepia koala koala-sepia",
        "filter blur koala koala-blur",
        "filter blur koala-blur koala-super-blur",
        "filter blur koala-super-blur koala-super-blur",
        "filter blur koala-super-blur koala-super-blur",
        "filter sharpen koala koala-sharpen",
        "flip horizontal koala koala-horizontal",
        "flip vertical koala koala-vertical",
        "brighten 50 koala koala-brighter",
        "brighten -50 koala koala-darker",
        "save res/koala-copy.ppm koala",
        "save res/koala-copy.jpg koala",
        "save res/koala-copy.png koala",
        "save res/koala-copy.bmp koala",
        "save res/koala-red.png koala-red",
        "save res/koala-green.png koala-green",
        "save res/koala-blue.png koala-blue",
        "save res/koala-intensity.png koala-intensity",
        "save res/koala-value.jpg koala-value",
        "save res/koala-luma.jpg koala-luma",
        "save res/koala-sepia.jpg koala-sepia",
        "save res/koala-super-blur.jpg koala-super-blur",
        "save res/koala-sharpen.jpg koala-sharpen",
        "save res/koala-horizontal.bmp koala-horizontal",
        "save res/koala-vertical.bmp koala-vertical",
        "save res/koala-brighter-by-50.bmp koala-brighter",
        "save res/koala-darker-by-50.bmp koala-darker");
    assertEquals("Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds, the image has been overridden by the new one.\r\n" +
        "Process succeeds, the image has been overridden by the new one.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n" +
        "Process succeeds.\r\n", output_source.toString());
  }

  // test the method runApp of ImageProcessorModel
  @Test
  public void testRunApp() {
    ImageProcessorModel model = new ImageProcessorModelImpl();

    // test commands
    assertTrue(TestHelper.runCommandSuccess(model, "load Koala.ppm koala-ppm\r\nquit"));
    assertTrue(TestHelper.runCommandSuccess(model, "load Koala.jpg koala-jpg\nquit"));
    assertTrue(TestHelper.runCommandSuccess(model, "load Koala.jpg koala-png\nquit"));
    assertTrue(TestHelper.runCommandSuccess(model, "load Koala.jpg koala-bmp\nquit"));
    assertTrue(TestHelper.runCommandSuccess(model,
        "color-transform red koala-ppm koala-red\r\nquit"));
    assertTrue(TestHelper.runCommandSuccess(model,
        "color-transform green koala-jpg koala-green\r\nquit"));
    assertTrue(TestHelper.runCommandSuccess(model,
        "color-transform blue koala-png koala-blue\r\nquit"));
    assertTrue(TestHelper.runCommandSuccess(model,
        "color-transform value koala-bmp koala-value\r\nquit"));
    assertTrue(TestHelper.runCommandSuccess(model,
        "color-transform intensity koala-ppm koala-intensity\r\nquit"));
    assertTrue(TestHelper.runCommandSuccess(model,
        "color-transform luma koala-jpg koala-luma\r\nquit"));
    assertTrue(TestHelper.runCommandSuccess(model,
        "color-transform sepia koala-png koala-sepia\r\nquit"));
    assertTrue(TestHelper.runCommandSuccess(model,
        "filter blur koala-bmp koala-blur\r\nquit"));
    assertTrue(TestHelper.runCommandSuccess(model,
        "filter sharpen koala-ppm koala-sharpen\r\nquit"));
    assertTrue(TestHelper.runCommandSuccess(model,
        "flip vertical koala-jpg koala-vertical\r\nquit"));
    assertTrue(TestHelper.runCommandSuccess(model,
        "flip horizontal koala-png koala-horizontal\r\nquit"));
    assertTrue(TestHelper.runCommandSuccess(model,
        "brighten 30 koala-bmp koala-brighten-by-30\r\nquit"));
    assertTrue(TestHelper.runCommandSuccess(model,
        "brighten -50 koala-ppm koala-darken-by-50\r\nquit"));
    assertTrue(TestHelper.runCommandSuccess(model,
        "copy koala-ppm koala-copy\r\nquit"));
    assertTrue(TestHelper.runCommandSuccess(model,
        "save koala-copy.ppm koala-copy\r\nquit"));
  }

  // test the error messages in method runApp of ImageProcessorModel
  @Test
  public void testRunAppErrors() {
    ImageProcessorModel model = new ImageProcessorModelImpl();
    model.accept(new LoadCommand("Koala.ppm", "koala"));
    // file not found
    assertEquals("File not found in the given location.\r\n",
        TestHelper.runCommand(model, "load Koala-not-exist.ppm koala\r\nquit"));
    // image not found
    assertEquals("Image not found in the processor.\r\n",
        TestHelper.runCommand(model, "save Koala-not-exist.ppm koala-not-exist\r\nquit"));
    // incorrect format
    assertEquals("The given file format is not supported.\r\n",
        TestHelper.runCommand(model, "load Koala-incorrect-header.ppm koala-incorrect\r\nquit"));
    // unknown command
    assertEquals("Unknown command head.\r\n",
        TestHelper.runCommand(model, "not-exist-command koala koala-2\r\nquit"));
    // incorrect command format
    assertEquals("Unsupported command format.\r\n",
        TestHelper.runCommand(model, "brighten not-an-int koala koala-brighter\r\nquit"));
    // not enough command elements
    assertEquals("Not enough entries in the command.\r\n",
        TestHelper.runCommand(model, "brighten 50 koala\r\nquit"));
  }


}
