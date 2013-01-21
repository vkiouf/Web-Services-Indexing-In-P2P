import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * XML File Parser and Handler
 */
public class XMLHandler
{
	/*=========================================================================
	 *					Fields
	 *=========================================================================*/

	protected String document;			// xml document
	protected Vector<String> tags;		// all tags names of xml document 
	protected Vector<String> attributes;	// all attributes of xml document
	
	public final static String[] PrimitiveTypes=
			new String[]{"boolean","byte","double","float","integer","long","string",
		"short","decimal","int","anyURI","dateTime","QName"};	// primitive types of xml
	
	/*=========================================================================
	 *					Constructors
	 *=========================================================================*/

	/**
	 * @param document Path of xml document
	 */
	public XMLHandler(String document)
	{
		this.document = document;
		tags = new Vector<String>();
		attributes = new Vector<String>();
	}

	/*=========================================================================
	 *					Getters
	 *=========================================================================*/
	
	/**
	 * @return the document
	 */
	public String getDocument()
	{
		return document;
	}

	/**
	 * @return the tags
	 */
	public Vector<String> getTags()
	{
		return tags;
	}

	/**
	 * @return the attributes
	 */
	public Vector<String> getAttributes()
	{
		return attributes;
	}
	
	/*=========================================================================
	 *					Setters
	 *=========================================================================*/
	
	/*=========================================================================
	 *					Methods
	 *=========================================================================*/
	
	/**
	 * Create parser for document
	 * @return Document as parser
	 */
	public Document createParser()
	{
		return createParser(document);
	}
	
	/**
	 * Create parser for document
	 * @return Document as parser
	 */
	public static Document createParser(String document)
	{
		try
		{
			// Create parser for document
			File wsdlDoc = new File(document);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(wsdlDoc);
			doc.getDocumentElement().normalize();
			
			return doc;
		}
		catch (SAXException | IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
		catch (ParserConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Find  all element tags and attributes names of xml document and
	 * updates tags list
	 */
	public void getAllXMLTagsAttributesNames()
	{
		Document doc = createParser();
		
		// Get children of root node and iterate each child through depth to take element tags
		tags.add("?xml");
		NodeList nodesList = doc.getChildNodes();
		findTags(nodesList);
	}
	
	/**
	 * Find tag names from children of nodes
	 * and add them to tag list 
	 * 
	 * @param nodelist List of nodes 
	 */
	private void findTags(NodeList nodeList)
	{
		String nodeName;	// current node name
		
		for(int i=0; i<nodeList.getLength(); i++)
		{
		  Node childNode = nodeList.item(i);
		  if(childNode.getNodeType()==Node.ELEMENT_NODE)
		  {
			  nodeName = childNode.getNodeName();
			  if(!tags.contains(nodeName))
				  tags.add(nodeName);
			  
			  //recursively iterate through child nodes
			  NodeList children = childNode.getChildNodes();
			  if (children != null)
			      findTags(children);
		  }      
		}
	}
}
