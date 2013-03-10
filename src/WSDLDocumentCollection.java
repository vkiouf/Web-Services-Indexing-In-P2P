import java.io.File;
import java.util.*;

/**
 * Collection of wsdl documents describing web services
 */
public class WSDLDocumentCollection
{
	/*=========================================================================
	 *					Fields
	 *=========================================================================*/
	
	private String repository;							// Repository name of wsdl documents
	private String path;								// Directory contains wsdl documents
	private Vector<String> documents;					// Path of wsdl documents in local repository
	
	protected Vector<WSDLDocument> WSDLDocuments; 								// Collection of web services descriptions 
	protected HashMap<WSDLDocument,WSDLDocumentHandler> WSDLDocumentHandlers;	// Handlers for each wsdl documents
	
	private WSDLCategory categories;					// Documents set in categories
	
	private VectorSpaceModel vectorSpaceModel;			// Vector space model for wsdl documents of collection
	
	/*=========================================================================
	 *					Constructors
	 *=========================================================================*/


	/**
	 * Creates a collection of wsdl documents searching the directory recursively
	 * 
	 * @param repository Repository name of wsdl documents
	 * @param path Path of directory contains web services descriptions
	 */
	public WSDLDocumentCollection(String repository,String path)
	{
		this.repository = repository;
		this.path = path;
		this.categories = new WSDLCategory(repository);
		
		// Read all documents from path and update the appropriate structures
		read();
	}
	
	/**
	 * Creates a collection of wsdl documents passed as parameter
	 * 
	 * @param documents List of wsdl documents
	 */
	/*public WebServicesCollection(Vector<String> documents)
	{
		this.documents = documents;
		//updateNames();
	}*/
	
	/*=========================================================================
	 *					Getters
	 *=========================================================================*/
	
	/**
	 * @return the directory
	 */
	public String getDirectory()
	{
		return path;
	}
	
	/**
	 * @return the documents
	 */
	public Vector<String> getDocuments()
	{
		return documents;
	}
	
	/**
	 * @return the repository
	 */
	public String getRepository()
	{
		return repository;
	}

	/**
	 * @return the wSDLDocuments
	 */
	public Vector<WSDLDocument> getWSDLDocuments()
	{
		return WSDLDocuments;
	}

	/**
	 * @return the wSDLDocumentHandlers
	 */
	public HashMap<WSDLDocument, WSDLDocumentHandler> getWSDLDocumentHandlers()
	{
		return WSDLDocumentHandlers;
	}
	
	/**
	 * @return the vectorSpaceModel
	 * @throws Exception 
	 */
	public VectorSpaceModel getVectorSpaceModel() throws Exception
	{
		if(vectorSpaceModel==null)
			throw new Exception("Vector space model has not been created yet.");
		
		return vectorSpaceModel;
	}
	
	/*=========================================================================
	 *					Setters
	 *=========================================================================*/

