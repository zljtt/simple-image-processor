import org.junit.Test;

import cs3500.imageprocessor.commands.BrightnessCommand;
import cs3500.imageprocessor.commands.ColorTransformCommand;
import cs3500.imageprocessor.commands.ColorTransformCommand.ColorTransformOperation;
import cs3500.imageprocessor.commands.CopyCommand;
import cs3500.imageprocessor.commands.FilterCommand;
import cs3500.imageprocessor.commands.FlipCommand;
import cs3500.imageprocessor.commands.LoadCommand;
import cs3500.imageprocessor.ProcessResult;
import cs3500.imageprocessor.commands.SaveCommand;
import cs3500.imageprocessor.model.ImageProcessorModel;
import cs3500.imageprocessor.model.ImageProcessorModelImpl;
import cs3500.imageprocessor.view.TextBasedView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for ImageProcessorModel.
 */
public class ImageProcessorModelTest {

  // test accepting LoadCommand of ImageProcessorModel
  @Test
  public void testLoad() {
    ImageProcessorModel model = new ImageProcessorModelImpl();
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("Koala.ppm", "koala-ppm")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("Koala.jpg", "koala-jpg")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("Koala.png", "koala-png")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("Koala.bmp", "koala-bmp")));
    StringBuilder builder = new StringBuilder();
    assertEquals(ProcessResult.SUCCESS,
        new TextBasedView(model, builder).renderImage("koala-ppm"));
    assertEquals(ProcessResult.SUCCESS,
        new TextBasedView(model, builder).renderImage("koala-jpg"));
    assertEquals(ProcessResult.SUCCESS,
        new TextBasedView(model, builder).renderImage("koala-png"));
    assertEquals(ProcessResult.SUCCESS,
        new TextBasedView(model, builder).renderImage("koala-bmp"));
    assertEquals("koala-ppm: Width 1024 | Height 768 | File Type ppm\r\n"
        + "koala-jpg: Width 1024 | Height 768 | File Type jpg\r\n"
        + "koala-png: Width 1024 | Height 768 | File Type png\r\n"
        + "koala-bmp: Width 1024 | Height 768 | File Type bmp\r\n", builder.toString());
    // test exception
    assertEquals(ProcessResult.FILE_NOT_FOUND,
        model.accept(new LoadCommand("Koala-not-exist.jpg", "koala-jpg")));
    assertEquals(ProcessResult.UNSUPPORTED_FILE_FORMAT,
        model.accept(new LoadCommand("Koala-incorrect-header.ppm", "koala-ppm")));
    assertEquals(ProcessResult.UNSUPPORTED_FILE_FORMAT,
        model.accept(new LoadCommand("Koala-incorrect-elements.ppm", "koala-ppm")));
  }

  // test accepting SaveCommand of ImageProcessorModel
  @Test
  public void testSave() {
    ImageProcessorModel model = new ImageProcessorModelImpl();
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("Koala.ppm", "koala-ppm")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("Koala.jpg", "koala-jpg")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("Koala.png", "koala-png")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("Koala.bmp", "koala-bmp")));
    // save ppm
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new SaveCommand("Koala-copy.ppm", "koala-ppm")));
    assertTrue(TestHelper.samePPM("Koala.ppm", "Koala-copy.ppm"));
    // save other format
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new SaveCommand("Koala-from-ppm.jpg", "koala-ppm")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new SaveCommand("Koala-from-ppm.png", "koala-ppm")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new SaveCommand("Koala-from-ppm.bmp", "koala-ppm")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new SaveCommand("Koala-from-bmp.ppm", "koala-bmp")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new SaveCommand("Koala-from-jpg.ppm", "koala-jpg")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new SaveCommand("Koala-from-png.ppm", "koala-png")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new SaveCommand("Koala-from-png.jpg", "koala-png")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new SaveCommand("Koala-from-jpg.png", "koala-jpg")));
    // test exception
    assertEquals(ProcessResult.IMAGE_NOT_FOUND,
        model.accept(new SaveCommand("Koala-copy.ppm", "koala-not-exist")));
    assertEquals(ProcessResult.IMAGE_NOT_FOUND,
        model.accept(new SaveCommand("Koala-copy.jpg", "koala-not-exist")));
    assertEquals(ProcessResult.IMAGE_NOT_FOUND,
        model.accept(new SaveCommand("Koala-copy.png", "koala-not-exist")));
    assertEquals(ProcessResult.IMAGE_NOT_FOUND,
        model.accept(new SaveCommand("Koala-copy.bmp", "koala-not-exist")));
  }

  // test accepting CopyCommand of ImageProcessorModel
  @Test
  public void testCopy() {
    ImageProcessorModel model = new ImageProcessorModelImpl();
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("Koala.ppm", "koala")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new CopyCommand("koala", "koala2")));
    assertEquals(model.getImage("koala"), model.getImage("koala2"));
    assertEquals(ProcessResult.IMAGE_NOT_FOUND,
        model.accept(new CopyCommand("koala3", "koala1")));
    assertEquals(ProcessResult.OVERRIDE_IMAGE,
        model.accept(new CopyCommand("koala2", "koala2")));
    assertEquals(ProcessResult.OVERRIDE_IMAGE,
        model.accept(new CopyCommand("koala", "koala")));
    assertEquals(model.getImage("koala"), model.getImage("koala"));
  }

  // test accepting ColorTransformCommand of ImageProcessorModel
  @Test
  public void testColorTransform() {
    ImageProcessorModel model = new ImageProcessorModelImpl();
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("Koala.ppm", "koala-ppm")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("Koala.bmp", "koala-bmp")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("Koala.jpg", "koala-jpg")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("Koala.png", "koala-png")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("koala-blue-greyscale.ppm", "koala-ppm-blue-accurate")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("koala-green-greyscale.ppm", "koala-ppm-green-accurate")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("koala-red-greyscale.ppm", "koala-ppm-red-accurate")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("koala-luma-greyscale.ppm", "koala-ppm-luma-accurate")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("koala-value-greyscale.ppm", "koala-ppm-value-accurate")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("koala-intensity-greyscale.ppm",
            "koala-ppm-intensity-accurate")));
    // test exception
    assertEquals(ProcessResult.INCORRECT_COMMAND_FORMAT,
        model.accept(new ColorTransformCommand("null",
            "koala-ppm", "koala-blue-test")));
    assertEquals(ProcessResult.IMAGE_NOT_FOUND,
        model.accept(new ColorTransformCommand(ColorTransformOperation.BLUE.getCommand(),
            "koala-not-exist", "koala-blue-test")));
    model.accept(new CopyCommand("koala-ppm", "koala-ppm2"));
    assertEquals(ProcessResult.OVERRIDE_IMAGE,
        model.accept(new ColorTransformCommand(ColorTransformOperation.BLUE.getCommand(),
            "koala-ppm", "koala-ppm2")));
    // test blue
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new ColorTransformCommand(ColorTransformOperation.BLUE.getCommand(),
            "koala-ppm", "koala-ppm-blue-test")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new ColorTransformCommand(ColorTransformOperation.BLUE.getCommand(),
            "koala-jpg", "koala-jpg-blue-test")));
    assertEquals(model.getImage("koala-ppm-blue-test"), model.getImage("koala-ppm-blue-accurate"));
    // test green
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new ColorTransformCommand(ColorTransformOperation.GREEN.getCommand(),
            "koala-ppm", "koala-ppm-green-test")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new ColorTransformCommand(ColorTransformOperation.GREEN.getCommand(),
            "koala-png", "koala-png-green-test")));
    assertEquals(model.getImage("koala-ppm-green-test"),
        model.getImage("koala-ppm-green-accurate"));
    // test red
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new ColorTransformCommand(ColorTransformOperation.RED.getCommand(),
            "koala-ppm", "koala-ppm-red-test")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new ColorTransformCommand(ColorTransformOperation.RED.getCommand(),
            "koala-bmp", "koala-bmp-red-test")));
    assertEquals(model.getImage("koala-ppm-red-test"), model.getImage("koala-ppm-red-accurate"));
    // test luma
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new ColorTransformCommand(ColorTransformOperation.LUMA.getCommand(),
            "koala-ppm", "koala-ppm-luma-test")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new ColorTransformCommand(ColorTransformOperation.LUMA.getCommand(),
            "koala-jpg", "koala-jpg-luma-test")));
    assertEquals(model.getImage("koala-ppm-luma-test"), model.getImage("koala-ppm-luma-accurate"));
    // test value
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new ColorTransformCommand(ColorTransformOperation.VALUE.getCommand(),
            "koala-ppm", "koala-ppm-value-test")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new ColorTransformCommand(ColorTransformOperation.VALUE.getCommand(),
            "koala-png", "koala-png-value-test")));
    assertEquals(model.getImage("koala-ppm-value-test"),
        model.getImage("koala-ppm-value-accurate"));
    // test intensity
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new ColorTransformCommand(ColorTransformOperation.INTENSITY.getCommand(),
            "koala-ppm", "koala-ppm-intensity-test")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new ColorTransformCommand(ColorTransformOperation.INTENSITY.getCommand(),
            "koala-bmp", "koala-bmp-intensity-test")));
    assertEquals(model.getImage("koala-ppm-intensity-test"),
        model.getImage("koala-ppm-intensity-accurate"));
    // test sepia
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new ColorTransformCommand(ColorTransformOperation.SEPIA.getCommand(),
            "koala-ppm", "koala-ppm-sepia-test")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new ColorTransformCommand(ColorTransformOperation.SEPIA.getCommand(),
            "koala-jpg", "koala-jpg-sepia-test")));
  }

  // test accepting brighten command of ImageProcessorModel
  @Test
  public void testModifyBrightness() {
    ImageProcessorModel model = new ImageProcessorModelImpl();
    // load
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("Koala.ppm", "koala-ppm")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("Koala.jpg", "koala-jpg")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("Koala.png", "koala-png")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("Koala.bmp", "koala-bmp")));
    // test
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new BrightnessCommand("50", "koala-ppm", "koala-ppm-brighten-test")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new BrightnessCommand("12", "koala-jpg", "koala-jpg-brighten-test")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new BrightnessCommand("-30", "koala-png", "koala-png-brighten-test")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new BrightnessCommand("0", "koala-bmp", "koala-bmp-brighten-test")));
    assertEquals(model.getImage("koala-bmp"),
        model.getImage("koala-bmp-brighten-test"));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("koala-brighter-by-50.ppm",
            "koala-ppm-brighten-accurate")));
    assertEquals(model.getImage("koala-ppm-brighten-test"),
        model.getImage("koala-ppm-brighten-accurate"));
    // test exception
    assertEquals(ProcessResult.IMAGE_NOT_FOUND,
        model.accept(new BrightnessCommand("50", "koala-not-exist", "koala-ppm-brighten-test")));
    assertEquals(ProcessResult.OVERRIDE_IMAGE,
        model.accept(new BrightnessCommand("50", "koala-ppm", "koala-ppm-brighten-test")));
    assertEquals(ProcessResult.OVERRIDE_IMAGE,
        model.accept(new BrightnessCommand("50", "koala-jpg", "koala-jpg-brighten-test")));
  }

  // test accepting filter command of ImageProcessorModel
  @Test
  public void testFilter() {
    ImageProcessorModel model = new ImageProcessorModelImpl();
    // load all
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("Koala.ppm", "koala-ppm")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("Koala.jpg", "koala-jpg")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("Koala.png", "koala-png")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("Koala.bmp", "koala-bmp")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new FilterCommand("blur", "koala-ppm", "koala-ppm-blur-test")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new FilterCommand("sharpen", "koala-ppm", "koala-ppm-sharpen-test")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new FilterCommand("blur", "koala-jpg", "koala-jpg-blur-test")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new FilterCommand("sharpen", "koala-jpg", "koala-jpg-sharpen-test")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new FilterCommand("blur", "koala-png", "koala-png-blur-test")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new FilterCommand("sharpen", "koala-png", "koala-png-sharpen-test")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new FilterCommand("blur", "koala-bmp", "koala-bmp-blur-test")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new FilterCommand("sharpen", "koala-bmp", "koala-bmp-sharpen-test")));
  }

  // test accepting flip command of ImageProcessorModel
  @Test
  public void testFlip() {
    ImageProcessorModel model = new ImageProcessorModelImpl();
    // load all
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("Koala.ppm", "koala-ppm")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("Koala.jpg", "koala-jpg")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("Koala.png", "koala-png")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("Koala.bmp", "koala-bmp")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("koala-horizontal.ppm", "koala-ppm-horizontal-accurate")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("koala-vertical.ppm", "koala-ppm-vertical-accurate")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("koala-horizontal-vertical.ppm",
            "koala-ppm-horizontal-vertical-accurate")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new LoadCommand("koala-vertical-horizontal.ppm",
            "koala-ppm-vertical-horizontal-accurate")));
    // test
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new FlipCommand("horizontal", "koala-ppm", "koala-ppm-horizontal-test")));
    assertEquals(model.getImage("koala-ppm-horizontal-test"),
        model.getImage("koala-ppm-horizontal-accurate"));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new FlipCommand("vertical", "koala-ppm", "koala-ppm-vertical-test")));
    assertEquals(model.getImage("koala-ppm-vertical-test"),
        model.getImage("koala-ppm-vertical-accurate"));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new FlipCommand("horizontal", "koala-ppm-vertical-test",
            "koala-ppm-vertical-horizontal-test")));
    assertEquals(model.getImage("koala-ppm-vertical-horizontal-test"),
        model.getImage("koala-ppm-vertical-horizontal-accurate"));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new FlipCommand("vertical", "koala-ppm-horizontal-test",
            "koala-ppm-horizontal-vertical-test")));
    assertEquals(model.getImage("koala-ppm-horizontal-vertical-test"),
        model.getImage("koala-ppm-horizontal-vertical-accurate"));
    assertEquals(model.getImage("koala-ppm-horizontal-vertical-test"),
        model.getImage("koala-ppm-vertical-horizontal-test"));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new FlipCommand("vertical", "koala-jpg",
            "koala-jpg-horizontal-vertical-test")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new FlipCommand("vertical", "koala-png",
            "koala-png-horizontal-vertical-test")));
    assertEquals(ProcessResult.SUCCESS,
        model.accept(new FlipCommand("vertical", "koala-bmp",
            "koala-bmp-horizontal-vertical-test")));
    // test exception
    assertEquals(ProcessResult.IMAGE_NOT_FOUND,
        model.accept(new FlipCommand("vertical", "koala-not-exist", "koala-vertical-test")));
    assertEquals(ProcessResult.OVERRIDE_IMAGE,
        model.accept(new FlipCommand("vertical", "koala-ppm", "koala-ppm-vertical-test")));
  }

  // test the method getImage of ImageProcessorModel
  @Test
  public void testGetImage() {
    ImageProcessorModel model = new ImageProcessorModelImpl();
    assertEquals(ProcessResult.SUCCESS, model.accept(new LoadCommand("Koala.ppm", "koala-ppm")));
    assertEquals(ProcessResult.SUCCESS, model.accept(new LoadCommand("Koala.jpg", "koala-jpg")));
    assertNull(model.getImage("koala-not-exist"));
    assertNotEquals(null, model.getImage("koala-ppm"));
    assertEquals(1024, model.getImage("koala-ppm").getWidth());
    assertEquals(768, model.getImage("koala-ppm").getHeight());
    assertEquals(255, model.getImage("koala-ppm").getMaxValue());
    assertEquals(101, model.getImage("koala-ppm").getRed(0, 0));
    assertEquals(103, model.getImage("koala-ppm").getRed(1, 0));
    assertNotEquals(null, model.getImage("koala-jpg"));
    assertEquals(1024, model.getImage("koala-jpg").getWidth());
    assertEquals(768, model.getImage("koala-jpg").getHeight());
    assertEquals(255, model.getImage("koala-jpg").getMaxValue());
    assertEquals(101, model.getImage("koala-jpg").getRed(0, 0));
    assertEquals(103, model.getImage("koala-jpg").getRed(1, 0));
  }

  // test the method getAllImages of ImageProcessorModel
  @Test
  public void testGetAllImages() {
    ImageProcessorModel model = new ImageProcessorModelImpl();
    assertEquals(ProcessResult.SUCCESS, model.accept(new LoadCommand("Koala.ppm", "koala-ppm")));
    assertEquals(ProcessResult.SUCCESS, model.accept(new LoadCommand("Koala.jpg", "koala-jpg")));
    assertEquals(ProcessResult.SUCCESS, model.accept(new LoadCommand("Koala.png", "koala-png")));
    assertEquals(ProcessResult.SUCCESS, model.accept(new LoadCommand("Koala.bmp", "koala-bmp")));
    assertEquals("ppm", model.getAllImages().get("koala-ppm").getImageType());
    assertEquals("jpg", model.getAllImages().get("koala-jpg").getImageType());
    assertEquals("png", model.getAllImages().get("koala-png").getImageType());
    assertEquals("bmp", model.getAllImages().get("koala-bmp").getImageType());
  }

  // test the method getProcessorName of ImageProcessorModel
  @Test
  public void testGetProcessorName() {
    ImageProcessorModel model = new ImageProcessorModelImpl();
    assertEquals("Image Processor", model.getProcessorName());
  }
}
