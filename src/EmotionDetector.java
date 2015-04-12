import java.io.File;

import org.opencv.core.Core;


public class EmotionDetector {
	public static String mode = "Testing";
	private String emotionOfFace;	//Will be set after calculation of eigen vectors and comparisons.
	
	public static String imagePath;	//User input
	public static String emotion;		//User input
	GuiComponent gui = new GuiComponent();	//Remove in next phase.
	
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		new EmotionDetector().start(false);
	}
	
	public void setEmotion(String emotion) {
		emotionOfFace = emotion;
	}
	
	public void start(boolean cameraIntegration) {
		getInput(cameraIntegration);
		imagePath = FeatureExtractor.testImagesLoc + imagePath;
		if(!new File(imagePath).exists()) {
			System.out.println(imagePath + " doesn't exist.");
			return;
		}
		FeatureExtractor featureExtractor = new FeatureExtractor();
		System.out.println(imagePath);
		boolean valid = featureExtractor.start(imagePath);
		if(!valid) {	//If all the features are not detected.
			System.out.println("Didn't detect all features. Hence skipping this image.");
			File file = new File(imagePath);
			File destFile = new File(imagePath.substring(0, imagePath.indexOf('.')) + "_invalid.png");
			file.renameTo(destFile);
			return;
		}
		Covariance covariance = new Covariance();
		
		if(mode.equalsIgnoreCase("training")) {	//Training mode.	
			covariance.trainSystem(emotion);
		}
		else {	//Emotion Detection mode.
			emotionOfFace = covariance.detectEmotion();
//			covariance.getMeanSubtractedImage(imagePath, "");
			System.out.println("The emotion is: " + emotionOfFace);
		}
		
		if(!mode.equals("Training") && !cameraIntegration) {
			gui.showResult(emotionOfFace);
		}
	}
	
	public void getInput(boolean cameraIntegration) {
		//Set user inputs.
	//	imagePath = FeatureExtractor.testImagesLoc + "WP_20150124_13_35_53_Pro.jpg";
	//	emotion = "Sad";
	
		if(cameraIntegration) {
			imagePath = "capture.jpg";
			mode = "testing";
			return;
		}
		
		try {
			synchronized(gui) {
				gui.showGui();
				gui.wait();
			}
		} catch(Exception e) {e.printStackTrace();}
	}
}
