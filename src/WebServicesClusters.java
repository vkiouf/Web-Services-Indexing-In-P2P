import java.io.*;
import java.util.*;

import com.aliasi.cluster.*;
import javax.persistence.*;

@Entity
public class WebServicesClusters
{
	/*=========================================================================
	 *					Fields
	 *=========================================================================*/
	
	String algorithm;		// clustering algorithm label
	String repository;			// data source ( name of web service repository)
	double threshold;
	int number;
	List<WebServicesCluster> clusters;
	double precision;
	double recall;
	double fMeasure;

	transient ClusterScore<String> clusterScore;	// cluster score instance
	transient Set<Set<String>> reference;		// input clusters
	private transient Set<Set<String>> response= new HashSet<Set<String>>();	// clusters documents as sets
	
	/*=========================================================================
	 *					Constructors
	 *=========================================================================*/
	
	/**
	 * @param threshold
	 * @param number
	 * @param cluster
	 * @param precision
	 * @param recall
	 */
	public WebServicesClusters(double threshold, int number,
			List<WebServicesCluster> clusters, double precision, double recall,double fMeasure,String algorithm,
			String repository)
	{
		this.threshold = threshold;
		this.number = number;
		this.clusters = clusters;
		this.algorithm = algorithm;
		this.repository = repository;
		
		prepare();
		
		this.precision = precision;
		this.recall = recall;
		this.fMeasure = fMeasure;
	}

	/**
	 * @param threshold
	 * @param number
	 * @param cluster
	 */
	public WebServicesClusters(double threshold, int number,
			List<WebServicesCluster> clusters,String algorithm,
			String repository)
	{
		this.threshold = threshold;
		this.number = number;
		this.clusters = new ArrayList<WebServicesCluster>(clusters.size());
		this.algorithm = algorithm;
		this.repository = repository;
		
		for(WebServicesCluster cluster:clusters)
			this.clusters.add(new WebServicesCluster(cluster.getIndex(),cluster.getDocuments(),cluster.getDocumentNames()));
		
		prepare();
		computePrecision();
		computeRecall();
		computeFMeasure();
	}
	
	/*=========================================================================
	 *					Getters / Setters
	 *=========================================================================*/
	
	/**
	 * @return the threshold
	 */
	public double getThreshold()
	{
		return threshold;
	}

	/**
	 * @param threshold the threshold to set
	 */
	public void setThreshold(double threshold)
	{
		this.threshold = threshold;
	}

	/**
	 * @return the number
	 */
	public int getNumber()
	{
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(int number)
	{
		this.number = number;
	}

	/**
	 * @return the cluster
	 */
	public List<WebServicesCluster> getCluster()
	{
		return clusters;
	}

	/**
	 * @param cluster the cluster to set
	 */
	public void setCluster(List<WebServicesCluster> clusters)
	{
		this.clusters = clusters;
	}

	/**
	 * @return the precision
	 */
	public double getPrecision()
	{
		return precision;
	}

	/**
	 * @param precision the precision to set
	 */
	public void setPrecision(double precision)
	{
		this.precision = precision;
	}

	/**
	 * @return the recall
	 */
	public double getRecall()
	{
		return recall;
	}

	/**
	 * @param recall the recall to set
	 */
	public void setRecall(double recall)
	{
		this.recall = recall;
	}
	
	/**
	 * @return the fMeasure
	 */
	public double getfMeasure()
	{
		return fMeasure;
	}

	/**
	 * @param fMeasure the fMeasure to set
	 */
	public void setfMeasure(double fMeasure)
	{
		this.fMeasure = fMeasure;
	}
	
	/**
	 * @return the algorithm
	 */
	public String getAlgorithm()
	{
		return algorithm;
	}

	/**
	 * @param algorithm the algorithm to set
	 */
	public void setAlgorithm(String algorithm)
	{
		this.algorithm = algorithm;
	}

	/**
	 * @return the repository
	 */
	public String getRepository()
	{
		return repository;
	}

	/**
	 * @param repository the repository to set
	 */
	public void setRepository(String repository)
	{
		this.repository = repository;
	}

	/*=========================================================================
	 *					Methods
	 *=========================================================================*/
	
	/**
	 * Create reference and response clusters list 
	 */
	private void prepare()
	{
		WSDLCategory category = new WSDLCategory(repository);	// get documents categories from selected source repository
		Set<Set<String>> reference = category.getCategoriesSet();	
		
		for(WebServicesCluster cluster:clusters)
			response.add(new HashSet<String>(cluster.getDocumentNames()));
		
		clusterScore = new ClusterScore<String>(reference,response);
	}
	
	/**
	 * Compute precision of clustering
	 */
	public void computePrecision()
	{
		precision = clusterScore.equivalenceEvaluation().precision();
	}
	
	/**
	 * Compute recall of clustering
	 */
	public void computeRecall()
	{	
		recall = clusterScore.equivalenceEvaluation().recall();
	}
	
	/**
	 * Compute f measure of clustering 
	 */
	public void computeFMeasure()
	{
		fMeasure = clusterScore.equivalenceEvaluation().fMeasure();
	}	
}
