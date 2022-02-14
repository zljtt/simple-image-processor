GROUP MEMBER
Zhijian Wang
Bitan Wu

-----------------------------------------------------------
Updated 2021/11/10
-----------------------------------------------------------

CODE STRUCTURE
The codes have five parts:

[Model]
The model controls the logic of the image processor. It also contains a list of images uploaded by the user in the buffer, waiting to be saved into file system.  
The constructable model object ItemProcceserModelImpl implements ItemProcceserModel; ItemProccesorModel extends ImageProcessorViewModel and CommandReceiver. 
- ItemProcessorModel provides different ways to access and modify the containing images. 
- ImageProcessorViewModel contains all the read-only methods that would not modify the data in the model. 
- CommandReceiver contains a function [+accept(cmd: Command): ProcessResult], which uses hybrid delegation to allows handing over responsibility of image processing from the model to each command.
Through accepting command, the model is able to perform different functions, for example saving, loading and modifying the image contained in the buffer.
Method [accept] return a ProcessResult enum, which indicate the result of running the function with the given arguments. There are many types of result, such as SUCCESS, FILE_NOT_FOUND, OVERRIDE_IMAGE. All ProcessResult contains a message, which can be easily accessed by the controller and be printed out using view.

[View]
The view prints out the messages to the user and display the contained images in a processor.
The current view object is TextbasedView which prints out messages to the Appendable object it holds. Besides the Appendable, constructor also takes in a ImageProcessorViewModel with read-only methods.
TextbasedView implements ImageProcessorView, where ImageProcessorView extends CommandReceiver.
- ImageProcessorView provides different ways to display the containing images. 
- CommandReceiver: the same as mentioned in the Model part.

[Command]
Commands contain the logic of model and views, including saving, loading, modifying and displaying images. The commands are used by the controller for converting user input into function calls. The controller uses different command depends on the first user input token in a line.
All command needs to be constructed with an array of string, which represent the other argument for the command.
There are two catagory of commands: ImageProcessCommand and ImageDisplayCommand, which are both abstract classes implementing Command interface.
- ImageProcessCommand has a method [+process(model: ImageProcessorModel): ProcessResult] that performs logic on the model.
- ImageDisplayCommand has a method [+process(view: ImageProcessorView): ProcessResult] that performs logic on the view.

[Controller]
The controller contain a model and a view. It gives user a way to interact with the model, and use the view to display the images in the model. 
Currently there is a Text-based implementing ImageProcessorController, representing an controller that only take text inputs and print text output.
- ImageProcessorController provide two methods. One without argument would run the commands in the Readable object (usually system.in); another one would run the array of commands given as the function argument.

[Image]
The interface abstractImage indicates the image data type holds in the model. It stores all information of the image read from the image file and prepares for writing back into the file system. 
The image impl is called CommonImage, which represents all image type that would be used in the current project, including ppm, jpg, png and bmp.
- It holds a BufferedImage, and delegate almost all methods like [+getWidth(): int] to the BufferedImage type. 
- There are two ways of construction an CommonImage. One takes in a BufferedImage and other needed data, which is intended to be used while reading the jpg, png and bmp from the file system. Another one takes in the width/height and other data to init a blank BufferedImage and assign all rgb one by one, which is used while reading a ppm.
CommonImage implements AbstractImage, where AbstractImage extends AbstractViewImage.
- AbstractImage provides all functions to modify the image, for example assigning the rgb.
- AbstractViewImage provides all read-only functions that never modify the image data. 
In the read-only methods of ImageProcessorViewModel, AbstractViewImage is provided instead of AbstractImage, so that the original image would not be modified. For example, [+getAllImages(): Map<String, AbstractViewImage>].
PS: Since there we need to switch between different image types in the processor, I believe it is more convenient to combine all image types into one image object instead of having one impl for each image type.


DESIGN CHANGES
- Delegate all the logic from the model to each commands, so that the model can be shorter and more concise. In addition, when there are more model impls in the future, and when I want to add a new function, I do not need to add the new function to every model impls -- I just need to add a new command.
- Added ViewModel Design pattern for the image and model, so that the view can only access read-only methods.
- Build the image with BufferedImage as a field. Therefore, it can be compatible with both ppm image and other common image type (png, jpg, bmp). Most methods delegate to the BufferedImage.
- Rewrite the commands so that the command is constructed with the context and the method [process] takes in a model/view (which is opposite in the last build).
- Add new filter command, and merge greyscale command and sepia into color-transform command.
- Add a main method for that can either start with the command file input or start with system.in as an input.


