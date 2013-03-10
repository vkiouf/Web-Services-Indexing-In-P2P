import java.util.List;


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
           Sum += Math.pow((array1[i]-array2[i]),2.0);
        }
        distance = Math.sqrt(Sum);
		
		return distance;
	}
	
	/**
	 * Find maximum value in a vector of numeric elements
	 * @param elements Element of any numeric type
	 * @return Maximum value
	 */
	public static int getMax(List<Integer> elements)
	{
		// Sort in ascending order and get last element
		java.util.Collections.sort(elements);
		return elements.get(elements.size()-1);
	}
}
