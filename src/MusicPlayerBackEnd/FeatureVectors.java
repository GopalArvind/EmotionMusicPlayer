package MusicPlayerBackEnd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class FeatureVectors implements Serializable {
	private double[][] leftEye, rightEye;	//Eigen vectors
	private double[][] nose, mouth;
	private double[][] mouthNose;
	private String emotion;
	
	private int objCount = 0;
	
	public FeatureVectors(String emotion) {
		this.emotion = emotion;
		File file = new File(FeatureExtractor.vectorsFileLoc);
		objCount = file.list().length;
	}
	
	public void setVectors(String feature, double[][] eigenVectors) {
		if(feature.equalsIgnoreCase("lefteye.png")) {
			leftEye = eigenVectors;
		}
		else if(feature.equalsIgnoreCase("righteye.png")) {
			rightEye = eigenVectors;
		}
		else if(feature.equalsIgnoreCase("nose.png")) {
			nose = eigenVectors;
		}
		else if(feature.equalsIgnoreCase("mouth.png")) {
			mouth = eigenVectors;
		}
		else if(feature.equalsIgnoreCase("mouthnose.png")) {
			mouthNose = eigenVectors;
		}
	}
	
	//Can remove separate setter methods.
/*	public void setLetEye(double[][] leftEye) {
		this.leftEye = leftEye;
	}
	
	public void setRightEye(double[][] rightEye) {
		this.rightEye = rightEye;
	}
	
	public void setNose(double[][] nose) {
		this.nose = nose;
	}
	
	public void setMouth(double[][] mouth) {
		this.mouth = mouth;
	}
	
	public void setNoseMouth(double[][] mouthNose) {
		this.mouthNose = mouthNose;
	}
*/	
	public double[][] getLeftEye() {
		return leftEye;
	}
	
	public double[][] getRightEye() {
		return rightEye;
	}
	
	public double[][] getNose() {
		return nose;
	}
	
	public double[][] getMouth() {
		return mouth;
	}
	
	public double[][] getMouthNose() {
		return mouthNose;
	}
	
	public String getEmotion() {
		return emotion;
	}
	
	public void saveObject() {
		try {
//			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FeatureExtractor.vectorsFileLoc + emotion + "/" + objCount + ".data"));
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FeatureExtractor.vectorsFileLoc + objCount + ".data"));
			oos.writeObject(this);
			oos.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static FeatureVectors loadObject(File file) {
		FeatureVectors objToReturn = null;
		
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			objToReturn = (FeatureVectors) ois.readObject();
			ois.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return objToReturn;
	} 
}