TESTS
For testing MVC, we used the images provided by in the starter codes, which is the Koala.ppm and other png files. We first convert all png files into ppm format by using GIMP image processor. During the test, I load all the correct images and compare them with the images produced by our testing image model/controller. Then I repeat the test for other image format such as png, jpg and bmp. For the sake of the limited size of files to hand in, we did not include those testing images and converted files.


CITATION FOR THE IMAGE
The image demon-head.jpg is owned by the group member Bitan Wu. He has authorized our use of the picture and the derivative ppm files in our project.








-----------------------------------------------------------
Updated 2021/11/02
-----------------------------------------------------------

CODE STRUCTURE
The codes have five parts:

[Model]
The model controls the logic and functions of the image processor. It provides different ways to process the image, including saving, loading and modifying images. It also contains a list of images uploaded by the user in the buffer, waiting to be saved into file system. All function that modify images return a ProcessResult enum, which indicate the result of running the function with the given arguments. There are many types of result, such as SUCCESS, FILE_NOT_FOUND, OVERRIDE_IMAGE. All ProcessResult contains a message, which can be easily accessed by the controller and be printed out.
Currently there is only PPM image processor which process .ppm files, but it can be expanded to other image processer such as PNG image processor. 

[View]
The view prints out the messages to user and display the images in a processor. Since we don't need to actually display the image using a gui, current it is only a test-based view, printing out some messages, the image's name, width and height to an appendable.

[Controller]
The controller contain a model and a view. It gives user a way to interact with the model, and use the view to display the images in the model. 
Currently there is only a text-based controller which read string inputs from readable and write string outputs 

[Image]
The interface abstractImage indicates different types of images. It stores the information of the image. Currently we are only using PPD, but later it can be used for other type of image.

[Command]
The commands are used by the controller for converting user input into function calls. The controller uses different command depends on the first user input token in a line. All command needs to be constructed with a view and a model, which can be obtained easily in the controller; and has a function process(String[]), which takes in the remaining user input (mostly the config of the process) and then call the according methods in the model or view according to the config. 
All process(String[]) function also usually pass the ProcessResult from the model to the controller (and sometimes throws own ProcessResults like NOT_ENOUGH_COMMAND_ELEMENT according to the inputs), so that the controller can access the result of the processing and print out the message according to the result.


COMMANDS
To use the PPM image processor in a console, type in the command after the system prompts you "Please enter command:"
Please enter the command in a whole line, that is, finish the command line before pressing ENTER / a command cannot be seperated into two lines. Command keywords are case sensitive, no upper-case letters in all keywords of commands.
There are seven types of command:
- load [location of the file to load] [name of the image to be stored in the processor]
	- save the given image from the processor to the file system.
- save [location of the file to save] [name of the image that is stored in the processor]
	- load the given image from the file system to the processor
- greyscale [operation type] [name of the original image] [name of the created image] 
	- There are different opeation types:
		red-component : create a greyscale image with the red-component of the original image
		green-component : create a greyscale image with the green-component of the original image
		blue-component : create a greyscale image with the blue-component of the original image
		value-component : create a greyscale image with the value-component of the original image
		intensity-component : create a greyscale image with the intensity-component of the original image
		luma-component : create a greyscale image with the luma-component of the original image
- flip vertical/horizontal [name of the original image] [name of the created image]
	- create a new image with all pixel of the original image flipped vertically or horizontally
- brighten [value of brightness to increase or decrease] [name of the original image] [name of the created image]
	- negative value indicates darkening the image.
	- positive value indicates brightening the image.
- copy [name of the original image] [name of the created image]
	- copy the original image, and store a clone in the processor with the given name.
- print [name of the image]/all
	- print the image if the second argument is the name of the image.
	- print all images if the second argument is "all".

Here are some example commands:
load Koala.ppm koala
greyscale red-component koala koala-red
greyscale luma-component koala koala-luma
flip vertical koala koala-vertical-flip
flip horizontal koala koala-horizontal-flip
brighten 50 koala koala-brighten
brighten -50 koala koala-darken
copy koala koala-copy
print koala-brighten
print all
save Koala-HP.ppm koala-horizontal-flip
save Koala2.ppm koala-copy


TESTS
For testing MVC, we used the images provided by in the starter codes, which is the Koala.ppm and other png files. We first convert all png files into ppm format by using GIMP image processor. During the test, I load all the correct images and compare them with the images produced by our testing image model/controller. However, for the sake of the limited size of files to hand in, we did not include those testing images and converted ppm files.


CITATION FOR THE IMAGE
The image demon-head.jpg is owned by the group member Bitan Wu. He has authorized our use of the picture and the derivative ppm files in our project.