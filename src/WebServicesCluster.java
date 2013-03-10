import java.util.*;

import javax.persistence.*;

/**
 * A cluster of web services with similar functionality
 */
@Embeddable
public class WebServicesCluster
{
	/*=========================================================================
	 *					Fields
	 *=========================================================================*/
	
	private int index;								// Index of cluster 
	private transient String name;					// Name of cluster
	private transient List<WSDLDocument> documents;	// Collection of wsdl documents
	private List<String> documentNames;				// Names of documents
	
	private transient  String category;						// Display cluster category
	private transient int succ;										// Number of documents belongs to cluster category
	private transient double precision=0.0;
	private transient double recall=0.0;

	
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
	
	public WebServicesCluster(int index, List<WSDLDocument> documents,List<String> documentNames)
	{
		this.index = index;
		this.documentNames =new ArrayList<String>( documentNames);
		this.documents = documents;
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
	
	/**
	 * @return the category
	 */
	public String getCategory()
	{
		return category;
	}

	/**
	 * @return the succ
	 */
	public int getSucc()
	{
		return succ;
	}

	/**
	 * @return the precision
	 */
	public double getPrecision()
	{
		return precision;
	}

	/**
	 * @return the recall
	 */
	public double getRecall()
	{
		return recall;
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
	
	/**
	 * Add another cluster's documents into this
	 * @param cluster Cluster to merge with
	 */
	public void merge(WebServicesCluster cluster)
	{
		this.documents.addAll(cluster.getDocuments());
		this.documentNames.clear();
		for(WSDLDocument document:this.documents)
			this.documentNames.add(document.getNameWithoutExtension());
	}
	
//	/**
//	 * Find category of cluster.
//	 * The category of cluster is the category which has the most documents in this cluster.
//	 * The remaining considered as misplaced
//	 * 
//	 * @return Category name
//	 */
//	public String findCategory(HashMap<String,Vector<String>> categories)
//	{
//		HashMap<String,Integer> numDocumentsPerCategory = new HashMap<String,Integer>(categories.keySet().size()); // local categorization
//		Set<String> catNames= categories.keySet();			// category names of predefined categories
//		int maxNumDocumentsCategory;								// category with maximum number of documents
//		
//		// initialise numDocumentsPerCategory
//		for(String catName:catNames)
//			numDocumentsPerCategory.put(catName, 0);
//		
//		// find local number of documents in each category
//		for(String document:documentNames)
//			for(String catName:catNames)
//				if(categories.get(catName).contains(document))
//					numDocumentsPerCategory.put(catName,numDocumentsPerCategory.get(catName)+1);
//		
//		maxNumDocumentsCategory = Collections.max(numDocumentsPerCategory.values());
//		
//		// set as cluster category, the category which contains the maximum number of values
//		succ = maxNumDocumentsCategory;
//		for(String catName:catNames)
//			if(numDocumentsPerCategory.get(catName)==maxNumDocumentsCategory)
//			{
//				this.category = catName;
//				break;
//			}
//		
//		return this.category;
//	}
//	
//	/**
//	 * Measures precision of cluster against a predefined document categorization
//	 * defined in WSDLCategory
//	 * 
//	 * Precision = succ / (succ+mispl)
//	 * 
//	 * @returns Precision 
//	 */
//	public double measurePrecision()
//	{
//		/*Set<String> categoriesLabels = WSDLCategory.getCategories().keySet();	// get categories labels from wsdl category 
//		int succANDmispl = this.documentNames.size();							// the denominator is all documents of this cluster
//		int _succ;	// temporary holds the succesful documents from current category
//		double _precision;
//		
//		for(String categoryLabel:categoriesLabels)
//		{
//			_succ = Sets.intersection(new HashSet(WSDLCategory.getCategories().get(categoryLabel)), new HashSet(documents)).size();
//			_precision = (double) _succ / succANDmispl;
//
//			if(_precision>this.precision)
//			{
//				this.succ = _succ;
//				this.precision= _precision;
//			}
//		}*/
//		
//		return this.precision;
//	}
//	
//	/**
//	 * Measures recall of cluster against a predefined document categorization defined in WSDLCategory
//	 * 
//	 * Recall = succ / (succ+missed)
//	 * 
//	 * @param categories A predefined document categorization
//	 * @returns Precision 
//	 */
//	public double measureRecall()
//	{
//		/*Set<String> categoriesLabels = WSDLCategory.getCategories().keySet();	// get categories labels from wsdl category 
//		int succANDmissed;						// the denominator is all documents of this cluster
//		int _succ;	// temporary holds the succesful documents from current category
//		double _recall;
//		
//		for(String categoryLabel:categoriesLabels)
//		{
//			succANDmissed = WSDLCategory.getCategories().get(categoryLabel).size();
//			_succ = Sets.intersection(new HashSet(WSDLCategory.getCategories().get(categoryLabel)), new HashSet(documents)).size();
//			_recall = (double) _succ / succANDmissed;
//
//			if(_recall>this.recall)
//			{
//				this.succ = _succ;
//				this.recall= _recall;
//			}
//		}*/
//		
//		return this.recall;
//	}
	
}
