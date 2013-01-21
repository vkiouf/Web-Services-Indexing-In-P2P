import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Handler for wsdl handler as a specific type of xml handler. 
 * Parses wsdl file to extract useful features
 */
public class WSDLDocumentHandler extends XMLHandler
{

	/*=========================================================================
	 *					Fields
	 *=========================================================================*/
	
	// Some useful nodes in a wsdl file
	private final static String wsdlTypeTag = "wsdl:types";
	private final static String complexTypeTag = "s:complexType";
	private final static String messageTag = "wsdl:message";
	private final static String portTag = "wsdl:portType";
	private final static String webServiceTag = "wsdl:service";
	
	private WSDLDocument wsdlDocument;											// Document of handler
	public static String emptyContentWSDLFile = "empty_content.wsdl";			// WSDL File with empty text ( extract techninal terms)

	// General computing words
	public static String generalCompWordsFile = "general_computing_words.dat";	// General computing words including wsdl tags
	public static Vector<String> generalComputingWords;							// Vector of general computing words
	
	// Features extracted from wsdl
	private HashMap<String,String[]> complexTypes;	// Key: Name of complex type 
													// Value: Data Array of element types
	private HashMap<String,String[]> messages;	// Key: Name of message
												// Value: Parameters types
	
	private HashMap<String,String[]> portMessagesSequence;			// Key: Name of port
															// Value: Type of message ( input,output)
	private HashMap<String,String[]> portMessagesStructure;		// Key: Name of port
											    			// Value: List of messages 
	
	/*=========================================================================
	 *					Constructors
	 *=========================================================================*/
	
	/**
	 * @param wsdlDocument WSDL Document 
	 */
	public WSDLDocumentHandler(WSDLDocument wsdlDocument)
	{
		super(wsdlDocument.getPath());
		this.wsdlDocument = wsdlDocument;
	}
	
	/*=========================================================================
	 *					Getters
	 *=========================================================================*/
	
	/**
	 * @return complexTypes field
	 */
	public HashMap<String,String[]> getComplexTypes()
	{
		return complexTypes;
	}
	
	/**
	 * @return the messages
	 */
	public HashMap<String,String[]> getMessages()
	{
		return messages;
	}
	
	/**
	 * @return the portMessagesSequence
	 */
	public HashMap<String, String[]> getPortMessagesSequence()
	{
		return portMessagesSequence;
	}

	/**
	 * @return the portMessagesStructure
	 */
	public HashMap<String, String[]> getPortMessagesStructure()
	{
		return portMessagesStructure;
	}
	
	/*=========================================================================
	 *					Methods
	 *=========================================================================*/
	
	/**
	 * Extract content from types names and attributes, messages, ports and urls.
	 * The words extracted are stored in wsdl document.
	 */
	public void extractContent()
	{
		String[] startNodeTags = new String[]{wsdlTypeTag,messageTag,portTag};
		Document doc = createParser();	// create xml parser for document
		NodeList startNodeList;	// collection of nodes specific tag

		int i=0;
		
		for(String startNodeTag:startNodeTags)
		{
			startNodeList = doc.getElementsByTagName(startNodeTag);
			for(i=0;i<startNodeList.getLength();i++)
				if(startNodeList.item(i).getNodeType()== Node.ELEMENT_NODE)
					getChildrenNodeNames(startNodeList.item(i));
		}
	}
	
	private void getNodeName(Node node)
	{
		NamedNodeMap attrs;
		String name;	// node name
		String[] simpleWordsName;	// composite name in its part names splitted by Upper case or underscore
		
		attrs = node.getAttributes();
		if(attrs.getNamedItem("name")!=null)
		{
			name = attrs.getNamedItem("name").getNodeValue();
			simpleWordsName = name.split("(?=\\p{Upper})");
			for(String simpleWord:simpleWordsName)
				if(!simpleWord.isEmpty() && simpleWord.length()>1 && !simpleWord.contains("_"))
					this.wsdlDocument.addWord(simpleWord.toLowerCase());
		}
		
	}
	
