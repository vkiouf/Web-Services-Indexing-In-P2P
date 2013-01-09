/**
 * 
 */

/**
 * Contains extra mathematician functions
 */
public class MathExt
{

	public static double euclideanDistance(double[] array1, double[] array2)
	{
		double distance=0.0;
		double Sum = 0.0;
		
        for(int i=0;i<array1.length;i++) {
           Sum = Sum + Math.pow((array1[i]-array2[i]),2.0);
        }
        distance = Math.sqrt(Sum);
		
		return distance;
	}
}
