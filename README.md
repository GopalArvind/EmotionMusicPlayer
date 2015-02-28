Emotion Based Music Player

This project is a basic implementation of a music player which builds a playlist for the users based on their emotion at the time.
The emotion detection is done by extracting certain features from the facial image of the user, the details of which will be explained as the project progresses.

How does the FeatureExtractor work?

FeatureExtractor is the class which is used to get all the features required for the emotion detection.
First, an array is initialized to store all the classifier xml files and the dimensions of the images of the features. The start method is where the class starts doing its stuff.
The test images used in this project are stored in the location specified in the String testImagesLoc. While the features are extracted, they're also temporarily cached, and these are stored in the location specified in cacheLoc.

First of all, the opencv library is loaded in the start method. Then a test image is loaded, and the features are extracted from this image.
Here, first the face is extracted from the image, and then the features are extracted from that facial image.
The detectFeatures method is used to obtain all the features that are detected based on the classifier used. It also temporarily saves the features extracted in the cache directory.
The extractFeature method is used to actually extract the features based on all the detections. It is a recursive method, and the integer counter is used to differentiate between the code which deals with extraction of face, and extraction of features from a single face.
So, -1 is used for extraction of faces from an image. When the counter is greater than -1, it is only for extraction of features from a single face.

So, basically, a face is extracted from an image, and the different classifiers are used on that particular face to get the various features out of it.

The code will surely be prone to a LOT of changes in the future, but I feel this is a good start.

If you have any bugs or issues, just raise a bug in the git repository. This will make it easier for us to track the bugs and fix them.

More details will be added to this readme file (and some, removed from the file) as the project progresses.

P. S. You will need JavaCV and OpenCV 2.4 to run this project.