	/**
	 * Get name value from children of node  
	 * @param node Parent node
	 */
	private void getChildrenNodeNames(Node node) 
	{
	    NodeList nodeList = node.getChildNodes();
	    for (int i = 0; i < nodeList.getLength(); i++) 
	    {
	        Node currentNode = nodeList.item(i);
	        if (currentNode.getNodeType() == Node.ELEMENT_NODE) 
	        {
	        	getNodeName(currentNode);
	        	getChildrenNodeNames(currentNode);
	        }
	    }
	}
	
	/**
	 * Get web service name from tag "service" of document
	 * @return Web service name
	 */
	public String getWebServiceName()
	{
		String webServiceName;	// Web service name in "service" tag
		Document doc = createParser();	// Document for parse
		Node webServiceNode = doc.getElementsByTagName(webServiceTag).item(0);	// web service node
		
		webServiceName = webServiceNode.getAttributes().getNamedItem("name").getNodeValue();
		
				
		return webServiceName;
	}
	
	/**
	 * Get web service name as a set of words split in upper case
	 * @return Words of web service
	 */
	public String[] getWebServiceNameSplit()
	{
		String webServiceName = getWebServiceName();		
		String[] webServiceNameSegmentWords = webServiceName.split("(?=\\p{Upper})");
		String splitWord;
		
		// remove empty strings
		List<String> namesL = new ArrayList<String>(webServiceNameSegmentWords.length);
		Collections.addAll(namesL, webServiceNameSegmentWords);
		Iterator<String> namesLIter = namesL.iterator();
		while(namesLIter.hasNext())
		{
			splitWord = namesLIter.next();
			if(splitWord.isEmpty() || splitWord.length()==1)
				namesLIter.remove();
		}
				
		// all letters are capital
		if(namesL.size()==0)
			return new String[]{ webServiceName.toLowerCase()};
				
		return namesL.toArray(new String[namesL.size()]);
	}
	
