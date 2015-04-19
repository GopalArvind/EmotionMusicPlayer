package MusicPlayerBackEnd;
import org.bytedeco.javacpp.opencv_core.IplImage;

import static org.bytedeco.javacpp.opencv_core.cvFlip;
import static org.bytedeco.javacpp.opencv_highgui.cvSaveImage;

import org.bytedeco.javacv.*;

public class CaptureImage {
	public static boolean executing = true;
	
    public static void captureFrame() {
        // 0-default camera, 1 - next...so on
    	System.out.println("In Capture Frame function:");
        final OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        boolean done =false;
        while(!done)
        {
        	try {
        		grabber.start();
        		IplImage img = grabber.grab();
        		if (img != null) {
        			System.out.println("Image captured");
        			cvFlip(img,img,1);
        				if(cvSaveImage(FeatureExtractor.testImagesLoc + "capture.jpg", img) == 1) {
        					System.out.println("Image saved");
        					done = true;
        				} else {
        					System.out.println("Image not saved");
        				}
        		}
        		grabber.stop();
        	} catch (Exception e) {
            e.printStackTrace();
        	}
        }
    }
    
    public static void timer() {
    	TrainingAutomation.loadVectorsList();
    	EmotionDetector ed = new EmotionDetector();
    	
    	while(executing) {
    		
    		try {
				Thread.sleep(1000 * 60*3);
				if(executing)
				{
				   captureFrame();
	    		   ed.start(true);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		
    	}
    }
        
    public static void main(String[] args) {
        timer();
    }
}

