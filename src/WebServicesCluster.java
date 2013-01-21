import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 *
 * A cluster of web services with similar functionality
 */
public class WebServicesCluster
{
	/*=========================================================================
	 *					Fields
	 *=========================================================================*/
	
	private int index;				// Index of cluster 
	private String name;			// Name of cluster
	private List<WSDLDocument> documents;	// Collection of wsdl documents
	
	/*=========================================================================
	 *					Constructors
	 *=========================================================================*/
	
	/**
	 * @param index A number which distinguish this cluster from others 
	 * @param name Tag name of cluster
	 */
	public WebServicesCluster(int index)
	{
		this.index = index;
		documents = new ArrayList<WSDLDocument>();
	}
	
	/*=========================================================================
	 *					Getters
	 *=========================================================================*/
	
	/**
	 * @return the index
	 */
	public int getIndex()
	{
		return index;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * @return the documents
	 */
	public List<WSDLDocument> getDocuments()
	{
		return documents;
	}

	/*=========================================================================
	 *					Setters
	 *=========================================================================*/
	
	/**
	 * @param index the index to set
	 */
	public void setIndex(int index)
	{
		this.index = index;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/*=========================================================================
	 *					Methods
	 *=========================================================================*/
	
	/**
	 * Adds document in cluster
	 * @param document WSDLDocument belongs to cluster
	 */
	public void add(WSDLDocument document)
	{
		this.documents.add(document);
	}
	
	/**
	 * Get  names of clustered documents
	 * @return  A vector of names
	 */
	public Vector<String> getDocumentNames()
	{
		Vector<String> docNames=new Vector<String>(documents.size());
		
		for(WSDLDocument document:documents)
			docNames.add(document.getNameWithoutExtension());
		
		return docNames;
	}
}