	/**
	 * Updates complexTypes hash map where key is the name of complex element type and 
	 * value an array of element types.
	 * @return complexTypes map
	 */
	public HashMap<String,String[]> getComplexTypesMap()
	{
		//Vector<String[]> complexTypes;	// Complex types
		Vector<String> complexTypeElements=new Vector<String>();	// Complex type as an array of element
		String complexTypeName="";			// Name of complex type element
		String type;				// Single element type ( e.g. string)
		
		int i=0,j=0,k=0,l=0,m=0;
		int numComplexTypes,numElements;
		
		Document doc = createParser();	// Document for parse
		
		NodeList wsdlTypeList = doc.getElementsByTagName("wsdl:types");
		NodeList schemaNodeList;	// schema nodes under wsdl types
		NodeList elementsNodeList;	// elements under each node
		NodeList elementChildrenNodeList;	// elements under each node
		
	
		Node wsdlNode;
		Node schemaNode;
		Node elementNode;
		Node complexTypeNode;
		Node sequenceNode;
		Node typeNode;
		Node attr;
		
		complexTypes = new HashMap<String,String[]>();
		
		for(i=0;i<wsdlTypeList.getLength();i++)
		{
			wsdlNode = wsdlTypeList.item(i);
			if(wsdlNode.getNodeType()!= Node.ELEMENT_NODE)
				continue;
			schemaNodeList = wsdlNode.getChildNodes();
			for(j=0;j<schemaNodeList.getLength();j++)
			{
				schemaNode = schemaNodeList.item(j);
				if(schemaNode.getNodeType()!= Node.ELEMENT_NODE)
					continue;
				elementsNodeList =schemaNode.getChildNodes();
				for(k=0;k<elementsNodeList.getLength();k++)
				{
					complexTypeElements.clear();
					elementNode = elementsNodeList.item(k);
					if(elementNode.getNodeType()!= Node.ELEMENT_NODE)
						continue;
					
					// array complex type
					if(elementNode.getNodeName()=="s:complexType")
					{
						complexTypeElements = getComplexElements(elementNode);
						complexTypes.put(elementNode.getAttributes().getNamedItem("name").getNodeValue(),complexTypeElements.toArray(new String[complexTypeElements.size()]));
						continue;
					}
					
					elementChildrenNodeList = elementNode.getChildNodes();
					
					if(elementChildrenNodeList.getLength()==0)
						continue;
					
					for(l=0;l<elementChildrenNodeList.getLength();l++)
					{
						
						if(elementChildrenNodeList.item(l).getNodeName()=="s:complexType")
						{
							complexTypeNode = elementChildrenNodeList.item(l);
							if(complexTypeNode.getNodeType()!= Node.ELEMENT_NODE)
								continue;
							
							complexTypeElements = getComplexElements(complexTypeNode);
						}
					}
					
					if(complexTypeElements!=null)
					complexTypes.put(elementNode.getAttributes().getNamedItem("name").getNodeValue(),complexTypeElements.toArray(new String[complexTypeElements.size()]));
					
				}
			}
		}
		
		/*Iterator<String> keysIter = complexTypes.keySet().iterator();
		String key;
		String[] vals;
		
		String[] arrayVal;
		
		while(keysIter.hasNext())
		{
			key  = keysIter.next();
			vals = complexTypes.get(key);
			System.out.print(key+"-->");
			for(i=0;i<vals.length;i++)
				System.out.print(" "+vals[i]);
			System.out.println();
		}*/
		
		return complexTypes;
		
//		NodeList complexTypeList = doc.getElementsByTagName(complexTypeTag);	// Nodes of complex type tag
//		NodeList complexTypeChildrenList;
//		NodeList elementNodeList;										// Nodes of element tag
//		
//		Node complexTypeNode;
//		Node complexTypeChildNode;
//		Node elementNode;
//		Node attribute;
//		
//		numComplexTypes=complexTypeList.getLength();
//		complexTypes = new HashMap<String,String[]>(numComplexTypes);
//		for(i=0;i<numComplexTypes;i++)
//		{
//			complexTypeNode = complexTypeList.item(i);
//			if(complexTypeNode.getNodeType()==Node.ELEMENT_NODE) //
//			{
//				if(complexTypeNode.getParentNode().getNodeName().contains("element"))	// part of element and not array complex type
//				{
//					complexTypeName = complexTypeNode.getParentNode().getAttributes().getNamedItem("name").getNodeValue(); // complex element name 
//					// After complexType, follows sequence node. Get children of sequence node as elements
//					complexTypeChildrenList = complexTypeNode.getChildNodes();// .getChildNodes(); // sequence node
//					
//					for(j=0;j<complexTypeChildrenList.getLength();j++)
//					{
//						complexTypeChildNode = complexTypeChildrenList.item(j);
//						if(complexTypeChildNode.getNodeType() == Node.ELEMENT_NODE)
//						{
//							elementNodeList=complexTypeChildNode.getChildNodes();
//							numElements = elementNodeList.getLength();
//							complexTypeElements = new Vector<String>(numElements);
//							for(k=0;k<numElements;k++)
//							{
//								elementNode = elementNodeList.item(k);
//								if(elementNode.getNodeType()== Node.ELEMENT_NODE)
//								{
//									attribute= elementNode.getAttributes().getNamedItem("type");
//									if(attribute==null)
//										systemType="mixed";
//									else
//									{
//										systemType =  attribute.getNodeValue();	// get single type
//										if(systemType.contains(":"))	// if system type contains namespace remove it
//											if(systemType.split(":")[0]=="tns")	// theoroume oti to namespace tns periexei onomata pinakwn
//												systemType = "Array";
//											else
//												systemType = systemType.split(":")[1];
//									}
//									
//									complexTypeElements.add(systemType);
//								}
//							}
//							
//							complexTypes.put(complexTypeName,complexTypeElements.toArray(new String[complexTypeElements.size()]));
//						}
//						
//					}
//				}
//			}
//		}
	
	}
	
