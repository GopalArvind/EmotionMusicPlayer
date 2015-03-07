/*
 * First get face.
 * Left eye, if more than one image, discard features not in 1st quadrant.
 * Do that for the rest.
 * For nose, use full face. Should be close to origin.
 * Mouth in third AND fourth quadrant!!
 * Don't try to pass single quadrant sub images, THEY DO NOT WORK!
 */




import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvSize;
import static org.bytedeco.javacpp.opencv_highgui.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_highgui.cvLoadImage;
import static org.bytedeco.javacpp.opencv_highgui.cvSaveImage;
import static org.bytedeco.javacpp.opencv_imgproc.cvResize;

import java.io.File;
import java.io.IOException;

import org.bytedeco.javacpp.opencv_core.CvSize;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;


public class FeatureExtractor {
	private final String testImagesLoc = "TestImages/";
	private final String cacheLoc = "Cache/";
	private final String outputLoc = "Output/";
	private final String classifierLoc = "C:\\Users\\Akash\\OpenCV\\opencv\\sources\\data\\haarcascades\\";
	private final String faceWithFeaturesFilename = cacheLoc + "faceWithFeatures.png";
	private final String faceClassifier = "haarcascade_frontalface_alt.xml";
	private final String leftEyeClassifier = "haarcascade_lefteye_2splits.xml";
	private final String rightEyeClassifier = "haarcascade_righteye_2splits.xml";
	private final String noseClassifier = "haarcascade_mcs_nose.xml";
	private final String mouthClassifier = "haarcascade_mcs_mouth.xml";
	private final String mouthNoseClassifier = "MouthNose.xml";
	
	private String[] classifierFiles;
	private CvSize[] dimensions;
	private Rect[] partsOfFace;
	private static int featureCount = 0;
	
	private Mat faceWithFeatures;
	
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
		
		partsOfFace = new Rect[4];
		
		createDirectory(testImagesLoc);
		createDirectory(cacheLoc);
		createDirectory(outputLoc);
	}
	
	public void createDirectory(String directory) {
		File dir = new File(directory);
		if(!dir.exists()) {
			dir.mkdir();
		}
	}
	
	public void start() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("Running FaceDetector");
        
        Mat image = Highgui.imread(testImagesLoc + "WP_20150124_13_35_53_Pro.jpg", CV_LOAD_IMAGE_GRAYSCALE);
        faceWithFeatures = image; 
        MatOfRect faceDetections = detectFeature(classifierLoc + faceClassifier, image);
        System.out.println(String.format("Detected %s faces.", faceDetections.toArray().length));
        
        extractFeatures(faceDetections, image, -1);		//Plan this. This method may have to return stuff in the future according to requirements.
       /*
     //REMOVE!!   
        Rect r = faceDetections.toArray()[0];
        Mat face = image.submat(r);
        System.out.println(String.format("Face width = %s, Face height = %s", face.width(), face.height()));
        partsOfFace[0] = new Rect(face.width()/2, 0, face.width()/2, face.height()/2);	//Left
        partsOfFace[1] = new Rect(0, 0, face.width()/2, face.height()/2);	//right
        partsOfFace[2] = new Rect(0, 0, face.width(), face.height()); //full face for nose.
        partsOfFace[3] = new Rect(0, face.height()/2, face.width(), face.height()/2);	//lower face
        
        detectFeature(leftEyeClassifier, face);
        */
 /*       int counter = 0;
        for(Rect rect: partsOfFace) {
        	String filename = counter + ".png";
        	Core.rectangle(
					face, 
					new Point(rect.x, rect.y), 
					new Point(rect.x + rect.width, rect.y + rect.height),
					new Scalar(0, 255, 0));	//Draws green rectangle.
        	
        	System.out.println(String.format("Writing feature to %s.", filename));
			Highgui.imwrite(filename, face);
//			detectFeature(classifierFiles[counter], face.submat(rect));
			counter++;
        }
   */     
        //REMOVE!!
	}
	
	public void extractFeatures(MatOfRect featureDetections, Mat image, int counter) {
		for(Rect rect: featureDetections.toArray()) {
			Mat subImage = image.submat(rect);
			
			if(counter < 0) {	//Face Detection
				faceWithFeatures = subImage.clone();	//Cloning to avoid having rectangles in cropped features.
				System.out.println("Cloning.");
				for(String classifier: classifierFiles) {
					System.out.println("Using classifier: " + classifier);
					//Add code to fix faulty classifiers here (mount, mouthNose)
					
					MatOfRect features = detectFeature(classifierLoc + classifier, subImage);
					counter++;
					
					extractFeatures(features, subImage, counter);	//Feature extracted is not face, cuz counter > 0. Functionality in next level of recursion, in else block.
				}
			}
			else {
				IplImage source = cvLoadImage(cacheLoc + (FeatureExtractor.featureCount - 1) + ".png");	//Cuz last image saved is the feature we are dealing with. RESIZING THE FEATURES!!
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
	
	public void saveFeatures(MatOfRect featureDetections, Mat image) {	//Must Change. :(
		System.out.println(String.format("Detected %s features.", featureDetections.toArray().length));
		
		for(Rect rect: featureDetections.toArray()) {
			String filename = cacheLoc + FeatureExtractor.featureCount + ".png";
			
			//Writing feature to a new file.
			Mat feature = image.submat(rect);
			System.out.println(String.format("Writing feature to %s.", filename));
			Highgui.imwrite(filename, feature);
		
			FeatureExtractor.featureCount++;
		}
		
		for(Rect rect: featureDetections.toArray()) {
			/* This must come after writing individual feature!
			 * Otherwise, it will have rectangles in the features!!
			 */
			Core.rectangle(
					faceWithFeatures, 
					new Point(rect.x, rect.y), 
					new Point(rect.x + rect.width, rect.y + rect.height),
					new Scalar(0, 255, 0));	//Draws green rectangle.
			
			//Writing all features to same face image.
			System.out.println(String.format("Writing feature to %s.", faceWithFeaturesFilename));
			Highgui.imwrite(faceWithFeaturesFilename, faceWithFeatures);
		}
		
//		FeatureExtractor.featureCount++;
	}
	
	public static void main(String[] args) {	//Temporary. Will be called by some code on server.
		new FeatureExtractor().start();
	}
}
