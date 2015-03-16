




import Jama.Matrix;
import Jama.EigenvalueDecomposition;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.opencv.core.Core;
import org.opencv.highgui.Highgui;



/*
import org.apache.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.linear.realMatrix;
import org.apache.commons.math.linear.BlockRealMatrix;
import org.apache.commons.math.stat.descriptive.moment.Mean;
import org.apache.commons.math.stat.descriptive.moment.Variance;*/
import static org.bytedeco.javacpp.opencv_highgui.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_highgui.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgproc.cvResize;
import static org.bytedeco.javacpp.opencv_core.*;

public class EigenVector{
	
	public static double[][] getEigen(double[][] covariance,int cols) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		
		//IplImage img = cvLoadImage("C:\\Users\\skk\\Pictures\\karthik.jpg",CV_LOAD_IMAGE_GRAYSCALE);
		//IplImage des = cvCreateImage(cvSize(50,50),IPL_DEPTH_8U,img.nChannels());
		//org.opencv.core.Mat image=Highgui.imread("D:\\Test images\\1\\3.png",CV_LOAD_IMAGE_GRAYSCALE);
		//cvResize(img,des);
		/*int r=image.height();
		int c=image.width();
		System.out.println(" "+r +" "+c);
		double temp[][][] = new double[r][][];
		int i,j;
		for(i=0;i<r;i++)
		{
			temp[i] = new double[c][];
			for(j=0;j<c;j++)
			{
				temp[i][j] = new double[1];
			}
		}
		
		
		for(i=0;i<r;i++)
		{
			for(j=0;j<c;j++)
			{
					temp[i][j] = image.get(i,j);
					//System.out.print(" "+temp[i][j][0]);
			}
			System.out.println();
		}
		double temp2[][] =  new double[r][];
		for(i=0;i<r;i++)
		{
			temp2[i] = new double[c];
		}
		for(i=0;i<r;i++)
		{
			for(j=0;j<c;j++)
			{
				temp2[i][j] = temp[i][j][0];	
			}
		}*/
		Matrix A=new Matrix(covariance);		
		System.out.println(" "+ A.getRowDimension() +" "+ A.getColumnDimension());
		A=A.transpose().times(A);
		EigenvalueDecomposition e=A.eig();
		Matrix V=e.getV();
		Matrix D=e.getD();
		
		System.out.print("A=");
		A.print(9, 6);
		System.out.print("D");
		D.print(9, 6);
		System.out.println("D size "+ D.getRowDimension()+" "+D.getColumnDimension());
		System.out.print("V");
		V.print(9, 6);
		//System.out.println("V size "+ V.getRowDimension()+" "+V.getColumnDimension());
		//System.out.println(V.times(V.transpose()).minus(Matrix.identity(cols,cols)).normInf());
		//System.out.println("av=dv");
		
		//System.out.println(A.times(V).minus(V.times(D)).normInf()); 
		return(V.getArray());
	}
}
