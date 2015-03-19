import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class FeatureVectors {
	private double[][] leftEye, rightEye;	//Eigen vectors
	private double[][] nose, mouth;
	private double[][] noseMouth;
	private String emotion;
	
	private static int objCount = -1;
	
	public FeatureVectors(String emotion) {
		this.emotion = emotion;
		objCount++;
	}
	
	public void setLetEye(double[][] leftEye) {
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
	
	public void setNoseMouth(double[][] noseMouth) {
		this.noseMouth = noseMouth;
	}
	
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
	
	public double[][] getNoseMouth() {
		return noseMouth;
	}
	
	public void saveObject() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FeatureExtractor.vectorsFileLoc + emotion + "/" + objCount + ".data"));
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
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return objToReturn;
	} 
}