	public Vector<String> getComplexElements(Node complexTypeNode)
	{
		NodeList typeNodeList;	// type elements of complex type
		Node typeNode;
		Node attr;
		String type;
		Vector<String> complexTypeElements = new Vector<String>();
		int m=0;
		
		if(complexTypeNode.getChildNodes().getLength()==0)
			return complexTypeElements;
		
		typeNodeList = complexTypeNode.getChildNodes().item(1).getChildNodes();	// children of sequence node
		
		for(m=0;m<typeNodeList.getLength();m++)
		{
			if(typeNodeList.item(m).getNodeType()!=Node.ELEMENT_NODE)
				continue;
			typeNode = typeNodeList.item(m);
			attr = typeNode.getAttributes().getNamedItem("type");
			
			if(attr!=null)
			{
				type = attr.getNodeValue();
				
				if(type.startsWith("s:"))
				{
					complexTypeElements.add(type);
				}
				else if(type.toLowerCase().contains("array"))
					complexTypeElements.add("array");
				else 
					complexTypeElements.add("other");
			}
		}
		
		return complexTypeElements;
	}
	
	public Vector<String> getEnumerationValues()
	{
		Vector<String> enumerationValues = new Vector<String>();
		Document doc = createParser();
		NodeList enumerationNodeList = doc.getElementsByTagName("s:enumeration");

		if(enumerationNodeList.getLength()>0)
			for(int i=0;i<enumerationNodeList.getLength();i++)
				enumerationValues.add(enumerationNodeList.item(i).getAttributes().getNamedItem("value").getNodeValue());
			
		return enumerationValues;
	}
	
	public String getDescriptions()
	{
		StringBuffer descriptions = new StringBuffer();
		Document doc = createParser();
		NodeList descriptionNodeList = doc.getElementsByTagName("wsdl:documentation");

		if(descriptionNodeList.getLength()>0)
			for(int i=0;i<descriptionNodeList.getLength();i++)
				descriptions.append(descriptionNodeList.item(i).getTextContent());
			
		return descriptions.toString();
	}
	
	/**
	 * Get only complex types as a list of arrays of elements
	 * @return List where each position store a complex type
	 */
	public List<String[]> getComplexArrayTypes()
	{
		List<String[]> complexTypesArrays;
		
		if(complexTypes==null)
			getComplexTypesMap();
		
		complexTypesArrays = new ArrayList<String[]>(complexTypes.values());
		
		return complexTypesArrays;
	}
	
	
	/**
	 * Updates messages map where key is name of message and value an array of parameters types
	 * @return Messages map
	 */
	public HashMap<String,String[]> getMessagesStructures()
	{
		Vector<String> parametersTypes;		// Types of message parameters
		String messageName;					// Name of message
		String parameterType;					// Type of parameter
		
		Document doc = createParser();	// Document for parse
		
		NodeList messagesNodeList = doc.getElementsByTagName(messageTag);	// Message nodes
		NodeList parametersNodeList;				// Parameter nodes as children of message node
		
		Node messageNode;			// Message node
		Node parameterNode;			// Parameter node as child of message node
		Node parameterTypeNode;
		
		int i=0,j=0;
		int numMessages,numParameters;
		
		numMessages=messagesNodeList.getLength();
		messages = new HashMap<String,String[]>(numMessages);
		for(i=0;i<numMessages;i++)
		{
			messageNode = messagesNodeList.item(i);
			if(messageNode.getNodeType()==Node.ELEMENT_NODE)
			{
				messageName = messageNode.getAttributes().getNamedItem("name").getNodeValue();	 // message node name
		
				parametersNodeList = messageNode.getChildNodes();	// get parameter nodes
				numParameters = parametersNodeList.getLength();
				parametersTypes = new Vector<String>(numParameters);
				for(j=0;j<numParameters;j++)
				{
					parameterNode = parametersNodeList.item(j);
					
					if(parameterNode.getNodeType() == Node.ELEMENT_NODE)
					{
						parameterTypeNode =parameterNode.getAttributes().getNamedItem("element");
						if(parameterTypeNode==null)
							parameterTypeNode =parameterNode.getAttributes().getNamedItem("type");
						if(parameterTypeNode!=null)
						{
							parameterType = parameterTypeNode.getNodeValue();
							if(parameterType.contains(":"))	// remove namespace from parameter name
								parameterType = parameterType.split(":")[1];
							
						}
						else
							parameterType = "empty";
							
							parametersTypes.add(parameterType);
					}
				}
				messages.put(messageName,parametersTypes.toArray(new String[parametersTypes.size()]));
			}
		}
		
		return messages;
	}
	
