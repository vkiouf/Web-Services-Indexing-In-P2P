import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;


/**
 * Collection of wsdl documents describing web services
 */
public class WebServicesCollection
{
	/*=========================================================================
	 *					Fields
	 *=========================================================================*/
	
	private String path;		// Directory contains wsdl documents
	private Vector<String> documents;	// Web services descriptions 
	private List<String> docNames;	// Only document names
	
	/*=========================================================================
	 *					Constructors
	 *=========================================================================*/
	
	/**
	 * Creates a collection of wsdl documents searching the directory recursively
	 * 
	 * @param path Path of directory contains web services
	 */
	public WebServicesCollection(String path)
	{
		this.path = path;
		update();
	}
	
	/**
	 * Creates a collection of wsdl documents passed as parameter
	 * 
	 * @param documents List of wsdl documents
	 */
	public WebServicesCollection(Vector<String> documents)
	{
		this.documents = documents;
		 updateNames();
	}
	
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
	
	/*=========================================================================
	 *					Setters
	 *=========================================================================*/

	/**
	 * @param path the path to set
	 */
	public void setPath(String path)
	{
		this.path = path;
		update();
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
		File file = new File(document);
		
		documents.add(document);
		docNames.add(file.getName());
	}
	
	/**
	 *  Check if wsdl document exists in collection
	 * @param document WSDL document
	 * @return True if document exists. False if not
	 */
	public boolean contains(String document)
	{
		return documents.contains(document);
	}
	
	/**
	 * Retrieve a list of wsdl documents contain name
	 * @param name WSDL document name
	 * @return List of documents contain name
	 */
	public List<String> containsName(String name)
	{
		List<String> matches = new ArrayList<String>();
		
		for(String document:docNames)
			if(document.contains(name))
				matches.add(document);
		
		return matches;
	}
	
	/**
	 * Update documents list which are contained in path
	 */
	public void update()
	{	
		File folder = new File(path);
		File[] filesList = folder.listFiles(); 
		File file;
		String filename;
		
		documents = new Vector<String>();
		docNames = new ArrayList<String>();
		
		for(int i=0;i<filesList.length;i++)
		{
			file = filesList[i];
			if(file.isFile())
			{
				filename = file.getName();
					if(filename.endsWith(".asmx") || filename.endsWith(".wsdl"))
					{
						documents.add(file.getPath());
						docNames.add(filename);
					}
			}
		}
		
		Collections.sort(documents);
		Collections.sort(docNames);
	}
	
	/**
	 * Updates only document names
	 */
	public void updateNames()
	{
		File file;
		
		for(int i=0;i<documents.size();i++)
		{
			file = new File(documents.get(i));
			docNames.add(file.getName());
		}
	}
	
}
