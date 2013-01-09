import java.util.*;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Handler for wsdl handler as a specific type of xml handler
 */
public class WSDLDocumentHandler extends XMLHandler
{

	/*=========================================================================
	 *					Fields
	 *=========================================================================*/
	
	private final static String complexTypeTag = "s:complexType";
	private final static String messageTag = "wsdl:message";
	private final static String portTag = "wsdl:portType";
	
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
	 * @param document Path of wsdl document
	 */
	public WSDLDocumentHandler(String document)
	{
		super(document);
		// TODO Auto-generated constructor stub
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
	 * Updates complexTypes hash map where key is the name of complex element type and 
	 * value an array of element types.
	 * @return complexTypes map
	 */
	public HashMap<String,String[]> getComplexTypesMap()
	{
		//Vector<String[]> complexTypes;	// Complex types
		Vector<String> complexTypeElements;	// Complex type as an array of element
		String complexTypeName;			// Name of complex type element
		String systemType;				// Single element type ( e.g. string)
		
		int i=0,j=0,k=0;
		int numComplexTypes,numElements;
		
		Document doc = createParser();	// Document for parse
		
		NodeList complexTypeList = doc.getElementsByTagName(complexTypeTag);	// Nodes of complex type tag
		NodeList complexTypeChildrenList;
		NodeList elementNodeList;										// Nodes of element tag
		
		Node complexTypeNode;
		Node complexTypeChildNode;
		Node elementNode;
		
		numComplexTypes=complexTypeList.getLength();
		complexTypes = new HashMap<String,String[]>(numComplexTypes);
		for(i=0;i<numComplexTypes;i++)
		{
			complexTypeNode = complexTypeList.item(i);
			if(complexTypeNode.getNodeType()==Node.ELEMENT_NODE)
			{
				complexTypeName = complexTypeNode.getParentNode().getAttributes().getNamedItem("name").getNodeValue(); // complex element name 
				// After complexType, follows sequence node. Get children of sequence node as elements
				complexTypeChildrenList = complexTypeNode.getChildNodes();// .getChildNodes(); // sequence node
				
				for(j=0;j<complexTypeChildrenList.getLength();j++)
				{
					complexTypeChildNode = complexTypeChildrenList.item(j);
					if(complexTypeChildNode.getNodeType() == Node.ELEMENT_NODE)
					{
						elementNodeList=complexTypeChildNode.getChildNodes();
						numElements = elementNodeList.getLength();
						complexTypeElements = new Vector<String>(numElements);
						for(k=0;k<numElements;k++)
						{
							elementNode = elementNodeList.item(k);
							if(elementNode.getNodeType()== Node.ELEMENT_NODE)
							{
								systemType =  elementNode.getAttributes().getNamedItem("type").getNodeValue();	// get single type
								if(systemType.contains(":"))	// if system type contains namespace remove it
									systemType = systemType.split(":")[1];
								complexTypeElements.add(systemType);
							}
						}
						
						complexTypes.put(complexTypeName,complexTypeElements.toArray(new String[complexTypeElements.size()]));
					}
				}
			}
		}
		
		return complexTypes;
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
						parameterType = parameterTypeNode.getNodeValue();
						if(parameterType.contains(":"))	// remove namespace from parameter name
							parameterType = parameterType.split(":")[1];
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
}
