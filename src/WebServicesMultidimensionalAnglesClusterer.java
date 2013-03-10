import java.io.*;
import java.util.*;

import javax.persistence.TypedQuery;

import org.apache.commons.collections.map.*;

import com.google.common.collect.ArrayTable;

/**
 * Clusterer based on paper "Web Services Clustering using Multidimensional Angles as Proximity measures"
 */
public class WebServicesMultidimensionalAnglesClusterer extends WebServicesClusterer
{
	/*=========================================================================
	 *					Fields
	 *=========================================================================*/
	
	private MultiKeyMap decompressedMatrix;									// Decompressed matrix 
	private List<WebServicesCluster> _clusters;								// Final clusters of wsdl documents in current step
		
	private double minSimThreshold=0.4;										// Default value of similarity threshold under
																			// which reduction of decompressed matrix stops
	private double minNumClusters=20;										// Minimum number of clusters
	
	private boolean constructMatrix;				// True to construct matrix from beginning. False, to load decompressed matrix from database
	private boolean useFeaturesQF;					// True to use as similarity factor the quality factor of features clusterer
	
	private ArrayTable<String,String,Double> qualityFactor;	// Quality factor as result of features clusterer
	
	private String matrixF = "output/decompressed_matrix.txt";						// Decompressed matrix in tabular format in file
	private FileWriter fileWriter;											
	private BufferedWriter out;												
	
	
	/*=========================================================================
	 *					Constructors
	 *=========================================================================*/
	