	/**
	 * @param path the path to set
	 */
	public void setPath(String path)
	{
		this.path = path;
		read();
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
	 * Adds a wsdl document to collection
	 * @param document WSDL document
	 */
	public void addDocument(String document)
	{
		documents.add(document);
	}
	
	/**
	 *  Check if wsdl document exists in collection
	 * @param document Path of wSDL document
	 * @return True if document exists. False if not
	 */
	public boolean contains(String document)
	{
		return documents.contains(document);
	}
	
	/**
	 * Retrieve a list of wsdl documents that their name contains the text passed as parameter
	 * @param name Name to search for
	 * @return List of documents contain the parameter string
	 */
	/*public List<String> containsName(String name)
	{
		List<String> matches = new ArrayList<String>();
		
		for(String docName:names)
			if(docName.contains(name))
				matches.add(docName);
		
		return matches;
	}*/
	
	/**
	 * Get all documents included the determined path
	 */
	public void read()
	{	
		// Read all documents from path and store document paths in documents and document names in names
		// Create wsdl documents and document handlers for each read document
		File folder = new File(path);
		File[] filesList = folder.listFiles(); 
		File file;
		String filename;
		WSDLDocument wsdlDocument;
		
		documents = new Vector<String>();
		//names = new ArrayList<String>();
		WSDLDocuments = new Vector<WSDLDocument>();
		WSDLDocumentHandlers =new HashMap<WSDLDocument,WSDLDocumentHandler>();
		
		for(int i=0;i<filesList.length;i++)
		{
			file = filesList[i];
			if(file.isFile())
			{
				filename = file.getName();
					if(filename.endsWith(".asmx") || filename.endsWith(".wsdl"))
					{
						documents.add(file.getPath());

						wsdlDocument = new WSDLDocument(repository,file.getPath(),findCategory(filename));
						WSDLDocuments.add(wsdlDocument);
						WSDLDocumentHandlers.put(wsdlDocument,new WSDLDocumentHandler(wsdlDocument));
					}
			}
		}
		
		Collections.sort(documents);
		//Collections.sort(names);
	}
	
	/**
	 * Find category of document in selected repository
	 * @param name Filename of document
	 */
	public String findCategory(String name)
	{
		Set<String> categoryLabels = categories.getCategories().keySet();			// categories names
		String nameWithoutExt = name.substring(0,name.indexOf('.'));							// file name without extension
		
		// Find the category which contains the current document
		for(String category:categoryLabels)
			if(categories.getCategories().get(category).contains(nameWithoutExt))
				return category;
		
		return null;
	}
	
	/**
	 * Extract content from all documents and load word vectors of each wsdl document
	 * 
	 * @param loadDB True to get word vector from database. The rest of parameters ignored if set to true.
	 * @param isStemmed True to extract words stemmed into their base words
	 * @param isDescriptionIncluded True to extract tokens from documentation tags
	 * @param isURLIncluded True to extract tokens from urls
	 * @param isCommentIncluded True to extract tokens from comments
	 */
	public void extractContent(boolean loadDB,boolean isStemmed,boolean isDescriptionIncluded,boolean isURLIncluded,boolean isCommentIncluded)
	{
		for(WSDLDocument wsdlDocument : WSDLDocuments)
			wsdlDocument.extractContent(loadDB, isStemmed, isDescriptionIncluded, isURLIncluded, isCommentIncluded);
	}
	
	/**
	 * Get all documents names without extensions
	 * @return
	 */
	public Vector<String> getDocumentsNames()
	{
		Vector<String> documentNames = new Vector<String>(WSDLDocuments.size());
		for(WSDLDocument wsdlDocument : WSDLDocuments)
			documentNames.add(wsdlDocument.getNameWithoutExtension());
		
		return documentNames;
	}
	
	/**
	 * Creates vector space model from collection of wsdl documents
	 */
	public void createVectorSpaceModel()
	{
		// Create a vector space model for documents
		System.out.print("Create Vector Space Model...");System.out.flush();
		this.vectorSpaceModel = new VectorSpaceModel(WSDLDocuments);
		System.out.print("Compute Weights...");System.out.flush();
		this.vectorSpaceModel.computeWeights();	// compute weights in vector space model
		System.out.print("Save data...");System.out.flush();
		this.vectorSpaceModel.printToFileAndSaveToDb();
		System.out.println("Ended");
	}
	
	/**
	 * Updates only the list which contain document names without extensions
	 */
	/*public void updateNames()
	{
		File file;
		
		for(int i=0;i<documents.size();i++)
		{
			file = new File(documents.get(i));
			names.add(file.getName());
		}
	}*/
	
//	/**
//	 * Adds a document in category . If category does not exist, create a new one. Else
//	 * put the document in already existing category
//	 * 
//	 * @param category Category name where document belongs to
//	 * @param document Document to add 
//	 */
//	public void addDocumentInCategory(String category, String document)
//	{
//		// if categories not initialised, create a new one with an initial capacity of 10 categories*1000 documents 
//		if(categories==null)
//			categories = new HashMap<String,List<String>>(10*documents.size());
//		
//		List<String> categoryDocuments;	// documents list of category
//		
//		// Check if categories contain category passed as parameter. If true, add document to list of this entry
//		if(categories.containsKey(category))
//		{
//			categoryDocuments = categories.get(category);
//			categoryDocuments.add(document);
//			categories.put(category, categoryDocuments);
//		}
//		else	// Create a new list of documents for new category
//		{
//			categoryDocuments = new ArrayList<String>();
//			categoryDocuments.add(document);
//			categories.put(category, categoryDocuments);
//		}
//	}
//	
//	/**
//	 * Adds a list of documents in category . If category does not exist, create a new one. Else
//	 * put the documents in already existing category
//	 * 
//	 * @param category Category name where documents belongs to
//	 * @param documents List of category documents
//	 */
//	public void addDocumentsInCategory(String category, List<String> documents)
//	{
//		// if categories not initialised, create a new one with an initial capacity of 10 categories*1000 documents 
//		if(categories==null)
//			categories = new HashMap<String,List<String>>(10*this.documents.size());
//
//		List<String> categoryDocuments;	// documents list belong to category
//
//		// Check if categories contain category passed as parameter. If true, add documents to list of this entry
//		if(categories.containsKey(category))
//		{
//			categoryDocuments = categories.get(category);
//			categoryDocuments.addAll(documents);
//			categories.put(category, categoryDocuments);
//		}
//		else // Create new entry
//			categories.put(category, documents);
//	}
	
}
