import org.junit.Test;

import java.io.IOException;

import cs3500.imageprocessor.commands.LoadCommand;
import cs3500.imageprocessor.ProcessResult;
import cs3500.imageprocessor.model.ImageProcessorModel;
import cs3500.imageprocessor.model.ImageProcessorModelImpl;
import cs3500.imageprocessor.view.TextBasedView;

import static org.junit.Assert.assertEquals;

/**
 * Tests for a text-based view.
 */
public class TextBasedViewTest {

  // test constructors of the view
  @Test
  public void testConstructors() throws IOException {
    ImageProcessorModel model = new ImageProcessorModelImpl();
    TextBasedView view = new TextBasedView(model);
    TextBasedView view2 = new TextBasedView(model, new StringBuilder());
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("Koala.ppm", "koala")));
    assertEquals(ProcessResult.SUCCESS, view.renderImage("koala"));
    assertEquals(ProcessResult.SUCCESS, view2.renderImage("koala"));
    assertEquals(ProcessResult.SUCCESS, view.renderAllImage());
    assertEquals(ProcessResult.SUCCESS, view2.renderAllImage());
    assertEquals(ProcessResult.SUCCESS, view.renderMessage("Hello world"));
    assertEquals(ProcessResult.SUCCESS, view2.renderMessage("Hello world"));
  }

  // test constructor of TextBasedView
  // null model
  @Test(expected = IllegalArgumentException.class)
  public void testConstructor1() {
    new TextBasedView(null);
  }

  // test constructor of TextBasedView
  // null appendable
  @Test(expected = IllegalArgumentException.class)
  public void testConstructor2() {
    new TextBasedView(new ImageProcessorModelImpl(), null);
  }

  // test method renderMessage of TextBasedView
  @Test
  public void testRenderMessage() {
    StringBuilder builder = new StringBuilder();
    ImageProcessorModel model = new ImageProcessorModelImpl();
    TextBasedView view = new TextBasedView(model, builder);
    assertEquals(ProcessResult.SUCCESS, view.renderMessage("Hello world!"));
    assertEquals("Hello world!\r\n", builder.toString());
    assertEquals(ProcessResult.SUCCESS, view.renderMessage("print int 1, 2, 3"));
    assertEquals("Hello world!\r\nprint int 1, 2, 3\r\n", builder.toString());
  }

  // test method renderImage of TextBasedView
  @Test
  public void testRenderImage() {
    StringBuilder builder = new StringBuilder();
    ImageProcessorModel model = new ImageProcessorModelImpl();
    TextBasedView view = new TextBasedView(model, builder);
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("Koala.ppm", "koala")));
    assertEquals(ProcessResult.SUCCESS, view.renderImage("koala"));
    assertEquals("koala: Width 1024 | Height 768 | File Type ppm\r\n", builder.toString());
    assertEquals(ProcessResult.IMAGE_NOT_FOUND, view.renderImage(null));
    assertEquals(ProcessResult.IMAGE_NOT_FOUND, view.renderImage("koala-not-exist"));
  }

  // test method renderAllImage of TextBasedView
  @Test
  public void testRenderAllImage() {
    StringBuilder builder = new StringBuilder();
    ImageProcessorModel model = new ImageProcessorModelImpl();
    TextBasedView view = new TextBasedView(model, builder);
    assertEquals(ProcessResult.SUCCESS, view.renderAllImage());
    assertEquals("Listing All Images: \r\n", builder.toString());
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("Koala.ppm", "koala")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("koala-blue-greyscale.ppm", "koala-b")));
    assertEquals(ProcessResult.SUCCESS, view.renderAllImage());
    assertEquals("Listing All Images: \r\n"
        + "Listing All Images: \r\n"
        + "koala: Width 1024 | Height 768 | File Type ppm\r\n"
        + "koala-b: Width 1024 | Height 768 | File Type ppm\r\n", builder.toString());
  }
}
