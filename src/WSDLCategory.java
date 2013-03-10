import java.util.*;


import javax.persistence.*;

/**
 *  Entity which contains a category of documents and collection of document names belong to each category
 *
 */
@Entity
public class WSDLCategory
{
	/*=========================================================================
	 *					Fields
	 *=========================================================================*/

	String repository;		// source of wsdl documents
	String category;		
	Vector<String> documents;	// documents included in category
	transient HashMap<String,Vector<String>> categories;	// all categories with their documents
	transient Set<Set<String>> categoriesSet;	// categories as set of documents sets
	//transient String selectRepository;	// selected repository to retrieve categories
	
	/*=========================================================================
	 *					Constructors
	 *=========================================================================*/

	/**
	 * @param repository
	 * @param category
	 * @param documents
	 */
	public WSDLCategory(String repository,String category, Vector<String> documents)
	{
		this.repository = repository;
		this.category = category;
		this.documents = documents;
	}
	
	/**
	 * 
	 * @param repository Repository where to retieve categories from
	 */
	public WSDLCategory(String repository)
	{
		this.repository = repository;
	}
	
	/*=========================================================================
	 *					Getters
	 *=========================================================================*/
	
	/**
	 * @return the category
	 */
	public String getCategory()
	{
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category)
	{
		this.category = category;
	}

	/**
	 * @return the documents
	 */
	public Vector<String> getDocuments()
	{
		return documents;
	}

	/**
	 * @param documents the documents to set
	 */
	public void setDocuments(Vector<String> documents)
	{
		this.documents = documents;
	}
	
	/**
	 * @return the categoriesSet
	 */
	public  Set<Set<String>> getCategoriesSet()
	{
		try
		{
			if(categoriesSet==null)
				getCategoriesFromDB();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	
		
		return categoriesSet;
	}
	
	/**
	 * @return the categories
	 */
	public  HashMap<String, Vector<String>> getCategories()
	{
		try
		{
			if(categories==null)
				getCategoriesFromDB();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return categories;
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
	 * Get all categories from object db
	 * @throws Exception 
	 */
	private  void getCategoriesFromDB() throws Exception
	{
		/*if(selectRepository == null)
			throw new Exception("There is no selected repository");
		
		if(!selectRepository.equals(WSDLCrawler.webServiceXName) || 
				selectRepository.equals(WSDLCrawler.soaTraderName))
			throw new Exception("Selected repository should be "+WSDLCrawler.webServiceXName+" or "+WSDLCrawler.soaTraderName);*/
		
		WSDLCategory wsdlCat;
		TypedQuery<WSDLCategory> query = ObjectDBConn.em.createQuery(
			      "SELECT cat FROM WSDLCategory AS cat WHERE cat.repository='"+repository+"'", WSDLCategory.class);
		
		categories = new HashMap<String,Vector<String>>(query.getResultList().size());
		categoriesSet = new HashSet<Set<String>>(query.getResultList().size());
		
		int num=query.getResultList().size();
		for(int i=0;i<num;i++)
		{
			wsdlCat = query.getResultList().get(i);
			categories.put(wsdlCat.getCategory(), wsdlCat.getDocuments());
			if(wsdlCat.getDocuments().size()>0)
				categoriesSet.add(new HashSet<String>(wsdlCat.getDocuments()));
		}
	}
	
	/**
	 * Create custom categorization for a set of documents
	 * @param repository Repository name of wsdl documents
	 * @param categories A hashmap where key is the name of category and value is a list of documents in this category
	 */
	public static void create(String repository,Hashtable<String,Vector<String>> categories)
	{
		Set<String> categoryNames = categories.keySet();	// get categories labels
		
		// First delete all categories from wsdl category
		ObjectDBConn.em.getTransaction().begin();
		ObjectDBConn.em.createQuery("DELETE FROM WSDLCategory WHERE repository='"+repository+"'").executeUpdate();
		
		// Store categories
		for(String categoryName:categoryNames)
			ObjectDBConn.em.persist(new WSDLCategory(repository,categoryName,categories.get(categoryName)));
		
		ObjectDBConn.em.getTransaction().commit();
		ObjectDBConn.em.clear();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "WSDLCategory [category=" + category + ", documents="
				+ documents + "]";
	}

}