	/**
	 * Get port messages structures and sequences
	 * @return Port message sequences
	 */
	public HashMap<String,String[]> getPortSequences()
	{
		Vector<String> messagesTypes;		// Type of message(input or output)
		Vector<String> messagesNames;		// Sequence of messages based on name
		String portTypeName;				// Name of port type
		String messageType;					// Type of message
		String messageName;					// Name of message
		
		int i=0,j=0,k=0;
		int numPortTypes,numMessages;
		
		Document doc = createParser();		// Document for parse
		
		NodeList portTypeList = doc.getElementsByTagName(portTag);		// Nodes of port type tag
		NodeList operationNodeList;										// Operation node
		NodeList messageNodeList;										// Message nodes
		
		Node portTypeNode;
		Node operationNode;
		Node messageNode;
		
		numPortTypes = portTypeList.getLength();
		portMessagesSequence = new HashMap<String,String[]>(numPortTypes);
		portMessagesStructure = new HashMap<String,String[]>(numPortTypes);
		for(i=0;i<numPortTypes;i++)
		{
			portTypeNode = portTypeList.item(i);
			if(portTypeNode.getNodeType()==Node.ELEMENT_NODE)
			{
				portTypeName = portTypeNode.getAttributes().getNamedItem("name").getNodeValue(); // complex element name 
				// After complexType, follows sequence node. Get children of sequence node as elements
				operationNodeList = portTypeNode.getChildNodes(); // operation children nodes of port type
				
				for(j=0;j<operationNodeList.getLength();j++)
				{
					operationNode = operationNodeList.item(j);
					if(operationNode.getNodeType() == Node.ELEMENT_NODE)
					{
						messageNodeList=operationNode.getChildNodes();
						numMessages = messageNodeList.getLength();
						messagesTypes = new Vector<String>(numMessages);
						messagesNames = new Vector<String>(numMessages);
						for(k=0;k<numMessages;k++)
						{
							messageNode = messageNodeList.item(k);
							if(messageNode.getNodeType()== Node.ELEMENT_NODE)
							{
								messageType= messageNode.getNodeName();		// input or output
								if(messageType.contains(":"))
									messageType = messageType.split(":")[1];
								
								if(!messageType.trim().contains("documentation")) // ignore documentation
								{
									messageName =  messageNode.getAttributes().getNamedItem("message").getNodeValue();	// get message name
									if(messageName.contains(":"))	// if system type contains namespace remove it
										messageName = messageName.split(":")[1];
									
									
									messagesTypes.add(messageType);
									messagesNames.add(messageName);
								}
							}
						}
						
						portMessagesStructure.put(portTypeName, messagesTypes.toArray(new String[messagesTypes.size()]));
						portMessagesSequence.put(portTypeName, messagesNames.toArray(new String[messagesTypes.size()]));
					}
				}
			}
		}
		
		return portMessagesSequence;
	}
	
	/*//////////////////////////////////////////////////////////////////////////
	 *					General 
	 *////////////////////////////////////////////////////////////////////////*/
	
