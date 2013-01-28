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
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
	private HashMap<String,Vector<String>> complexTypes;	// Key: Name of complex type 
													// Value: Data Array of complex types
	private HashMap<String,Vector<String>> elementTypes;	// Key: Name of element
															// Value: Data Array of element types
	private HashMap<String,Integer> primitiveTypes;	// Key: Name of primitive type
													// Value: Number of apperances in all complex types
	
	private HashMap<String,Vector<String>> messages;	// Key: Name of message
												// Value: Parameters types
	private HashMap<String,Vector<String>> messagesInSimpleTypes;	// Key:Name of message
																	// Value: Paramaeters in simple types


	private HashMap<String,Vector<String>> portMessagesSequence;			// Key: Name of port
															// Value: Type of message ( input,output)
	private HashMap<String,Vector<String>> portMessagesStructure;		// Key: Name of port
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
	public HashMap<String,Vector<String>> getComplexTypes()
	{
		if(complexTypes==null)
			parseComplexTypes();
		
		return complexTypes;
	}
	
	/**
	 * @return the elementTypes
	 */
	public HashMap<String,Vector<String>> getElementTypes()
	{
		if(elementTypes==null)
			parseComplexTypes();
		
		return elementTypes;
	}

	/**
	 * @return the primitiveTypes
	 */
	public HashMap<String, Integer> getPrimitiveTypes()
	{
		if(primitiveTypes==null)
			parseComplexTypes();
		
		return primitiveTypes;
	}
	
	/**
	 * @return the messages
	 */
	public HashMap<String,Vector<String>> getMessages()
	{
		if(messages==null)
			parseMessages();
		
		return messages;
	}
	
	/**
	 * @return the messagesInSimpleTypes
	 */
	public HashMap<String, Vector<String>> getMessagesInSimpleTypes()
	{
		if(messages==null)
			parseMessages();
		
		return messagesInSimpleTypes;
	}
	
	/**
	 * @return the portMessagesSequence
	 */
	public HashMap<String, Vector<String>> getPortMessagesSequence()
	{
		if(portMessagesSequence==null)
			getPortSequences();
		
		return portMessagesSequence;
	}

	/**
	 * @return the portMessagesStructure
	 */
	public HashMap<String,Vector<String>> getPortMessagesStructure()
	{
		if(portMessagesStructure==null)
			getPortSequences();
		
		
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
	
	/**
	 * Get name of node, split in multiple words separated by capital letters and add them
	 * to document word vector
	 * @param node Node to get name
	 */
	private void getNodeName(Node node)
	{
		NamedNodeMap attrs;
		String name;	// node name
		Vector<String> simpleWords;	// composite name in its part names splitted by Upper case or underscore
		
		attrs = node.getAttributes();
		if(attrs.getNamedItem("name")!=null)
		{
			name = attrs.getNamedItem("name").getNodeValue();
			simpleWords = WordDatabase.splitWord(name);// name.split("(?=\\p{Upper})");
			
			for(String simpleWord:simpleWords)
				this.wsdlDocument.addWord(simpleWord.toLowerCase());
			/*for(String simpleWord:simpleWordsName)
			{
				if(simpleWord.matches(".*\\d.*")) // if word contains number remove
					simpleWord = simpleWord.replaceAll("\\d","");
				
				if(!simpleWord.isEmpty() && simpleWord.length()>1 && !simpleWord.contains("_"))
					this.wsdlDocument.addWord(simpleWord.toLowerCase());
			}*/
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
		Vector<String> simpleWords = WordDatabase.splitWord(webServiceName);
				
		return simpleWords.toArray(new String[simpleWords.size()]);
	}
	
	/**
	 * Updates complexTypes hash map where key is the name of complex element type and 
	 * value an array of element types.
	 * @return complexTypes map
	 */
	public void parseComplexTypes() // HashMap<String,String[]> getComplexTypesMap()
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
		
		elementTypes = new HashMap<String,Vector<String>>();
		complexTypes = new HashMap<String,Vector<String>>();
		primitiveTypes = new HashMap<String,Integer>();
		
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
					//complexTypeElements.clear();
					elementNode = elementsNodeList.item(k);
					if(elementNode.getNodeType()!= Node.ELEMENT_NODE)
						continue;
					
					// array complex type
					if(elementNode.getNodeName()=="s:complexType")
					{
						complexTypeElements = getComplexElements(elementNode);
						complexTypes.put(elementNode.getAttributes().getNamedItem("name").getNodeValue(),complexTypeElements);
						continue;
					}
					else if(elementNode.getNodeName()=="s:element")
					{
						elementChildrenNodeList = elementNode.getChildNodes();
						
						if(elementChildrenNodeList.getLength()==0)	// simple element node
						{
							complexTypeElements =new Vector<String>();
							complexTypeElements.add( elementNode.getAttributes().getNamedItem("type").getNodeValue());
							elementTypes.put(elementNode.getAttributes().getNamedItem("name").getNodeValue(),complexTypeElements);
							
						}
						else
						{
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
						}
					
						if(complexTypeElements!=null)
							elementTypes.put(elementNode.getAttributes().getNamedItem("name").getNodeValue(),complexTypeElements);
					}
					
				}
					
			}
		}
		
		// replace element types with complex types primitive types
		Set<String> elementTypesNames = elementTypes.keySet();
		Vector<String> typesInElement;
		Vector<String> primitiveTypes=new Vector<String>();
		Iterator<String>  curTypeIter;
		String curType;
		boolean containsOnlySimpleTypes=false; 
		
		//get primitive types from complex types and elements
		for(String elementTypeName: elementTypesNames)
		{
			typesInElement = elementTypes.get(elementTypeName);
			for(String _curType:typesInElement)
				if(_curType.startsWith("s:"))
				{
					type = _curType.split(":")[1];
					if(this.primitiveTypes.containsKey(type))
						this.primitiveTypes.put(type, this.primitiveTypes.get(type)+1);
					else
						this.primitiveTypes.put(type, 1);
				}
		}
		
		elementTypesNames = complexTypes.keySet();
		for(String complexType: elementTypesNames)
		{
			typesInElement = complexTypes.get(complexType);
			for(String _curType:typesInElement)
				if(_curType.startsWith("s:"))
				{
					type = _curType.split(":")[1];
					if(this.primitiveTypes.containsKey(type))
						this.primitiveTypes.put(type, this.primitiveTypes.get(type)+1);
					else
						this.primitiveTypes.put(type, 1);
				}
		}
		
		elementTypesNames = elementTypes.keySet();
		while(!containsOnlySimpleTypes)
		{
			containsOnlySimpleTypes=true;
			for(String elementTypeName: elementTypesNames)
			{
				typesInElement = elementTypes.get(elementTypeName);
				
				curTypeIter = typesInElement.iterator();
				primitiveTypes=new Vector<String>();
				while(curTypeIter.hasNext())
				{
					curType = curTypeIter.next();
					if(curType.startsWith("tns:"))
					{
						containsOnlySimpleTypes=false;
						curTypeIter.remove();
						//typesInElement.remove(curType);	// remove complex type as reference from element 
						if(complexTypes.containsKey(curType.split(":")[1]))	// check if name is contained in complex types
							primitiveTypes.addAll(complexTypes.get(curType.split(":")[1]));
						else
							primitiveTypes.add("enum");
					}
				}
				
				typesInElement.addAll(primitiveTypes);
			}
		}
	}
	
	/**
	 * Get elements of a complex element
	 * @param complexTypeNode Complex type
	 * @return Simple elements of complex element
	 */
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
				complexTypeElements.add(type);
			}
			else
				complexTypeElements.add("null");
		}
		
		return complexTypeElements;
	}

	/**
	 * Parses messages and replaces its one with primitive types by replacing complex elements with simple types
	 */
	public void parseMessages()
	{
		getMessagesStructures();
		Iterator<Entry<String,Vector<String>>> messageIter =this.messages.entrySet().iterator();	// iterator for messages
		String currentMessage;			// current message
		Iterator<String> parameters;	// iterator for messages of each parameter
		String curParameter;			// current parameter in iteration
		Vector<String> primitiveTypes;	// primitive types for current parameter
		
		this.messagesInSimpleTypes = new HashMap<String,Vector<String>>();
		
		while(messageIter.hasNext())
		{
			currentMessage = messageIter.next().getKey();
			primitiveTypes=new Vector<String>();
			parameters = this.messages.get(currentMessage).iterator();	//	Get iterator of current message's parameter
			while(parameters.hasNext())
			{
				curParameter = parameters.next();
				if(curParameter.startsWith("tns:"))	// parameter is element. Replace each element with primitive types
				{
					//parameters.remove();	// remove current parameter
					//if(this.elementTypes!=null)
					if(this.elementTypes.containsKey(curParameter.split(":")[1]))
						primitiveTypes.addAll(this.elementTypes.get(curParameter.split(":")[1]));
						
						if(primitiveTypes.isEmpty())
							primitiveTypes.add("null");
				}
				else
					primitiveTypes.add(curParameter);
			}
			
			if(!primitiveTypes.isEmpty())
				this.messagesInSimpleTypes.put(currentMessage, primitiveTypes);
		}
		

	}
	
	/**
	 * Updates messages map where key is name of message and value an array of parameters types
	 * @return Messages map
	 */
	public HashMap<String,Vector<String>> getMessagesStructures()
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
		messages = new HashMap<String,Vector<String>>(numMessages);
		for(i=0;i<numMessages;i++)
		{
			messageNode = messagesNodeList.item(i);
			if(messageNode.getNodeType()==Node.ELEMENT_NODE)
			{
				messageName = messageNode.getAttributes().getNamedItem("name").getNodeValue();	 // message node name
		
				parametersNodeList = messageNode.getChildNodes();	// get parameter nodes
				numParameters = parametersNodeList.getLength();
				parametersTypes = new Vector<String>(numParameters);
				
				if(numParameters==0)
					parametersTypes.add("null");
				else
				{
					
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
								//if(parameterType.contains(":"))	// remove namespace from parameter name
									//parameterType = parameterType.split(":")[1];
								
							}
							else
								parameterType = "empty";
								
							parametersTypes.add(parameterType);
						}
					}
				}
				messages.put(messageName,parametersTypes);
			}
		}
		
		return messages;
	}
	
	/**
	 * Get port messages structures and sequences
	 * @return Port message sequences
	 */
	public HashMap<String,Vector<String>> getPortSequences()
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
		portMessagesSequence = new HashMap<String,Vector<String>>(numPortTypes);
		portMessagesStructure = new HashMap<String,Vector<String>>(numPortTypes);
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
						
						portMessagesStructure.put(portTypeName, messagesTypes);
						portMessagesSequence.put(portTypeName, messagesNames);
					}
				}
			}
		}
		
		return portMessagesSequence;
	}
	
	/**
	 * Get values from enumeration types
	 * @return Values of all enumerations
	 */
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
	
	/**
	 * Get all description in one text
	 * @return Descriptions of wsdl document
	 */
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
	 * Update database with general computing words reading corresponding file
	 */
	public static void updateGeneralComputingWordsDB()
	{
		
		int numGeneralCompWords=0;	// number of function words contained in file
		LineNumberReader lineNumberReader=null;	// reader of line number 
		BufferedReader br=null;		// function word file reader
		
		// delete all entries
		WebServicesClusterer.em.getTransaction().begin();
		Query query = WebServicesClusterer.em.createQuery(
			      "DELETE FROM GeneralComputingWord");
		query.executeUpdate();
				
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
			
			WebServicesClusterer.em.persist(new GeneralComputingWord(generalComputingWords));
			WebServicesClusterer.em.getTransaction().commit();
			
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
	
	/**
	 * Get general computing words from database
	 * and store them in generalComputingWords vector
	 * @throws IOException 
	 */
	public static void readGeneralComputingWords()
	{
		TypedQuery<GeneralComputingWord> query = WebServicesClusterer.em.createQuery(
			      "SELECT gcw FROM GeneralComputingWord AS gcw ", GeneralComputingWord.class);
		GeneralComputingWord generalComputingWord = query.getResultList().get(0);
		
		generalComputingWords = generalComputingWord.getWords();
	}
	
}
