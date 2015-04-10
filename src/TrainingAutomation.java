import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;

import org.opencv.core.Core;


public class TrainingAutomation {
	private String datasetFilename;
	private boolean training = false;
	private HashMap<String, String> imageEmotionMap, resultMap;
	private int pass = 0, fail = 0;
	PrintWriter err;
	
	public TrainingAutomation() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		imageEmotionMap = new HashMap<String, String>();
		resultMap = new HashMap<String, String>(); 
		
		if(training) {
			datasetFilename = "TrainingDataset.txt";
		}
		else {
			datasetFilename = "TestingDataset.txt";
		}
	}
	
	public static void main(String[] args) {
		try {
			new TrainingAutomation().startAutomation();
		} catch(Exception e) {e.printStackTrace();}
	}
	
	public void startAutomation() throws Exception {
		System.out.println("Starting automation.");
		readDataset();
		
		err = new PrintWriter("ErrorLog.txt");
		long startTime = System.currentTimeMillis();
		for(String image: imageEmotionMap.keySet()) {
			String imagePath = FeatureExtractor.testImagesLoc + image;
			if(!new File(imagePath).exists()) {
				System.out.println(imagePath + " doesn't exist.");
				fail++;
				err.println(imagePath + " doesn't exist.");
				err.flush();
				continue;
			}
			FeatureExtractor featureExtractor = new FeatureExtractor();
			System.out.println(image);
			boolean valid = featureExtractor.start(imagePath);
			if(!valid) {	//If all the features are not detected.
	//			System.out.println("Didn't detect all features. Hence skipping this image.");
				File file = new File(imagePath);
				File destFile = new File(imagePath.substring(0, imagePath.indexOf('.')) + "_invalid.png");
				file.renameTo(destFile);
				resultMap.put(image, "Fail");
				fail++;
				err.println(image + " : Invalid image.");
				err.flush();
				continue;
			}
				
			Covariance covariance = new Covariance();
			String emotionOfFace;
			if(training) {
				covariance.trainSystem(imageEmotionMap.get(image));
				emotionOfFace = "Pass_" + imageEmotionMap.get(image);
				pass++;
			}
			else {
				emotionOfFace = covariance.detectEmotion();
				if(emotionOfFace.equalsIgnoreCase(imageEmotionMap.get(image))) {
					emotionOfFace = "Pass_" + emotionOfFace;
					pass++;
				}
				else {
					emotionOfFace = "Fail_" + emotionOfFace;
					fail++;
				}
			}
			resultMap.put(image, emotionOfFace);
		}
		long endTime = System.currentTimeMillis();
		System.out.println("\n\nTime taken = " + ((endTime - startTime)/1000) + " sec");
		saveTestResult();
		System.out.println("Pass = " + pass + "\tFail = " + fail);
	
		System.out.println("\n\nEnd of automation.");
	}
	
	public void saveTestResult() throws Exception {
		String saveFile;
		if(training) {
			saveFile = "Output_Training.txt";
		}
		else {
			saveFile = "Output_Testing.txt";
		}
		
		PrintWriter pw = new PrintWriter(saveFile);
		for(String image: resultMap.keySet()) {
			String output = image + "\t\t\t" + resultMap.get(image);
			pw.println(output);
			pw.flush();
		}
		pw.close();
	}
	
	public void readDataset() throws Exception {
		System.out.println("Reading dataset.");
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(datasetFilename)));
		
		String str = "";
		while((str = br.readLine()) != null) {
			String tokens[] = str.split("\\s+");
			imageEmotionMap.put(tokens[0], tokens[1]);
	//		imageEmotionMap.put("IMAGE", tokens[0]);
	//		imageEmotionMap.put("EMOTION", tokens[1]);
		}
		br.close();
	}
}
