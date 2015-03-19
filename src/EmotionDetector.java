import java.io.Serializable;

import org.opencv.core.Core;


public class EmotionDetector {
	private static String mode = "Testing";
	private String emotionOfFace;	//Will be set after calculation of eigen vectors and comparisons.
	
	public String imagePath;	//User input
	public String emotion;		//User input
	
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		new EmotionDetector().start();
	}
	
	public void setEmotion(String emotion) {
		emotionOfFace = emotion;
	}
	
	public void start() {
		getInput();
		FeatureExtractor featureExtractor = new FeatureExtractor();
		featureExtractor.start(imagePath);
		Covariance covariance = new Covariance();
		
		if(mode.equalsIgnoreCase("training")) {	//Training mode.	
			covariance.trainSystem(emotion);
		}
		else {	//Emotion Detection mode.
			emotionOfFace = covariance.detectEmotion();
//			covariance.getMeanSubtractedImage(imagePath, "");
			System.out.println("The emotion is: " + emotionOfFace);
		}
	}
	
	public void getInput() {
		//Set user inputs.
		imagePath = FeatureExtractor.testImagesLoc + "WP_20150124_13_35_53_Pro.jpg";
		emotion = "Sad";
	}
}
