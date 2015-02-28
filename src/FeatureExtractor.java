import static org.bytedeco.javacpp.opencv_contrib.*;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import org.bytedeco.javacpp.opencv_core.IplImage;

import org.opencv.core.Core;
import org.opencv.core.MatOfRect;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;


public class FeatureExtractor {
	private String testImagesLoc = "TestImages/";
	private String cacheLoc = "Cache/";
	private String classifierLoc = "C:\\Users\\Akash\\OpenCV\\opencv\\sources\\data\\haarcascades\\";
	private String faceClassifier = "haarcascade_frontalface_alt.xml";
	private String leftEyeClassifier = "haarcascade_lefteye_2splits.xml";
	private String rightEyeClassifier = "haarcascade_righteye_2splits.xml";
	private String noseClassifier = "haarcascade_mcs_nose.xml";
	private String mouthClassifier = "haarcascade_mcs_mouth.xml";
	private String mouthNoseClassifier = "MouthNose.xml";
	private String[] classifierFiles;
	private CvSize[] dimensions;
	
	private static int featureCount = 0;
	
	public FeatureExtractor() {
		classifierFiles = new String[] {
				leftEyeClassifier, 
				rightEyeClassifier, 
				noseClassifier,
				mouthClassifier,	//Need to fix.
				mouthNoseClassifier};	//Need to fix.

		dimensions = new CvSize[] {
				cvSize(40, 40),	//left
				cvSize(40, 40),	//right
				cvSize(60, 70),	//nose
				cvSize(90, 60),	//mouth
				cvSize(95, 120)};	//noseMouth
	}
	
	public void start() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("Running FaceDetector");
        
        Mat image = Highgui.imread(testImagesLoc + "WP_20150124_13_35_53_Pro.jpg");
        MatOfRect faceDetections = detectFeature(classifierLoc + faceClassifier, image);
        System.out.println(String.format("Detected %s faces.", faceDetections.toArray().length));
        
        extractFeatures(faceDetections, image, -1);		//Plan this. This method may have to return stuff in the future according to requirements.
	}
	
	public void extractFeatures(MatOfRect featureDetections, Mat image, int counter) {
		for(Rect rect: featureDetections.toArray()) {
			Mat subImage = image.submat(rect);
			
			if(counter < 0) {	//Face Detection
				for(String classifier: classifierFiles) {
					System.out.println("Using classifier: " + classifier);
					//Add code to fix faulty classifiers here (mount, mouthNose)
					
					MatOfRect features = detectFeature(classifierLoc + classifier, subImage);
					counter++;
					extractFeatures(features, subImage, counter);	//Feature extracted is not face, cuz counter > 0. Functionality in next level of recursion, in else block.
				}
			}
			else {
				IplImage source = cvLoadImage(cacheLoc + (FeatureExtractor.featureCount - 1) + ".png");	//Cuz last image saved is the feature we are dealing with.
				IplImage destination = cvCreateImage(dimensions[counter], IPL_DEPTH_8U, source.nChannels());
				cvResize(source, destination);
				
				cvSaveImage("Output/" + classifierFiles[counter].substring(0, classifierFiles[counter].length() - 3) + "png", destination);
			}
		}
	}
	
	public MatOfRect detectFeature(String classifier, Mat image) {
		CascadeClassifier featureDetector = new CascadeClassifier(classifier);
		
		MatOfRect featureDetections = new MatOfRect();
		featureDetector.detectMultiScale(image, featureDetections);
		
		saveFeatures(featureDetections, image);
		
		return featureDetections;
	}
	
	public void saveFeatures(MatOfRect featureDetections, Mat image) {
		String filename = cacheLoc + FeatureExtractor.featureCount + ".png";
		
		for(Rect rect: featureDetections.toArray()) {
			Core.rectangle(
					image, 
					new Point(rect.x, rect.y), 
					new Point(rect.x + rect.width, rect.y + rect.height),
					new Scalar(0, 255, 0));	//Draws green rectangle.
			
			System.out.println(String.format("Writing feature to %s.", filename));
			Highgui.imwrite(filename, image);
		}
		
		FeatureExtractor.featureCount++;
	}
	
	public static void main(String[] args) {	//Temporary. Will be called by some code on server.
		new FeatureExtractor().start();
	}
}