	/**
	 * Deletes from document the following contents:
	 * 1) Name values
	 * 2) Location values
	 * 3) Text nodes
	 * 4) Style value
	 * 5) soapAction values
	 * 6) Message and Element attributes values
	 * 
	 * Then, use wvtool to extract wsdl document without content words
	 * 
	 * Purpose of this function is to extract all techinal terms from a representative wsdl document
	 * where all content deleted manually and stored to a file called "empty_content.wsdl"
	 * 
	 * @param document Path of wsdl document
	 */
	public static void getTechinalTerms(String document)
	{
		System.out.println();
		System.out.println("Extracting techinal terms from "+document+"...");
		System.out.println("Create parser...");
		
		Document wsdlDocumentParser = createParser(document);
		System.out.println("Delete content values...");
		techninalTerms(wsdlDocumentParser.getDocumentElement());
		
		/////////////////
	    //Output the XML
	
	    //set up a transformer
	    TransformerFactory transformerFactory  = TransformerFactory.newInstance();
	    try
		{
			Transformer transformer = transformerFactory .newTransformer();
		
			System.out.println("Store new document without content to "+emptyContentWSDLFile+"...");
		    DOMSource source = new DOMSource(wsdlDocumentParser);
			StreamResult result = new StreamResult(new File(emptyContentWSDLFile));
			transformer.transform(source, result);
			
			System.out.println("Extract techinal terms using WVTool from"+emptyContentWSDLFile+"...");
			WSDLDocument testWSDLDoc = new WSDLDocument(emptyContentWSDLFile);
			testWSDLDoc.parse(generalCompWordsFile);
			System.out.println("Extracting techinal terms from "+document+"..."+"Ended");
			System.out.println();
		}
		catch (TransformerConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (TransformerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Get techinal terms from children of node
	 * @param node Parent node
	 */
	public static void techninalTerms(Node node) 
	{
		// Each value of following words considered as content words
		String[] notTechninalTermAttrs = {"name","location","style","soapAction","message","element","binding","type","value","targetNamespace"}; 
		Node attrNode;	// attribute

	    NodeList nodeList = node.getChildNodes();
	    for (int i = 0; i < nodeList.getLength(); i++) 
	    {
	    	
	        Node currentNode = nodeList.item(i);
	        if (currentNode.getNodeType() == Node.ELEMENT_NODE) 
	        {
	        	// In case the node has one of attributes which value is considered as content word, empty its value
	             for(String notTechninalTermAttr:notTechninalTermAttrs)
	            	 if((attrNode=currentNode.getAttributes().getNamedItem(notTechninalTermAttr))!=null)
	            	 {
	            		 if(notTechninalTermAttr.contentEquals("type"))
	            			 if(attrNode.getNodeValue().split(":")[0].contentEquals("s"))	// primitive type so not remove
	            				 continue;
	            		 
	            		 attrNode.setNodeValue("");
	            	 }
	             

	 	        // iterate through children
	 	        techninalTerms(currentNode);
	        }
	        else if(currentNode.getNodeType() == Node.TEXT_NODE)	// In case of text node, just remove its content	
	        	currentNode.setTextContent("");      
	        
	    }
	}
	
	/**
	 * Get general computing words from generalCompWordsFile file
	 * and store them in generalComputingWords vector
	 * @throws IOException 
	 */
	public static void readGeneralComputingWords()
	{
		
		int numGeneralCompWords=0;	// number of function words contained in file
		LineNumberReader lineNumberReader=null;	// reader of line number 
		BufferedReader br=null;		// function word file reader
		try
		{
			// Open the file that is the first 
			// command line parameter
			FileInputStream fstream = new FileInputStream(generalCompWordsFile);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			 
			// Get number of general computing words equal to number of lines
			lineNumberReader = new LineNumberReader(br);
			while(lineNumberReader.readLine()!=null);
			numGeneralCompWords = lineNumberReader.getLineNumber();
			
			// Initialise general computing words
			generalComputingWords = new Vector<String>(numGeneralCompWords);
			
			// Reset stream and read each function word
			fstream.getChannel().position(0);
			br = new BufferedReader(new InputStreamReader(in));
			for(int i=0;i<numGeneralCompWords;i++)
				generalComputingWords.add(br.readLine().trim().toLowerCase());
			
			if(lineNumberReader!=null)
				lineNumberReader.close();
			
			if(br!=null)
				br.close();
			
		}
		catch(IOException ioex)
		{
			ioex.printStackTrace();
		}
	}
	
}
