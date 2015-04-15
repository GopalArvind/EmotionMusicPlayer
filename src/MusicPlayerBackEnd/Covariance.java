package MusicPlayerBackEnd;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;



public class Covariance {
	Mat image;
	int rows,cols;
	double meanSubImage[][];
	double covariance[][] ;
	double columnMean[];
	public HashMap<String, Integer> emotionCount;
	public HashMap<FeatureVectors, ArrayList<double[]>> distanceMap;
	
	public Covariance() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		// TODO Auto-generated constructor stub
//		image = Highgui.imread("C:\\Users\\gopal\\Desktop\\Test images\\1\\1.png");
//		getMeanSubtractedImage();
	}
	
	public void trainSystem(String emotion) {
		File featuresFile = new File(FeatureExtractor.outputLoc);
		double[][] eigenVectors;
		FeatureVectors featureVectors = new FeatureVectors(emotion);
		
		for(String feature: featuresFile.list()) {
			eigenVectors = getMeanSubtractedImage(FeatureExtractor.outputLoc + feature);
			featureVectors.setVectors(feature, eigenVectors);
		}
		
		featureVectors.saveObject();
	}
	
	public String detectEmotion() {	//To be implemented.
		File featuresFile = new File(FeatureExtractor.outputLoc);
		File vectorFilesLoc = new File(FeatureExtractor.vectorsFileLoc);
		
		double[][] eigenVectors;
		FeatureVectors testImage = new FeatureVectors("");
		FeatureVectors trainedImage;
		
		for(String feature: featuresFile.list()) {
			eigenVectors = getMeanSubtractedImage(FeatureExtractor.outputLoc + feature);
			testImage.setVectors(feature, eigenVectors);
		}
		
		emotionCount = new HashMap<String, Integer>();
		for(String emotion: FeatureExtractor.emotions) {
			emotionCount.put(emotion, 0);
		}
//		emotionCount.put("Happy", 0);
//		emotionCount.put("Sad", 0);
//		emotionCount.put("Neutral", 0);
//		emotionCount.put("Anger", 0);
		distanceMap = new HashMap<FeatureVectors, ArrayList<double[]>>();
		String finalEmotion = "";
		
		try {
			for(File file: vectorFilesLoc.listFiles()) {
				trainedImage = (FeatureVectors) FeatureVectors.loadObject(file);
				compare(testImage, trainedImage);
			}
			
			findLeast();
			
			int max = 0;
			for(String emotion: emotionCount.keySet()) {
				if(emotionCount.get(emotion) > max) {
					max = emotionCount.get(emotion);
					finalEmotion = emotion;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		for(String emotion: FeatureExtractor.emotions) {
			System.out.println(emotion + " = " + emotionCount.get(emotion));
		}
//		System.out.println("happy = " + emotionCount.get("Happy"));
//		System.out.println("sad = " + emotionCount.get("Sad"));
//		System.out.println("neutral = " + emotionCount.get("Neutral"));
//		System.out.println("anger = " + emotionCount.get("Anger"));
		return finalEmotion;
	}
	
	public void findLeast() {
		String emotion = ""; 
/*		for(int i = 0; i < 4; i++) {	//(5) For each feature!
			double min = Double.MAX_VALUE - 2;
			for(FeatureVectors vectorFile: distanceMap.keySet()) {
				//Getting distance vectors of each feature now.
				ArrayList<double[]> distanceVectors = distanceMap.get(vectorFile);	//2d cuz a 5-value vector for each feature!!
				for(int j = 0; j < distanceVectors.size(); j++) {	//For each value in a feature.
					
				}
				if(distances.get(i) <= min) {
					min = distances.get(i);
					emotion = vectors.getEmotion();
					int count = emotionCount.get(emotion);
					count++;
					emotionCount.put(emotion, count);	//Incrementing emotion count for an emotion.
				}
			}
		}
	*/	
		for(int i = 0; i < 4; i++) {	//For each feature
			for(int j = 0; j < 5; j++) {	//For each value
				double min = Double.MAX_VALUE - 2;
				for(FeatureVectors vectorFile: distanceMap.keySet()) {	//For each file
					ArrayList<double[]> distanceVectors = distanceMap.get(vectorFile);
					double[] singleVector = distanceVectors.get(i);		//A single vector for a feature
					if(singleVector[j] < min) {
						min = singleVector[j];
						emotion = vectorFile.getEmotion();
					}
				}
				int count = emotionCount.get(emotion);
				count += 1;	//Increasing count for an emotion
				emotionCount.put(emotion, count);
			}
		}
	}
	
	public void compare(FeatureVectors testImage, FeatureVectors trainedImage) {
		//Left eye
		double[][] testImageVectors = testImage.getLeftEye();
		double[][] trainedImageVectors = trainedImage.getLeftEye();
		ArrayList<double[]> distanceVectors = new ArrayList<double[]>(); 
		double[] distanceVector = getDistanceVector(testImageVectors, trainedImageVectors);
		distanceVectors.add(distanceVector);
		
		//Right eye
		testImageVectors = testImage.getRightEye();
		trainedImageVectors = trainedImage.getRightEye();
		distanceVector = getDistanceVector(testImageVectors, trainedImageVectors);
		distanceVectors.add(distanceVector);
		
		//Nose
		testImageVectors = testImage.getNose();
		trainedImageVectors = trainedImage.getNose();
		distanceVector = getDistanceVector(testImageVectors, trainedImageVectors);
		distanceVectors.add(distanceVector);
		
		//Mouth
		testImageVectors = testImage.getMouth();
		trainedImageVectors = trainedImage.getMouth();
		distanceVector = getDistanceVector(testImageVectors, trainedImageVectors);
		distanceVectors.add(distanceVector);
		System.out.println();//debug
		
		//MouthNose
		testImageVectors = testImage.getMouthNose();
		trainedImageVectors = trainedImage.getMouthNose();
		distanceVector = getDistanceVector(testImageVectors, trainedImageVectors);
		distanceVectors.add(distanceVector);
		
		distanceMap.put(trainedImage, distanceVectors);
	} 
	
	public double[] getDistanceVector2(double[][] testImageVectors, double[][] trainedImageVectors) {
		double squaredSum = 0;
		double[] distanceVector = new double[testImageVectors.length];
		
		for(int i = 0; i < testImageVectors.length; i++) {
			for(int j = 0; j < testImageVectors[i].length; j++) {
	//			System.out.println(testImageVectors[i][j] + "\t" + trainedImageVectors[i][j] + "\t" + (testImageVectors[i][j] - trainedImageVectors[i][j]));
//				squaredSum += Math.pow((testImageVectors[i][j] - trainedImageVectors[i][j]), 2);
				//T1 is trainedImageVectors[i], H1 is testImageVectors[i]
				distanceVector[i] += Math.abs((testImageVectors[i][j] - trainedImageVectors[i][j]))/(testImageVectors[i][j] + trainedImageVectors[i][j]);
			}
//			distanceVector[i] = Math.sqrt(distanceVector[i]);
		}
		
		for(int i = 0; i < distanceVector.length; i++)
			System.out.print(distanceVector[i] + "\t");
		System.out.println();
		
		return distanceVector;
	}
	
	public double[] getDistanceVector(double[][] testImageVectors, double[][] trainedImageVectors) {
		double squaredSum = 0;
		double[] distanceVector = new double[testImageVectors.length];
		
		for(int i = 0; i < testImageVectors.length; i++) {
			for(int j = 0; j < testImageVectors[i].length; j++) {
	//			System.out.println(testImageVectors[i][j] + "\t" + trainedImageVectors[i][j] + "\t" + (testImageVectors[i][j] - trainedImageVectors[i][j]));
//				squaredSum += Math.pow((testImageVectors[i][j] - trainedImageVectors[i][j]), 2);
				//T1 is trainedImageVectors[i], H1 is testImageVectors[i]
				distanceVector[i] += Math.pow((testImageVectors[i][j] - trainedImageVectors[i][j]), 2);
			}
			distanceVector[i] = Math.sqrt(distanceVector[i]);
		}
		
		for(int i = 0; i < distanceVector.length; i++)
			System.out.print(distanceVector[i] + "\t");
		System.out.println();
		
		return distanceVector;
	}

	private double[][] getMeanSubtractedImage(String imagePath) {
		System.out.println("in getMeanSubtractedImage");
		
		image = Highgui.imread(imagePath);	//Change
		
		double sum = 0;
		double avg ;
		int i,j;
		rows = image.rows();
		cols = image.cols();
		System.out.println("image size" +rows+" "+cols);
		double cache[] = new double[1];
		for(i=0;i<rows;i++)
		{
			for(j=0;j<cols;j++)
			{
				cache = image.get(i, j);
				sum += cache[0];
			}
		}
		avg = sum/(rows*cols);
		System.out.println("mean calculated");
	    meanSubImage = new double[rows][];
		for(i=0;i<rows;i++)
		{
			meanSubImage[i] = new double[cols];
		}
		for(i=0;i<rows;i++)
		{
			for(j=0;j<cols;j++)
			{
				cache = image.get(i, j);
				meanSubImage[i][j] = cache[0] - avg;
			}
		}
		System.out.println("mean subtracted image calculated");
		getColumnMean(cols,rows);
		System.out.println("column mean calculated");
		 covariance = new double[cols][cols];
		for(i=0;i<cols;i++)
		{
			covariance[i] = new double[cols];
		}
		for(i=0;i<cols;i++)
		{
			for(j=0;j<cols;j++)
			{
//				System.out.print((int)(covariance[i][j] = getCovariance(i,j)) + " ");
				covariance[i][j] = getCovariance(i,j);
				
			}
//			System.out.println();
		}
		System.out.println("covariance claculated");
		double eigen[][]=EigenVector.getEigen(covariance,cols);
		//FROM EIGEN GET 5 SIGNIFICANT VECTORS AND STORE THEM IN A FILE. FOR EACH EMOTION STORE THE EIGEN VECTORS IN A FILE.
//		int index = cols/5;
		double finalEigen[][] = new double[5][cols];
		for(i=0;i<5;i++)
		{
			finalEigen[i] = new double[cols];
		}
		for(i=0,j=0;j < 5;i++,j++)
		{
			finalEigen[j] = eigen[i];
		}
		
		return finalEigen;
	}

	private void getColumnMean(int cols,int rows) {
		// TODO Auto-generated method stub
		System.out.println("in getColumnMean");
		columnMean = new double[cols];
		int i,j;
		for(i=0;i<cols;i++)
		{
			columnMean[i] = 0;
			for(j=0;j<rows;j++)
			{
				columnMean[i] += meanSubImage[j][i]; 
			}
			columnMean[i] = columnMean[i]/rows;
		}
	}

	private double getCovariance(final int i,final int j) {
		// TODO Auto-generated method stub
		//System.out.println("in getCovariance");
		double covariance = 0;
		int k,l;
		if(i==j)
		{
			for(k=0;k<rows;k++)
			{
				covariance += (meanSubImage[k][i] - columnMean[i])*(meanSubImage[k][i] - columnMean[i]);  
			}
		}
		else
		{
			for(k=0;k<rows;k++)
			{
				covariance += (meanSubImage[k][i] - columnMean[i])*(meanSubImage[k][j] - columnMean[j]);
			}
		}
		covariance = covariance/(rows-1);
		return covariance;
	}
}
