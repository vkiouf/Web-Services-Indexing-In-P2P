import java.io.*;
import java.util.*;

import javax.persistence.TypedQuery;

/**
 * An abstract class which should inherit all clustering algorithms
 *
 */
public abstract class WebServicesClusterer
{
	/*=========================================================================
	 *					Fields
	 *=========================================================================*/
	
	protected String name ;										// Name of algorithm
	protected WSDLDocumentCollection WSDLDocumentsCollection;				// Collection of wsdl documents
	protected String clusterFilename;							// Clusters and their collection of documents in a file
	
	protected Vector<WSDLDocument> WSDLDocuments; 								// Collection of web services descriptions 
	protected HashMap<WSDLDocument,WSDLDocumentHandler> WSDLDocumentHandlers;	// Handlers for each wsdl documents
	protected boolean saveEndingClusters;											// True to save only ending clusters
	
	//Output
	protected List<WebServicesClusters> clusters;			// A list of web services clusters with extra information
															// for each clustering

	/*=========================================================================
	 *					Constructors
	 *=========================================================================*/
	
	/**
	 * 
	 * @param WSDLDocumentCollection	A collection of wsdl documents
	 * @param clusterFilename Filename to print final clusters
	 * @param name	Name of clusterer algorithm
	 * @param bool saveEndingClusters True to save only ending clusters
	 */
	public WebServicesClusterer(String name,WSDLDocumentCollection WSDLDocumentsCollection,String clusterFilename,boolean saveEndingClusters)
	{
		this.WSDLDocumentsCollection = WSDLDocumentsCollection;
		this.WSDLDocuments = WSDLDocumentsCollection.getWSDLDocuments();
		this.WSDLDocumentHandlers = WSDLDocumentsCollection.getWSDLDocumentHandlers();
		this.saveEndingClusters = saveEndingClusters;
		
		//  algorithm		
		this.name=name;
		
		// clusters data
		clusters = new ArrayList<WebServicesClusters>();
		this.clusterFilename = clusterFilename;
		
		ObjectDBConn.em.getTransaction().begin();
		ObjectDBConn.em.createQuery("DELETE FROM WebServicesClusters WHERE repository='"+WSDLDocumentsCollection.getRepository()+"'").executeUpdate();
		ObjectDBConn.em.getTransaction().commit();
		
		// create output directory
		File outputdir = new File("output");
		if (!outputdir.exists())
		{
			outputdir.mkdir();
		}
		
	}
	
	/*=========================================================================
	 *					Getters
	 *=========================================================================*/
	
	public WSDLDocumentCollection getWSDLDocumentsCollection() 
	{
		return WSDLDocumentsCollection;
	}

	/**
	 * @return the clusters
	 */
	public List<WebServicesClusters> getClusters()
	{
		return clusters;
	}
	
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	
	/*=========================================================================
	 *					Setters
	 *=========================================================================*/

	/**
	 * Set collection of wsdl documents to cluster
	 * @param wSDLDocuments
	 */
	/*public void setWSDLDocuments(Vector<String> wSDLDocuments) 
	{
		WSDLDocuments = wSDLDocuments;
	}*/
	
	/*=========================================================================
	 *					Methods
	 *=========================================================================*/
	
	public abstract List<WebServicesClusters> cluster(); 
	
	/*//////////////////////////////////////////////////////////////////////////
	 *					Save
	 *////////////////////////////////////////////////////////////////////////*/
	
	/**
	 *  Save into object database and print to output the final clusters in current step
	 *  @param clusters Clusters formatted in current step
	 *  @param threshold Similarity threshold 
	 */
	public void saveClusters(List<WebServicesCluster> clusters, double threshold)
	{
		int i=0;
		
		System.out.println("\n-----------------------------------------------");
		System.out.println("Number of clusters: "+clusters.size()+"\n");
		for( i=0;i<clusters.size();i++)
			System.out.println("Cluster "+clusters.get(i).getIndex()+" : "+clusters.get(i).getDocumentNames());
		System.out.println("-----------------------------------------------");
		
		System.out.println();
		System.out.println();
		System.out.print("Writing clusters to database...");

		try
		{
			FileWriter clusterWriter = new FileWriter(clusterFilename+"_"+threshold+".txt");
			BufferedWriter clusterOut = new BufferedWriter(clusterWriter);
			
			clusterOut.write("Number of clusters: "+clusters.size());
			clusterOut.newLine();
			
			ObjectDBConn.em.getTransaction().begin();
			ObjectDBConn.em.persist(new WebServicesClusters(threshold,clusters.size(),new ArrayList<WebServicesCluster>(clusters),
					name,WSDLDocumentsCollection.getRepository()));
			ObjectDBConn.em.getTransaction().commit();
			
			for(i=0;i<clusters.size();i++)
			{
				clusterOut.write("Cluster "+clusters.get(i).getIndex()+" : "+clusters.get(i).getDocumentNames());
				clusterOut.newLine();
			}

			clusterOut.close();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	/*//////////////////////////////////////////////////////////////////////////
	 *					Print
	 *////////////////////////////////////////////////////////////////////////*/
	
	/**
	 * Print all web services clusterers snapshots to a file 
	 * @param  File where precision and recall of each categorization snapshot printed 
	 */
	public void printSnapshotsToFile(String snapshotsFilename)
	{
		TypedQuery<WebServicesClusters> query =  ObjectDBConn.em.createQuery(
			      "SELECT WebServ FROM WebServicesClusters As WebServ WHERE repository='"+WSDLDocumentsCollection.getRepository()+"'",WebServicesClusters.class);
		
		FileWriter fw;
		try
		{
			fw = new FileWriter(snapshotsFilename);
			BufferedWriter bw = new BufferedWriter(fw);
			
			for(int i=0;i<query.getResultList().size();i++)
			{
				WebServicesClusters clusters = query.getResultList().get(i);
				bw.write(clusters.getNumber()+"\t"+clusters.getThreshold()+"\t"+clusters.getPrecision()+"\t"+clusters.getRecall()+"\t"+clusters.getfMeasure());
				bw.newLine();
			}
			
			bw.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
