
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.core.Core;



public class Covariance {
Mat image;
int rows,cols;
double meanSubImage[][];
double covariance[][] ;
double columnMean[];
	public Covariance() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		// TODO Auto-generated constructor stub
		image = Highgui.imread("C:\\Users\\gopal\\Desktop\\Test images\\1\\1.png");
		getMeanSubtractedImage("happy");
		
	}

	private void getMeanSubtractedImage(String emotion) {
		// TODO Auto-generated method stub
		System.out.println("in getMeanSubtractedImage");
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
				System.out.print((int)(covariance[i][j] = getCovariance(i,j)) + " ");
			}
			System.out.println();
		}
		System.out.println("covariance claculated");
		double eigen[][]=EigenVector.getEigen(covariance,cols);
		//FROM EIGEN GET 5 SIGNIFICANT VECTORS AND STORE THEM IN A FILE. FOR EACH EMOTION STORE THE EIGEN VECTORS IN A FILE.
		
		
		//Make an instance of FeatureVectors and call save method on it.
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Covariance test = new Covariance();
	}

}