	/**
	 * Constructs a web services clustering process in a collection of wsdl documents
	 * 
	 * @param WSDLDocuments A collection of wsdl documents
	 * @param minSimThreshold Similarity threshold
	 * @param minNumClustersThrehold A threshold to minimum number of clusters 
	 * @param constructMatrix True to construct matrix from beginning. False, to load decompressed matrix from database
	 * @param bool saveEndingClusters True to save only ending clusters
	 * @param bool useFeaturesQF True to use as similarity factor the quality factor of features clusterer
	 */
	public WebServicesMultidimensionalAnglesClusterer(WSDLDocumentCollection WSDLDocuments,double minSimThreshold,
			double minNumClustersThrehold,boolean constructMatrix,boolean saveEndingClusters,boolean useFeaturesQF)
	{
		super("MultidimensionalAngles",WSDLDocuments,"output/multidimensionalAngles_clusters",saveEndingClusters);
		
		// Set minimum threshold to terminate reduction of decompressed matrix
		this.useFeaturesQF = useFeaturesQF;
		this.minSimThreshold = minSimThreshold;
		this.minNumClusters = minNumClustersThrehold;
		this.constructMatrix = constructMatrix;
		
		// Create decompressed matrix
		int N= WSDLDocuments.getDocuments().size();
		decompressedMatrix =  MultiKeyMap.decorate(new HashedMap(N*N));
		
		// Write decompressed matrix file
		/*try
		{
			fileWriter = new FileWriter(matrixF);
			out = new BufferedWriter(fileWriter);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	protected void finalize() throws Throwable
	{
		//out.close();
		super.finalize(); //not necessary if extending Object.
	}
	
	/*=========================================================================
	 *					Getters
	 *=========================================================================*/
	
	
	/*=========================================================================
	 *					Setters
	 *=========================================================================*/
	
	/*=========================================================================
	 *					Methods
	 *=========================================================================*/
	
	/**
	 * Cluster documents following these steps:
	 * 1) Create an initial similarity decompressed matrix from weights of each pair of documents as computed in vector space model.
	 * 		These documents are single-element clusters
	 * 2) Search for maximum value(similarity) in decompressed matrix, maxSim
	 * 3) Find the pair of clusters with maximum similarity
	 * 4) Join the two clusters, compute their mean value and remove the second one from rows and columns of decompressed matrix
	 * 5) Proceed with step 2 until maximum value in matrix is below threshold or remains only one cluster
	 * 
	 * @param constructMatrix True to created  vector space model and construct decompressed matrix by reading documents \n
	 * 					False to load matrix directly from database
	 */
	public List<WebServicesClusters> cluster()
	{
		double maxSim=0.0;						// Maximum similarity in each reduction step decompressed matrix
		List<WebServicesCluster> clustersPair;	// Pair of clusters corresponding to maximum similarity
		int i=0;
		Set<Double> values ;					// All single values of decompressed matrix
		double localThreshold=1;					// Local threshold to store clusters
		
		
		try
		{
			if(constructMatrix)
				WSDLDocumentsCollection.getVectorSpaceModel();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Construct initial decompressed matrix
		System.out.println("Clustering started...\n");
		System.out.print("Constructing decompressed matrix...");
		
		decompressedInitialMatrix(WSDLDocuments.size(),!constructMatrix);
		
		System.out.println("Ended");
		
		// Find maximum value of decompressed matrix in current reduction step
		values = new HashSet<Double>(decompressedMatrix.values());
		maxSim = Collections.max(values);
		
		// Reduce decompressed matrix until the maximum value in decompressed matrix is under threshold o if remains only a number of clusters
		while(maxSim>minSimThreshold && _clusters.size()>minNumClusters)
		{	
			clustersPair = findMultiKeyValue(maxSim);	// get the pair of documents that have the maximum similarity
			
			System.out.println("\tReduction Step "+(i++)+"--> Max similarity:"+maxSim);
			System.out.println("\tClusters "+clustersPair.get(0).getIndex()+":"+clustersPair.get(0).getDocumentNames()+" - "+clustersPair.get(1).getIndex()+":"+clustersPair.get(1).getDocumentNames());
			
			// Reduce decompressed matrix
			reduce(clustersPair);
			
			// Find maximum value after reduction
			values = new HashSet<Double>(decompressedMatrix.values());
			if(!values.isEmpty())
				maxSim = Collections.max(values);	// get maximum value in decompressed similarity matrix
			
			// If flag not set, then Save clusters periodically
			if(!saveEndingClusters)
			{
				if(maxSim<localThreshold || _clusters.size()<30)
				{
					saveClusters(_clusters,localThreshold);
					if(maxSim<localThreshold)
					{
						if(localThreshold>0.2)
							localThreshold-=0.1;
						else if(localThreshold>0.02)
							localThreshold-=0.01;
						else
							localThreshold-=0.001;
					}
				}
			}
			
		}
		
		System.out.println("Reduction ended.");
	
		// save clusters to database and print to output
		if(!saveEndingClusters)
			saveClusters(_clusters,0);
		else // if save only ending clusters, save the given minimum similarity threshold
			saveClusters(_clusters,minSimThreshold);
		
		System.out.print("Write all clusters snapshots to file...");
		printSnapshotsToFile("output/clustering_multidimensionalAngles_eval.dat");
		
		System.out.println("Ended");
		
		return clusters;
	}
	
	/**
	 * Create initial decompressed matrix of specific size for similairities among all documents
	 * @param size Size in each dimension of decompressed matrix
	 * @param loadDB True to load decompressed matrix from database
	 */
	public void decompressedInitialMatrix(int size,boolean loadDB)
	{
		double sim;
		WebServicesCluster wsCluster;
		int i=0;
		HashMap<String,WebServicesCluster> docToClusters =new HashMap<String,WebServicesCluster>(size);
		_clusters = new ArrayList<WebServicesCluster>(WSDLDocumentsCollection.getDocuments().size());
		
		try
		{
			if(useFeaturesQF)
				qualityFactor = new WebServicesFeaturesClusterer(WSDLDocumentsCollection,new double[]{},true).computeQualityFactor();
			
			// Create clusters of one document for each wsdl document in collection
			for(WSDLDocument wsdlDocument:WSDLDocuments)
			{
				wsCluster = new WebServicesCluster(++i);
				wsCluster.add(wsdlDocument);
				_clusters.add(wsCluster);
				docToClusters.put(wsdlDocument.getNameWithoutExtension(), wsCluster);
			}
			
			if(!loadDB)	// create decompressed matrix
			{
				ObjectDBConn.em.getTransaction().begin();
				ObjectDBConn.em.createQuery("DELETE FROM DecompressedMatrix WHERE Repository='"+WSDLDocumentsCollection.getRepository()+"'").executeUpdate();
				ObjectDBConn.em.getTransaction().commit();
				
				// Find similarities for each pair of clusters length 1
				i=0;
				for(WebServicesCluster wsCluster_i:_clusters)
				{
					System.out.println("Row document "+(i++)+". "+wsCluster_i.getDocuments().get(0).getNameWithoutExtension());
					ObjectDBConn.em.getTransaction().begin();
					for(WebServicesCluster wsCluster_j:_clusters)
					{
						if(wsCluster_i==wsCluster_j)	// same cluster ( same wsdl document)
							sim = -1.0;
						else if(decompressedMatrix.containsKey(wsCluster_j,wsCluster_i))	// pair of documents already exists
							sim = (double) decompressedMatrix.get(wsCluster_j,wsCluster_i);
						else	/// pair of documents does not exists in matrix
						{
							if(!useFeaturesQF)
								sim = WSDLDocumentsCollection.getVectorSpaceModel().sim(wsCluster_i.getDocuments().get(0), wsCluster_j.getDocuments().get(0));
							else
								sim = qualityFactor.get(wsCluster_i.getDocuments().get(0).getNameWithoutExtension(),wsCluster_j.getDocuments().get(0).getNameWithoutExtension());
						}
						
						// add document pair and their similarity in matrix
						decompressedMatrix.put(wsCluster_i,wsCluster_j,sim);
		
						ObjectDBConn.em.persist(new DecompressedMatrix(WSDLDocumentsCollection.getRepository(),wsCluster_i.getDocuments().get(0).getNameWithoutExtension(),
								wsCluster_j.getDocuments().get(0).getNameWithoutExtension(),sim));
							
					}
					ObjectDBConn.em.getTransaction().commit();
					ObjectDBConn.em.clear();
				}
			}
			else	// load decompressed matrix from database
			{
				List<DecompressedMatrix> decompressedMatrixEntries;
				DecompressedMatrix decompressedMatrixEntry;
				int numEntries;	// number of values in decompressed matrix	
				
				
				TypedQuery<DecompressedMatrix> query = ObjectDBConn.em.createQuery(
					      "SELECT decompressedMatrix FROM DecompressedMatrix AS decompressedMatrix WHERE Repository='"+WSDLDocumentsCollection.getRepository()+"'",DecompressedMatrix.class);
				
				decompressedMatrixEntries= query.getResultList();
				numEntries = decompressedMatrixEntries.size();
				for( i=0;i<numEntries;i++)
				{
					decompressedMatrixEntry = decompressedMatrixEntries.get(i);
					decompressedMatrix.put(docToClusters.get(decompressedMatrixEntry.getDocument_1()), 
							docToClusters.get(decompressedMatrixEntry.getDocument_2()),decompressedMatrixEntry.getSimilarity());
	
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * Searches in decompressed matrix for the pair of clusters that have a specific similarity as value
	 * 
	 * @param val Similarity to search for
	 * @return Documents with similarity val
	 */
	private List<WebServicesCluster> findMultiKeyValue(double val)
	{
		List<WebServicesCluster> keys = new ArrayList<WebServicesCluster>();	// the pair of clusters with value val in decompressed matrix
		
		for(WebServicesCluster wsCluster_i:_clusters)
			for(WebServicesCluster wsCluster_j:_clusters)
			{
			//	System.out.println(decompressedMatrix.get(wsCluster_i,wsCluster_j));
				if((Double)decompressedMatrix.get(wsCluster_i,wsCluster_j)==val)
				{
					keys.add(wsCluster_i);
					keys.add(wsCluster_j);
					
					return keys;
				}
			}
		
		return null;
	}
	
	/**
	 * Reduce decompressed matrix by joining two clusters, adding the elements of second cluster to first one
	 * and removing the second cluster from row and column of decompressed matrix
	 * 
	 * @param wsCluster_i First cluster to join 
	 * @param wsCluster_j Second cluster to join 
	 */
	private void reduce(WebServicesCluster wsCluster_i, WebServicesCluster wsCluster_j)
	{
		double mean;	// mean value of two clusters in same column
		
		// Merge two clusters into one and store result in 
		wsCluster_i.merge(wsCluster_j);
		
		// find mean value of two clusters vectors(rows)
		for(WebServicesCluster wsClusterCol:_clusters)
		{
			if(wsCluster_i==wsClusterCol)
			{
				decompressedMatrix.put(wsCluster_i, wsClusterCol,-1.0);
				decompressedMatrix.put(wsClusterCol,wsCluster_i ,-1.0);
			}
			else
			{
				mean =((double) decompressedMatrix.get(wsCluster_i,wsClusterCol)+(double)decompressedMatrix.get(wsCluster_j,wsClusterCol))/2;
				decompressedMatrix.put(wsCluster_i, wsClusterCol,mean);
				decompressedMatrix.put(wsClusterCol, wsCluster_i,mean);
			}
		}
		
		// Remove all entries from decompressed matrix where first key (row) is wsCluster_j)
		decompressedMatrix.removeAll(wsCluster_j);	
		
		// Remove all entries from decompressed matrix where second key ( column)
		for(WebServicesCluster wsClusterCol:_clusters)
			if(decompressedMatrix.containsKey(wsClusterCol,wsCluster_j))
					decompressedMatrix.remove(wsClusterCol,wsCluster_j);
		
		// Remove second cluster from list of clusters
		_clusters.remove(wsCluster_j);	
	}
	
	/**
	 * Call reduce(WebServicesCluster wsCluster_i, WebServicesCluster wsCluster_j) for a pair of clusters
	 * 
	 * @param clustersPair Pair of clusters 
	 * @throws Exception 
	 */
	public void reduce(List<WebServicesCluster> clustersPair) 
	{
		if(clustersPair.size()<2)
			throw new IllegalArgumentException("Decompressed matrix reduction: List of clusters to join cannot be less than two");
		
		reduce(clustersPair.get(0),clustersPair.get(1));
	}
	
	/**
	 * Prints decompressed matrix to a file
	 * @param step Reduction step
	 */
	public void print(int step)
	{
		int i,j=0;
		
		try
		{
			out.write("-------------------------------------------------------------\n");
			out.write("Step "+step+"\n");
			out.write("-------------------------------------------------------------\n");
			out.newLine();
			
			// columns
			for(i=0;i<clusters.size();i++)
				out.write("\t"+_clusters.get(i).getIndex());
			out.newLine();
			// rows
			for(i=0;i<clusters.size();i++)
			{
				out.write(_clusters.get(i).getIndex()+"\t");
				for(j=0;j<clusters.size();j++)
					out.write(String.format("%.7f",decompressedMatrix.get(clusters.get(i),clusters.get(j)))+"\t");
					
				out.newLine();
			}
			
			out.newLine();
			for(i=0;i<_clusters.size();i++)
				out.write("Cluster "+_clusters.get(i).getIndex()+": "+_clusters.get(i).getDocumentNames()+"\n");
			
			out.flush();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
