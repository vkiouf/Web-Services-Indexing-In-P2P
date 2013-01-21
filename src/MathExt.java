/**
 * 
 */

/**
 * Contains extra mathematician functions
 */
public class MathExt
{

	/**
	 * Compute euclidean distance between two arrays of same length
	 * @param array1 
	 * @param array2
	 * @return Euclidean distance
	 */
	public static double euclideanDistance(double[] array1, double[] array2)
	{
		double distance=0.0;
		double Sum = 0.0;
		
		if(array1.length!=array2.length)
			throw new IllegalArgumentException("Arrays are not the same length");
		
        for(int i=0;i<array1.length;i++) {
           Sum = Sum + Math.pow((array1[i]-array2[i]),2.0);
        }
        distance = Math.sqrt(Sum);
		
		return distance;
	}
}
