import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.persistence.*;

/**
 *
 * A cluster of web services with similar functionality
 */
@Entity 
public class WebServicesCluster
{
	/*=========================================================================
	 *					Fields
	 *=========================================================================*/
	
	private int index;				// Index of cluster 
	private transient String name;			// Name of cluster
	private transient List<WSDLDocument> documents;	// Collection of wsdl documents
	private List<String> documentNames;		// Names of documents
	
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
		documentNames = new ArrayList<String>();
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
	
	
	/**
	 * @return the documentNames
	 */
	public List<String> getDocumentNames()
	{
		return documentNames;
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
		this.documentNames.add(document.getNameWithoutExtension());
	}
	
}
