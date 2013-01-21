import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.apache.commons.collections.map.MultiKeyMap;

/**
 * Parsing the WSDL document of a web service to extract features that describe
 * the semantic and behavior of the web service. These features are:
 * i.   WSDL content
 * ii.  WSDL types
 * iii. WSDL messages
 * iv.	WSDL ports
 * v.	Web service name  
 * 
 * Based on these features, all web services of collection are clustered into
 * functionally similar groups using Quality Threshold clustering algorithm
 */
public class WebServicesClusterer
{
	/*=========================================================================
	 *					Fields
	 *=========================================================================*/
	
	private Vector<String> WSDLDocuments;					// Names of wsdl documents to cluster 
	private Vector<WSDLDocument> WSDLDocumentsC; 			// Collection of web services descriptions 
	private Hashtable<String,Integer> n_w;					// Number of documents contain word w ( Key: Keyword, Value: Number)
	//private Hashtable<String,Double> n_w_;					// Estimated document frequency
	//private Hashtable<String,Double> Lambda_w;				// Overestimator factor for each word
	//private double Lambda_threhold = 1.0;					// Threshold lambda
	
	List<WebServicesCluster> clusters;						// Clusters of wsdl documents with similar functionality
	
	// for each pair of web services
	private MultiKeyMap wsdlComplexTypesMatch;					// Type matches 
	private MultiKeyMap wsdlMessagesMatch;						// Message matches					
	private MultiKeyMap wsdlPortsMatch;						// Ports matches
	private MultiKeyMap webServiceNameSimilarity;				// Name matches
	private MultiKeyMap similarityFactor;						// Similarity factor È
	
	private WordDatabase searchEnginewordDB;	// Word database instance where similarity is based on search engine results
	private WordDatabase wordNetWordDB;			// Word database instance where similarity is based on wordnet
	
	public  double similarityFactorThreshold = 0.7;		// Similarity Factor theta treshold above which web services match
	
	// Filenames
	private static String clusterFilename="clusters.dat";	// Clusters and their collection of documents
	private static String similarityResFilename= "similarity_results.dat";	// Matches and similarity value
	
	// Output file for similarity values for each pair of web services
	FileWriter similarityFStream;
	BufferedWriter similarityBufferedOutput;
	
	
	/*=========================================================================
	 *					Constructors
	 *=========================================================================*/

	/**
	 * Constructs a web services clustering process in a collection of wsdl documents
	 * 
	 * @param WSDLDocuments Vector of wsdl documents collection
	 * @param similarityFactorThreshold Similarity Factor treshold for web services match
	 */
	public WebServicesClusterer(Vector<String> WSDLDocuments,double similarityFactorThreshold)
	{
		// WSDL Documents collection
		this.WSDLDocuments=WSDLDocuments;
		this.WSDLDocumentsC = new Vector<WSDLDocument>(WSDLDocuments.size());
		this.similarityFactorThreshold= similarityFactorThreshold;
		
		// For each wsdl document create a WSDLDocument object reflected to this web service description
		for(String WSDLDocument:WSDLDocuments)
			this.WSDLDocumentsC.add(new WSDLDocument(WSDLDocument));
		
		this.n_w = new Hashtable<String,Integer>();
		this.searchEnginewordDB = new WordDatabase(SimilarityType.NGD,SearchEngine.GOOGLE);
		this.wordNetWordDB = new WordDatabase(SimilarityType.WordNet,SearchEngine.NONE);
		
		// Get general computing words 
		WSDLDocumentHandler.readGeneralComputingWords();
		searchEnginewordDB.readFunctionWords();
		
		// Open file to write similarity results
		try
		{
			similarityFStream = new FileWriter(similarityResFilename);
			similarityBufferedOutput = new BufferedWriter(similarityFStream);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	protected void finalize() throws Throwable
	{
		similarityBufferedOutput.close();
		super.finalize(); //not necessary if extending Object.
	}
	
	/*=========================================================================
	 *					Getters
	 *=========================================================================*/
	
	public Vector<String> getWSDLDocuments() 
	{
		return WSDLDocuments;
	}
	
	public Hashtable<String, Integer> getN_w()
	{
		return n_w;
	}

	/**
	 * @return the clusters
	 */
	public List<WebServicesCluster> getClusters()
	{
		return clusters;
	}

	/*=========================================================================
	 *					Setters
	 *=========================================================================*/

	/**
	 * Set collection of wsdl documents to cluster
	 * @param wSDLDocuments
	 */
	public void setWSDLDocuments(Vector<String> wSDLDocuments) 
	{
		WSDLDocuments = wSDLDocuments;
	}
	
	/*=========================================================================
	 *					Methods
	 *=========================================================================*/
	
	/*//////////////////////////////////////////////////////////////////////////
	 *					Main 
	 *////////////////////////////////////////////////////////////////////////*/
	
	/**
	 * Cluster web services into similar functionally groups
	 * 
	 *   @return A list of clusters where each cluster contain its wsdl documents 
	 */
	public List<WebServicesCluster> cluster()
	{
		// Preprocessing
		System.out.println("Preprocessing - Extract Features from wsdl documents...");
		extractFeatures();
		
		// Cluster
		System.out.print("Clustering using QT algorithm with minimum similarity factor "+similarityFactorThreshold+"...");
		this.clusters = clusterWebServices();
	
		
		// Writing results
		System.out.println("Ended");
		
		System.out.println("\n-----------------------------------------------");
		System.out.println("Number of clusters: "+this.clusters.size());
		for(int i=0;i<clusters.size();i++)
			System.out.println("Cluster "+clusters.get(i).getIndex()+" : "+clusters.get(i).getDocumentNames());
		System.out.println("-----------------------------------------------");
		
		System.out.println();
		System.out.println();
		System.out.print("Writing clusters to "+clusterFilename+"...");
	
		
		try
		{
			FileWriter fileWriter= new FileWriter(clusterFilename);
			BufferedWriter out = new BufferedWriter(fileWriter);
			
			for(int i=0;i<clusters.size();i++)
			{
				out.write("Cluster "+(i+1)+" : ");
				
				for(int j=0;j<clusters.get(i).getDocuments().size();j++)
					out.write(clusters.get(i).getDocuments().get(j).getNameWithoutExtension()+"\t");
				out.newLine();
			}
			
			out.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Ended");
		
		return this.clusters;
	}
	
	/**
	 * Extract features from wsdl documents and find matches between
	 * complex types, messages, ports and web service names for each pair of documents
	 */
	private void extractFeatures()
	{
		extractWSDLContent();
		findMatches();
	}
	
	/*//////////////////////////////////////////////////////////////////////////
	 *					Extract Features 
	 *////////////////////////////////////////////////////////////////////////*/
	
	/**
	 * Extracts a list of content words for each web service description
	 */
	private void extractWSDLContent()
	{
		Vector<String> words;			// words as extracted from document before removing function and non-content words
		Vector<String> contentWords;	// words after removing function and general computin words
		String wordsFile;				// file to store words
		WSDLDocumentHandler documentHandler;	// wsdl document handler
		FileWriter fstream;
		BufferedWriter out;
		
		System.out.println("\t1. Extract wsdl content...");
		
		
		// Parse wsdl document extracting words without xml tags
		// and reduced to their stem
		for(WSDLDocument document:WSDLDocumentsC)
		{
			 // Extract content
			System.out.println("\t\t Processing "+document.getName()+" -- >");
			System.out.println("\t\t\t Step 1 : Extract Content... ");
			documentHandler = new WSDLDocumentHandler(document);
			documentHandler.extractContent();
			
			wordsFile = "document_words/"+document.getNameWithoutExtension()+"_words.dat";	// file to store words extracted from wsdl document
			System.out.println("\t\t\t... Write words to "+wordsFile);
			
			words= document.getWords(false);

			try
			{
				fstream = new FileWriter(wordsFile);
				 out = new BufferedWriter(fstream);
				
				for(String word:words)
				{
					out.write(word);
					out.newLine();
				}
				
				out.close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Remove function words
			System.out.println("\t\t\tStep 2 : Remove Function Words... ");
			removeFunctionWords(document);
			// Remove general computing words
			System.out.println("\t\t\tStep 3 : Remove General Computing Words... ");
			recogniseContentWords(document);
			
			wordsFile = "document_words/"+document.getNameWithoutExtension()+"_content.dat";	// file to store words extracted from wsdl document
			System.out.println("\t\t\t... Write content to "+wordsFile);
			contentWords = document.getWords(true); // get map of
			try
			{
				fstream = new FileWriter(wordsFile);
				out = new BufferedWriter(fstream);
				
				for(String contentWord:contentWords)
				{
					out.write(contentWord);
					out.newLine();
				}
				
				out.close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//overestimationFactor();
		
//		for(WSDLDocument document:WSDLDocumentsC)
//		{
//			removeFunctionWords(document);
//		//	recogniseContentWords(document);
//		}
	}
	
	/**
	 * Compute matches for each pair of web services in types,messages,ports and web service name
	 */
	private void findMatches()
	{
		int numDocuments = WSDLDocumentsC.size();	// number of documents
		WSDLDocument wsdlDoc1;						// first document of pair
		WSDLDocument wsdlDoc2;						// second document of pair
		double match;								// result of match value
		int i=0,j=0;								
		
		System.out.println("\t2. Find matches...");
		
		// initialize multi key maps to store matches values for each pair of wsdl documents
		wsdlComplexTypesMatch = new MultiKeyMap();
		wsdlMessagesMatch = new MultiKeyMap();
		wsdlPortsMatch = new MultiKeyMap();
		webServiceNameSimilarity =new MultiKeyMap();
		
		MultiKeyMap complexTypeMatchesFlags;	// contains the status matches on messages for each pair of documents 
		MultiKeyMap messageMatchesFlags;	
		
		for(i=0;i<numDocuments;i++)
		{
			wsdlDoc1 = WSDLDocumentsC.get(i);
			
			for(j=i+1;j<numDocuments;j++)
			{
				wsdlDoc2 = WSDLDocumentsC.get(j);
				
				// If pair already exists continue 
				if(wsdlComplexTypesMatch.containsKey(wsdlDoc1, wsdlDoc2)
						|| wsdlComplexTypesMatch.containsKey(wsdlDoc2, wsdlDoc1))	 
					continue;
				
				System.out.println("\t\tDocuments pair: "+wsdlDoc1.getName()+" "+wsdlDoc2.getName());
				
				complexTypeMatchesFlags = new MultiKeyMap();
				messageMatchesFlags = new MultiKeyMap();
//				
//				// WSDL Types
				System.out.print("\t\t\tWSDL ComplexTypes...");
				match = matchWSDLTypes(wsdlDoc1,wsdlDoc2,complexTypeMatchesFlags);
				wsdlComplexTypesMatch.put(wsdlDoc1, wsdlDoc2,match);
				System.out.println(match);
				
//				// WSDL Messages
				System.out.print("\t\t\tWSDL Messages...");
				match = matchWSDLMessages(wsdlDoc1,wsdlDoc2,complexTypeMatchesFlags,messageMatchesFlags);
				wsdlMessagesMatch.put(wsdlDoc1, wsdlDoc2,match);
				System.out.println(match);
//				
//				// WSDL Port Types
				System.out.print("\t\t\tWSDL Port Types...");
				match = matchWSDLPorts(wsdlDoc1,wsdlDoc2,messageMatchesFlags);
				wsdlPortsMatch.put(wsdlDoc1, wsdlDoc2,match);
				System.out.println(match);
				
				// Web Services names
				System.out.print("\t\t\tWeb Service Names...");
				match = webServiceNameSimilarity(wsdlDoc1,wsdlDoc2);
				webServiceNameSimilarity.put(wsdlDoc1, wsdlDoc2,match);
				System.out.println(match);
			}
		}
	}
	
	/**
	 * Match between a pair of web services in complex element types
	 * 
	 * @param WSDLDocument_i Web service i
	 * @param WSDLDocument_j Web service j
	 * @return Match on complex element types 
	 */
	private double matchWSDLTypes(WSDLDocument WSDLDocument_i,WSDLDocument WSDLDocument_j,MultiKeyMap complexTypeMatchesFlags)
	{
		int M=0;	// number of matched types
		double avg;	// average of total number of defined types between the two web services
		double match = 0.0;
		boolean isMatch=true;
		boolean _isMatch=true;
		
		int numDoc1ComplexTypes;	// num of complex types for first web service
		int numDoc2ComplexTypes;	// num of complex types for second web service
		int numTypes;		// number of types in complex type
		int k=0;
		// get handlers for each one of two documents
		WSDLDocumentHandler docHandler1 = new WSDLDocumentHandler(WSDLDocument_i);
		WSDLDocumentHandler docHandler2 = new WSDLDocumentHandler(WSDLDocument_j);
		
		// get each complex type as array of single elements
		HashMap<String,String[]> doc1ComplexTypes = docHandler1.getComplexTypesMap();
		HashMap<String,String[]> doc2ComplexTypes = docHandler2.getComplexTypesMap();
		numDoc1ComplexTypes = doc1ComplexTypes.size();
		numDoc2ComplexTypes = doc2ComplexTypes.size();
		String doc1ComplexTypeName;
		String doc2ComplexTypeName;
		List<String> doc1ComplexType;	
		List<String> doc2ComplexType;
		Iterator<String> typeIterator;
		String type;
		
		avg = (numDoc1ComplexTypes + numDoc2ComplexTypes)/2.0;	// average of total number of defined types
		
		// For each pair of complex types between the two web services , find if there is a match 
		// 1) In number of single elements
		// 2) If 1 is true, then check if every single element is the same type
		Iterator<String> doc1ComplexTypesIter = doc1ComplexTypes.keySet().iterator();
		Iterator<String> doc2ComplexTypesIter;
		while(doc1ComplexTypesIter.hasNext()) // doc 1 complex type
		{
			doc1ComplexTypeName = doc1ComplexTypesIter.next();
			doc1ComplexType = new LinkedList<String>(Arrays.asList(doc1ComplexTypes.get(doc1ComplexTypeName)));
			doc2ComplexTypesIter = doc2ComplexTypes.keySet().iterator();
			while(doc2ComplexTypesIter.hasNext())	// doc 2 complex type
			{
				isMatch = true;
				
				doc2ComplexTypeName = doc2ComplexTypesIter.next();
				// If true, check if every pair of types matches
				doc2ComplexType = new LinkedList<String>(Arrays.asList(doc2ComplexTypes.get(doc2ComplexTypeName)));
				if(doc1ComplexType.size()==doc2ComplexType.size())	// check for same length
				{
					
					numTypes = doc2ComplexType.size();	
					typeIterator = doc2ComplexType.iterator();
					
					while(typeIterator.hasNext())
					{
						type = typeIterator.next();
						if(!doc1ComplexType.contains(type))
						{
							isMatch=false;
							break;
						}
						
						typeIterator.remove();	
					}
				}
				else
					isMatch = false;
						
				// increase matches
				if(isMatch)
				{
					M++;
					doc2ComplexTypesIter.remove();
					// insert result of match for this pair in messageMatchesFlags
					//messageMatchesFlags.put(doc1ComplexTypeName, doc2ComplexTypeName,isMatch);
					break;	// find one match
				}
			}
		}
		
		// Fill message matches flags
		doc1ComplexTypesIter = doc1ComplexTypes.keySet().iterator();
		doc2ComplexTypes = docHandler2.getComplexTypesMap();
		while(doc1ComplexTypesIter.hasNext()) // doc 1 complex type
		{
			doc1ComplexTypeName = doc1ComplexTypesIter.next();
			doc1ComplexType =  Arrays.asList(doc1ComplexTypes.get(doc1ComplexTypeName));
			doc2ComplexTypesIter = doc2ComplexTypes.keySet().iterator();
			while(doc2ComplexTypesIter.hasNext())	// doc 2 complex type
			{
				isMatch = true;
				
				doc2ComplexTypeName = doc2ComplexTypesIter.next();
				doc2ComplexType =new LinkedList<String>(Arrays.asList(doc2ComplexTypes.get(doc2ComplexTypeName)));
				if(doc1ComplexType.size()==doc2ComplexType.size())	// check for same length
				{
					// If true, check if every pair of types matches
					
					numTypes = doc1ComplexType.size();	
					typeIterator = doc2ComplexType.iterator();
					
					while(typeIterator.hasNext())
					{
						if(!doc1ComplexType.contains(typeIterator.next()))
						{
							isMatch=false;
							break;
						}
						
						typeIterator.remove();
					}
				}
				else
					isMatch = false;
						
				complexTypeMatchesFlags.put(doc1ComplexTypeName, doc2ComplexTypeName,isMatch);
			}
		}
		
		// get match
		match = M/avg;
		
		return match;
	}
	
	/**
	 * Match between a pair of web services in message's structure
	 * 
	 * @param WSDLDocument_i Web service i
	 * @param WSDLDocument_j Web service j
	 * @param messageMatchesFlags Map where keys are a pair of messages from each service and value is true if there is match 
	 * @return Match on message structure
	 */
	private double matchWSDLMessages(WSDLDocument WSDLDocument_i,WSDLDocument WSDLDocument_j,MultiKeyMap complexTypeMatchesFlags
			,MultiKeyMap messageMatchesFlags)
	{
		int M=0;	// number of matched types
		double avg;	// average of total number of defined types between the two web services
		double match = 0.0;
		boolean isMatch=true;
		
		int numDoc1Messages;	// num of messages for first web service
		int numDoc2Messages;	// num of messages for second web service
		int numParameters;		// number of parameters in one of two messages
		int k=0;
		// get handlers for each one of two documents
		WSDLDocumentHandler docHandler1 = new WSDLDocumentHandler(WSDLDocument_i);
		WSDLDocumentHandler docHandler2 = new WSDLDocumentHandler(WSDLDocument_j);
		
		// get each complex type as array of single elements
		HashMap<String,String[]> doc1Messages = docHandler1.getMessagesStructures();
		HashMap<String,String[]> doc2Messages = docHandler2.getMessagesStructures();
		numDoc1Messages = doc1Messages.size();
		numDoc2Messages = doc2Messages.size();
		String doc1MessageName;
		String doc2MessageName;
		String[] doc1Message;	
		String[] doc2Message;
		
		avg = (numDoc1Messages + numDoc2Messages)/2.0;	// average of total number of defined types
		
		/*Iterator<String> doc1MessagesIter = doc1Messages.keySet().iterator();
		Iterator<String> doc2MessagesIter;
		while(doc1MessagesIter.hasNext()) // doc 1 complex type
		{
			doc1MessageName = doc1MessagesIter.next();
			doc2MessagesIter = doc2Messages.keySet().iterator();
			while(doc2MessagesIter.hasNext())	// doc 2 complex type
			{
				doc2MessageName = doc2MessagesIter.next();
				portTypeMatchesFlags.put(doc1MessageName, doc2MessageName,false);
			}
		}*/
		
		// For each pair of complex types between the two web services , find if there is a match 
		// 1) In number of single elements
		// 2) If 1 is true, then check if every single element is the same type
		Iterator<String> doc1MessagesIter = doc1Messages.keySet().iterator();
		Iterator<String> doc2MessagesIter;
		while(doc1MessagesIter.hasNext()) // doc 1 complex type
		{
			doc1MessageName = doc1MessagesIter.next();
			doc1Message = doc1Messages.get(doc1MessageName);
			doc2MessagesIter = doc2Messages.keySet().iterator();
			while(doc2MessagesIter.hasNext())	// doc 2 complex type
			{
				isMatch = true;
				
				doc2MessageName = doc2MessagesIter.next();
				doc2Message = doc2Messages.get(doc2MessageName);
				if(doc1Message.length==doc2Message.length)	// check for same length
				{
					// If true, check if every pair of messages matches
					
					numParameters = doc1Message.length;	
					
					for(k=0;k<numParameters;k++)
					{
						// if messages not contained in message matches map then they are single
						if(!complexTypeMatchesFlags.containsKey(doc1Message[k],doc2Message[k]))
						{
							if(!doc1Message[k].contentEquals(doc2Message[k]))
								isMatch=false;
						}
						else if((boolean)complexTypeMatchesFlags.get(doc1Message[k],doc2Message[k])==false)
								isMatch=false;
						/*else
						{
							// if two above checks passed, then two elements are complex
							if(((boolean)messageMatchesFlags.get(doc1Message[k],doc2Message[k]))==false)
								isMatch=false;
						}*/
							
						if(!isMatch)
							break;
					}
				
						
				}
				else
					isMatch = false;
				
				// increase matches
				if(isMatch)
				{
					M++;
					doc2MessagesIter.remove();

					break;	// find one match
				}
			}
		}
		
		// Fill portTypeMatchesFlags
		doc1MessagesIter = doc1Messages.keySet().iterator();
		doc2Messages =  docHandler2.getMessagesStructures();
		while(doc1MessagesIter.hasNext()) // doc 1 complex type
		{
			doc1MessageName = doc1MessagesIter.next();
			doc1Message = doc1Messages.get(doc1MessageName);
			doc2MessagesIter = doc2Messages.keySet().iterator();
			while(doc2MessagesIter.hasNext())	// doc 2 complex type
			{
				isMatch = true;
				
				doc2MessageName = doc2MessagesIter.next();
				doc2Message = doc2Messages.get(doc2MessageName);
				if(doc1Message.length==doc2Message.length)	// check for same length
				{
					// If true, check if every pair of messages matches
					
					numParameters = doc1Message.length;	
					
					for(k=0;k<numParameters;k++)
					{
						// if messages not contained in message matches map then they are single
						if(!complexTypeMatchesFlags.containsKey(doc1Message[k],doc2Message[k]))
						{
							if(!doc1Message[k].contentEquals(doc2Message[k]))
								isMatch=false;
						}
						else if((boolean)complexTypeMatchesFlags.get(doc1Message[k],doc2Message[k])==false)
								isMatch=false;
						/*else
						{
							// if two above checks passed, then two elements are complex
							if(((boolean)messageMatchesFlags.get(doc1Message[k],doc2Message[k]))==false)
								isMatch=false;
						}*/
							
						if(!isMatch)
							break;
					}
				
						
				}
				else
					isMatch = false;
				
				messageMatchesFlags.put(doc1MessageName, doc2MessageName,isMatch);
			}
		}
		
		// get match
		match = M/avg;
		
		return match;
	}
	
	/**
	 * Match between a pair of web services in ports
	 * 
	 * @param WSDLDocument_i Web service i
	 * @param WSDLDocument_j Web service j
	 * @return Match on port
	 */
	private double matchWSDLPorts(WSDLDocument WSDLDocument_i,WSDLDocument WSDLDocument_j,MultiKeyMap messageMatchesFlags)
	{
		int M=0;	// number of matched types
		double avg;	// average of total number of defined types between the two web services
		double match = 0.0;
		boolean isMatch=true;
		
		int numDoc1PortTypes;	// num of messages for first web service
		int numDoc2PortTypes;	// num of messages for second web service
		int numMessageTypes;		// number of message types( input,output or both)
		
		int k=0;
		
		// get handlers for each one of two documents
		WSDLDocumentHandler docHandler1 = new WSDLDocumentHandler(WSDLDocument_i);
		WSDLDocumentHandler docHandler2 = new WSDLDocumentHandler(WSDLDocument_j);
		
		// get each complex type as array of single elements
		HashMap<String,String[]> doc1PortSequences = docHandler1.getPortSequences();
		HashMap<String,String[]> doc2PortSequences = docHandler2.getPortSequences();
		HashMap<String,String[]> doc1PortStructures = docHandler1.getPortMessagesStructure();
		HashMap<String,String[]> doc2PortStructures = docHandler2.getPortMessagesStructure();
		numDoc1PortTypes = doc1PortSequences.size();
		numDoc2PortTypes = doc1PortSequences.size();
		String doc1PortTypeName;
		String doc2PortTypeName;
		String[] doc1Structure, doc1Sequence;	
		String[] doc2Structure,doc2Sequence;
		
		avg = (numDoc1PortTypes + numDoc2PortTypes)/2.0;	// average of total number of defined types
		
		// For each pair of complex types between the two web services , find if there is a match 
		// 1) In number of single elements
		// 2) If 1 is true, then check if every single element is the same type
		Iterator<String> doc1PortTypesIter = doc1PortSequences.keySet().iterator();
		Iterator<String> doc2PortTypesIter;
		while(doc1PortTypesIter.hasNext()) // doc 1 complex type
		{
			doc1PortTypeName = doc1PortTypesIter.next();
			doc1Sequence = doc1PortSequences.get(doc1PortTypeName);
			doc1Structure = doc1PortStructures.get(doc1PortTypeName);
			doc2PortTypesIter = doc2PortSequences.keySet().iterator();
			while(doc2PortTypesIter.hasNext())	// doc 2 complex type
			{
				isMatch = true;
				
				doc2PortTypeName = doc2PortTypesIter.next();
				doc2Sequence = doc2PortSequences.get(doc2PortTypeName);
				doc2Structure = doc2PortStructures.get(doc2PortTypeName);
				if(doc1Sequence.length==doc2Sequence.length)	// check for same length
				{
					// If true, check if every pair of messages matches
					
					numMessageTypes =doc1Sequence.length;
					for(k=0;k<numMessageTypes;k++)
					{
						// check if both sequence and structure match
						if(!doc1Structure[k].contentEquals(doc2Structure[k]))
							isMatch=false;
						else if((boolean)messageMatchesFlags.get(doc1Sequence[k],doc2Sequence[k])==false)
							isMatch=false;
						/*((boolean)portTypeMatchesFlags.get(doc1Sequence[k],doc2Sequence[k]))==false)
						{
							v
							break;
						}*/
						
						if(!isMatch)
							break;
					
					}
				
				}
				else
					isMatch = false;
				
				// increase matches
				if(isMatch)
				{
					M++;
					doc2PortTypesIter.remove();
					break;
				}
			}
		}
		
		// get match
		match = M/avg;
		
		return match;
	}
	
	/**
	 * Get similarity of web services in names as extracted from <wsdl:service> node
	 * 
	 * @param WSDLDocument_i Web service i
	 * @param WSDLDocument_j Web service j
	 * @return Similarity between services names
	 */
	private double webServiceNameSimilarity(WSDLDocument WSDLDocument_i,WSDLDocument WSDLDocument_j)
	{
		double similarity = 0.0;
		WSDLDocumentHandler wsdlDocumentHandler_i= new WSDLDocumentHandler(WSDLDocument_i);
		WSDLDocumentHandler wsdlDocumentHandler_j = new WSDLDocumentHandler(WSDLDocument_j);
		
		similarity = searchEnginewordDB.correlation(wsdlDocumentHandler_i.getWebServiceNameSplit(),
				wsdlDocumentHandler_j.getWebServiceNameSplit(),false);
		
		return similarity;
		
		/*try
		{
			out.write(WSDLDocument_i.getNameWithoutExtension()+"\t"+WSDLDocument_j.getNameWithoutExtension()+"\t"+similarity);
			out.newLine();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		/*for(int i=0;i<ws1name.length;i++)
			_ws1name.append(ws1name[i]+" ");
		for(int i=0;i<ws2name.length;i++)
			_ws2name.append(ws2name[i]+" ");
		similarity = wordDB.correlation(new String[]{_ws1name.toString().trim()},
				new String[]{_ws2name.toString().trim()},false);*/
		/*similarity = wordDB.correlation(new String[]{WSDLDocument_i.getNameWithoutExtension()},
				new String[]{WSDLDocument_j.getNameWithoutExtension()},false);
				//similarity = wordDB.correlation(ws1name,ws2name,false);
		
		return similarity;*/
	}
	
	/*//////////////////////////////////////////////////////////////////////////
	 *					Clustering
	 *////////////////////////////////////////////////////////////////////////*/
	
	/**
	 * Clusters web services using features extracted in preprocessing using
	 * QT algorithm.
	 */
	private List<WebServicesCluster> clusterWebServices()
	{
		List<WebServicesCluster> clusters =  new ArrayList<WebServicesCluster>(10);	// Vector of clusters
		WebServicesCluster cluster;													// New cluster created
		int clusterIndx=0;															// Cluster id
		List<WSDLDocument> clusteredDocs = new ArrayList<WSDLDocument>();			// Clustered documents
		double minTheta;															// Minimum similarity factor between current document and cluster documents
		this.similarityFactor = new MultiKeyMap();							
		
		while(clusteredDocs.size()!=WSDLDocuments.size())	// while exists non clustered documents
		{
			cluster = new WebServicesCluster(++clusterIndx);
			clusters.add(cluster); // create new clustered for non clustered documents
			
			System.out.println("New cluster created with index - "+clusterIndx);
			
			for(WSDLDocument wSDLDocument:WSDLDocumentsC)
			{
				// if document is clustered, continue
				if(clusteredDocs.contains(wSDLDocument))
					continue;
			
				System.out.println("...Examining document "+wSDLDocument.getName()+"...");
				
				// new empty cluster so add the first not clustered document
				if(cluster.getDocuments().size()==0)
				{
					System.out.println("......Added to (empty) cluster "+clusterIndx);
					cluster.add(wSDLDocument);
					clusteredDocs.add(wSDLDocument);
					continue;
				}
				
				// Find minimum similarity factor between current document and each document from cluster
				// If minimum similarity factor is greater than threshold, insert document in cluster
				minTheta = minSimilarityFactor(cluster,wSDLDocument);
				System.out.println("......Min È: "+minTheta);
				if(minTheta>=similarityFactorThreshold)
				{
					cluster.add(wSDLDocument);
					clusteredDocs.add(wSDLDocument);
					
					System.out.println("......Added to cluster "+cluster.getIndex()+"["+cluster.getDocumentNames()+"]");
				}
			}
		}
		
		return clusters;
	}
	
	/**
	 * Find the minimum theta between examining document and  each one of cluster documents 
	 * @param cluster WSDL documents cluster
	 * @param document Examining document
	 * @return Minimum similarity factor in cluster
	 */
	private double minSimilarityFactor(WebServicesCluster cluster,WSDLDocument document)
	{
		double minTheta = Double.MAX_VALUE;		// minimum similarity factor
		double theta;							// similarity factor
		List<WSDLDocument> clusteredWSDLDocuments = cluster.getDocuments();	// WSDL Documents belong to cluster
		
		// For each document in cluster , find similarity factor between this and examing document
		for(WSDLDocument clusteredWSDLDocument:clusteredWSDLDocuments)
		{
			theta = similarityFactor(document,clusteredWSDLDocument);
			this.similarityFactor.put(clusteredWSDLDocument, document,theta);	// store value of documents pair in similarity factor structure
			minTheta =Math.min(theta, minTheta);
			
		}
		
		return minTheta;
	}
	
	/**
	 * Similarity factor È(s_i,s_j) between a pair of web services based
	 * on five similarity features 
	 * 
	 * @param WSDLDocument_i Web service i
	 * @param WSDLDocument_j Web service j
	 * @return Similarity factor 
	 */
	private double similarityFactor(WSDLDocument WSDLDocument_i,WSDLDocument WSDLDocument_j)
	{
		double theta = 0.0;	// similarity factor
		
		// if document is not the first one in pair, check for reverse store order in name similarity
		if(!webServiceNameSimilarity.containsKey(WSDLDocument_i))
		{
			WSDLDocument WSDLDocument_temp = WSDLDocument_i;
			WSDLDocument_i = WSDLDocument_j;
			WSDLDocument_j = WSDLDocument_temp;
		}
		
		double contentWordSimilarity = searchEnginewordDB.correlation(WSDLDocument_i.getWords(true), WSDLDocument_j.getWords(true),false);
		double webServiceNameSimilarity =(double)this.webServiceNameSimilarity.get(WSDLDocument_i,WSDLDocument_j);
		double complexTypeMatch = (double)this.wsdlComplexTypesMatch.get(WSDLDocument_i,WSDLDocument_j);
		double messageMatch = (double)this.wsdlMessagesMatch.get(WSDLDocument_i,WSDLDocument_j);
		double portTypeMatch =(double)this.wsdlPortsMatch.get(WSDLDocument_i,WSDLDocument_j);
	
		// compute theta
		theta = 0.2*contentWordSimilarity + 0.2*webServiceNameSimilarity+0.2*complexTypeMatch+0.2*messageMatch+0.2*portTypeMatch;
		
		// write to similarity file
		try
		{
			similarityBufferedOutput.write(WSDLDocument_i.getNameWithoutExtension()+"\t"+WSDLDocument_j.getNameWithoutExtension()+"\t"+contentWordSimilarity+"\t"+webServiceNameSimilarity+"\t"+complexTypeMatch+"\t"+ 
					messageMatch +"\t"+portTypeMatch+"\t"+theta);
			similarityBufferedOutput.newLine();
			similarityBufferedOutput.flush();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return theta;
	}
	
	/*-----------------------------------------------------------------------
	 *					Misc
	 *-----------------------------------------------------------------------*/
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 *		WSDL Content													
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	
	/**
	 * Extract WSDL Content words reduced to their base words
	 * @param document WSDL Document to parse
	 */
	/*private void extractContentWords(WSDLDocument document)
	{
		document
	}*/
	
//	/**
//	 * Parses a wsdl document based on white spaces to produce a list of tokens
//	 *  
//	 * @param document WSDL document to parse
//	 */
//	private void parseWSDL(WSDLDocument document)
//	{
//		String line;								// line read from document
//		String[] lineTokens;						// tokens per line
//		
//		try
//		{
//			// open wsdl document for reading
//			FileInputStream fStream = new FileInputStream(document.getPath());
//			DataInputStream in = new DataInputStream(fStream);
//			BufferedReader br = new BufferedReader(new InputStreamReader(in));
//			
//			// for each line, get tokens separated by white spaces and add it to tokens vector
//			// also not include opening/ ending of a tag so to take only words
//			while((line = br.readLine())!=null)
//			{
//				lineTokens = line.split("<|</|>| ");
//				for(String lineToken:lineTokens)
//					if(!lineToken.isEmpty())
//						document.addToken(lineToken);	
//			}
//			
//			br.close();
//		} 
//		catch ( IOException ioex)
//		{
//			ioex.printStackTrace();
//		}
//	}
	
//	/**
//	 * Removes tokens from list which are not value of attribute names or not being part of text.
//	 * For remaining words, split values by capital letters
//	 * 
//	 * Briefly, extract words part of:
//	 * 1) Name attribute in each node
//	 * 2) Text node
//	 * 
//	 * @param document WSDL document to parse
//	 * @return Words extracted from token
//	 */
//	private void extractWords(WSDLDocument document)
//	{
//		//------------------------------ VARIABLES -------------------------------------------
//		Vector<String> tokens;
//		Vector<String> xmlTags;									// xml tags
//		XMLHandler xmlHandler; 									// handler to get xml tags
//		String curTag;											// current tag
//		
//		// token handlers
//		Iterator<String> tokenIter;								// token iterator
//		int i=0;
//		String curToken;										// current token in iteration
//		String[] tokenSplit;									// array of tokken split strings
//		String attributeName;									// Name of attribute
//		String attributeValue;									// Value of attribute
//		
//		//------------------------------ LOGIC -------------------------------------------
//		
//		// tokens
//		tokens = document.getTokens();	
//		
//		// Get XML Tags
//		xmlHandler = new XMLHandler(document.getPath());		// create xml handler for document 
//		xmlHandler.getAllXMLTagsAttributesNames();				// identify xml tags
//		xmlTags = xmlHandler.getTags();							// get xml tags
//		
//		// Remove xml tags from tokens
//		for(i=0;i<xmlTags.size();i++)
//		{
//			// opening tag
//			curTag = xmlTags.get(i);
//			while(tokens.contains(curTag))
//				tokens.remove(curTag);
//			
//			// closing tag
//			curTag = "/"+curTag;
//			while(tokens.contains(curTag))
//				tokens.remove(curTag);
//		}
//		
//		// Remove tokens which are not part of attribute name or text node
//		tokenIter =  tokens.iterator();			// token iterator
//		i=0;
//		while(tokenIter.hasNext())
//		{
//			curToken = tokenIter.next();
//			
//			if(curToken.contains("="))	// attribute ( syntax: name = value)
//			{
//				tokenSplit = curToken.split("=");	// split based on equal
//				attributeName = tokenSplit[0];		
//				attributeValue = tokenSplit[1];
//				
//				if(!attributeName.contentEquals("name"))	// only attributes of name we interest
//				{
//					tokenIter.remove();	// remove each different attribute
//					i--;
//				}
//				else
//					tokens.set(i, attributeValue); //attributeValue.split("(\"?\")")[1]);	// get only value of name attribute without quotes (start and end)
//			}
//			
//			i++;
//		}
//		
//		// Split words by capital letter and insert in words
//		tokenIter =  tokens.iterator();			// token iterator
//		while(tokenIter.hasNext())
//		{
//			curToken = tokenIter.next();
//			
//			if(curToken.startsWith("\""))	// if starts with " (double quote) then is attribute value so words must be split
//			{
//				attributeValue = curToken.split("(\"?\")")[1];
//				tokenSplit = attributeValue.split("(?=\\p{Upper})");	// extract double quotes split based on capital letters	
//				if(tokenSplit.length>1)	
//				{
//					for(int j=0,numTokens = tokenSplit.length;j<numTokens;j++)
//						if(!tokenSplit[j].isEmpty() && tokenSplit[j].length()!=1)
//							document.addWord(tokenSplit[j]);	
//				}
//				else
//					document.addWord(attributeValue);
//			}
//			else 
//				document.addWord(curToken);
//		}
//	}
	
//	/**
//	 * All tokens are reduced to their based words using a Porter stemmer
//	 * 
//	 * @param document WSDL document 
//	 */
//	private void wordsStemming(WSDLDocument document)
//	{
//		Vector<String> words = document.getWords();		// words of document
//		Stemmer stemmer = new Stemmer();				// object to reduce words to their base
//		Iterator<String> wordsIter = words.iterator();	// words iterator
//		String word;									// current word
//		
//		while(wordsIter.hasNext())
//		{
//			word = wordsIter.next();
//			stemmer.add(word.toCharArray(),word.length());
//			stemmer.stem();
//			document.addStemWord(stemmer.toString());
//		}
//	}
	
	/**
	 * Remove all function words from the  word vector
	 * 
	 * @param document WSDL document
	 */
	private void removeFunctionWords(WSDLDocument document)
	{
		Hashtable<String,Integer>  words= document.getWordsWithOccurrences();	// get stemmed words
		String[] functionWords = searchEnginewordDB.getFunctionWords();			// get function words
		
		// If document words vector contains any of function words, remove it from stemmed words
		for(String functionWord:functionWords)
			if(words.containsKey(functionWord))
				words.remove(functionWord);
		
		//Iterator<String> wordsIter = document.getWords(true).iterator();
		//Vector<String> contentWords= document.getWords(true);
		
		// Get function words from word database and if any of them is contained to
		// base words of document, remove it
		
		//System.out.println(contentWords.size());

		
		//remove enumeration values
		/*Vector<String> enumerationValues = new Vector<String>();
		WSDLDocumentHandler docHandler = new WSDLDocumentHandler(document);
		enumerationValues = docHandler.getEnumerationValues();
		for(String enumerationValue:enumerationValues)
			contentWords.remove(enumerationValue.toLowerCase());*/
		

		//System.out.println(contentWords.size());	
			

		//System.out.println(document.getName()+" "+ contentWords.keySet());
//		while(wordsIter.hasNext())
//		{
//			if(wordsIter)
//				wordsIter.remove();
//		}
		
	//	System.out.println(document.getName()+" "+ contentWords.keySet());
		
//		Hashtable<String,Double> n_w_;					// Estimated document frequency
//		Hashtable<String,Double> Lambda_w;				// Overestimator factor for each word
//		double Lambda_threhold = 1.0;							// Threshold lambda
//		WordDatabase wordDb= new WordDatabase(SimilarityType.NGD,SearchEngine.BING);
//		Hashtable<String,Integer> wordsOcc; // words from all documents as extracted from n_w list
//		Vector<String> words;
//		double Lambda;	// overestimation factor for word
//		
//		//numDocsContainWord();	// get estimated document frequency 
//		wordsOcc = document.getBaseWordsWithOccurrences();	// base words with occurrences
//		words= document.getBaseWords();	// content base words without occurrences
//		double estimatedDocFreq;
//		double averageLambda=0.0;		// average lamdba
//		Double[] allLambdas;			// only overestimation factor values
//		
//		Lambda_w = new Hashtable<String,Double>();
//		
//		// Overestimation factor for each word
//		for(String word:words)
//		{
//			estimatedDocFreq = wordDb.estimatedDocFreq(word);
//			System.out.println(word+" \t"+estimatedDocFreq);
//			Lambda =  estimatedDocFreq / wordsOcc.get(word);
//			Lambda_w.put(word, Lambda);
//		}
//		
//		// Average Lambda
//		allLambdas = Lambda_w.values().toArray(new Double[Lambda_w.size()]);
//		for(double lambda:allLambdas)
//			averageLambda +=lambda;
//		averageLambda /= allLambdas.length;
//		
//		// ï¿½ threshold
//		if(averageLambda>1)
//			Lambda_threhold = averageLambda;
//		
//		Hashtable<String,Integer>  contentWords= document.getBaseWordsWithOccurrences();		// get map of base words
//		Iterator<Entry<String,Integer>> wordsIter = contentWords.entrySet().iterator();
//				
//		while(wordsIter.hasNext())
//			if(Lambda_w.get(wordsIter.next().getKey())<=Lambda_threhold)
//				wordsIter.remove();

		//System.out.println(contentWords);
	}
	
	/**
	 * Remove words that do not describe the specific semantics of the web service such
	 * as 'data','web','port'
	 * 
	 * @param words Content in meaningful words
	 * @return Vector of content words
	 */
	private void recogniseContentWords(WSDLDocument document)
	{
		Hashtable<String,Integer> contentWords = document.getWordsWithOccurrences(); // 	// get stemmed words
		
		// If document words vector contains any of general computing words, remove it from stemmed words
		for(String generalComputingWord:WSDLDocumentHandler.generalComputingWords)
			if(contentWords.containsKey(generalComputingWord))
				contentWords.remove(generalComputingWord);
		
		
//		int numClusters = 2;
//		Vector<String> cluster1 = new Vector<String>();
//		Vector<String> cluster2 = new Vector<String>();
//		Vector<String> previousCluster = new Vector<String>();
//		double[] mean = new double[numClusters];
//		double[] distance = new double[2]; 
//		double wordDistance;
//		boolean start=true;
//		boolean convergence=true;
//		
//		do
//		{
//			convergence = true;
//
//			if(start)
//			{
//				mean[0] = wordDB.correlation(new String[]{contentWords.get(0)},generalComputingContentWords,true);
//				mean[1] =  wordDB.correlation(new String[]{contentWords.get(1)},generalComputingContentWords,true);
//			}
//			else
//			{
//				mean[0] = wordDB.correlation(cluster1.toArray(new String[cluster1.size()]),generalComputingContentWords,true);
//				mean[1] = wordDB.correlation(cluster2.toArray(new String[cluster2.size()]),generalComputingContentWords,true);
//			}
//			
//			cluster1 = new Vector<String>();
//			cluster2 = new Vector<String>();
//
//			for(String contentWord:contentWords)
//			{
//				
//				
//				wordDistance =  wordDB.correlation(new String[]{contentWord},generalComputingContentWords,true);
//				distance[0] = Math.abs(distance[0]- mean[0]);
//				distance[1] = Math.abs(distance[1]- mean[1]);
//				/*if(cluster1.size()>0)
//				{
//					distance[0] = wordDB.correlation(new String[]{contentWord},cluster1.toArray(new String[cluster1.size()]));
//					distance[0] = Math.abs(distance[0]- mean[0]);
//				}
//				else
//					distance[0] = wordDB.correlation(new String[]{contentWord}, generalComputingContentWords);
//				if(cluster2.size()>0)
//				{
//					distance[1] = wordDB.correlation(new String[]{contentWord}, cluster2.toArray(new String[cluster2.size()]));
//					distance[1] = Math.abs(distance[1]- mean[1]);
//				}
//				else
//					distance[1] = wordDB.correlation(new String[]{contentWord}, generalComputingContentWords);*/
//				
//				if(distance[0]<distance[1])
//					cluster1.add(contentWord);
//				else
//					cluster2.add(contentWord);
//				
//			}
//			
//			if(!start)
//			{
//				if(cluster1.equals(previousCluster))
//					break;	
//			}
//			start=false;
//			previousCluster = cluster1;
//		}while(convergence);
//			
//		System.out.println(cluster2);
//		return contentWords;
	}
	
//	/**
//	 * Remove from words of document the general computing words
//	 * @param words
//	 * @return
//	 */
//	public Vector<String> recogniseContentWords(String[] words)
//	{
//		Vector<String> contentWords = new Vector<String>(Arrays.asList(words)); //document.getStemWords();
//		int numClusters = 2;
//		//String[][] clusters;// = new String[][]{new String[contentWords.size()],new String[contentWords.size()]};
//		Vector<String> cluster1 = new Vector<String>();
//		Vector<String> cluster2 = new Vector<String>();
//		Vector<String> previousCluster1 = new Vector<String>();
//		Vector<String> previousCluster2 = new Vector<String>();
//		double[] mean = new double[numClusters];
//		double[] distance = new double[2]; 
//		//double wordDistance1,word2;
//		boolean start=true;
//		boolean convergence=true;
//		
//		do
//		{
//			convergence = true;
//
//			/*if(start)
//			{
//				mean[0] = wordDB.correlation(new String[]{contentWords.get(0)}, generalComputingContentWords,true);
//				mean[1] = wordDB.correlation(new String[]{contentWords.get(2)}, generalComputingContentWords,true);
//			}
//			else
//			{
//				mean[0] = wordDB.correlation(cluster1.toArray(new String[cluster1.size()]),generalComputingContentWords,true);
//				mean[1] = wordDB.correlation(cluster2.toArray(new String[cluster2.size()]),generalComputingContentWords,true);
//			}*/
//			
//			/*cluster1.add(contentWords.get(0));
//			cluster2.add(contentWords.get(1));*/
//			cluster1.add(contentWords.get(0));
//			cluster2.add(contentWords.get(1));
//			
//			if(!start)
//			{
//				cluster1.clear();
//				cluster2.clear();
//			}
//			
//			for(String contentWord:contentWords)
//			{
//				//wordDistance = wordDB.correlation(new String[]{contentWord},generalComputingContentWords,true);// cluster1.toArray(new String[cluster1.size()]));
//				//distance[0] = Math.abs(wordDistance- mean[0]);
//
//					//distance[1] = wordDB.similarity(new String[]{contentWord},generalComputingContentWords);// cluster2.toArray(new String[cluster2.size()]));
//				//distance[1] = Math.abs(wordDistance- mean[1]);
//				//	
//				//if(distance[0]<distance[1])
//				//	cluster1.add(contentWord);
//				//else
//				//	cluster2.add(contentWord);
//				
//				if(cluster1.contains(contentWord) || cluster2.contains(contentWord))
//					continue;
//				
//				if(start)
//				{
//					distance[0] = searchEnginewordDB.correlation(new String[]{contentWord},new String[]{contentWords.get(0)},true);// cluster1.toArray(new String[cluster1.size()]));
//					distance[1] = searchEnginewordDB.correlation(new String[]{contentWord},new String[]{contentWords.get(1)},true);// cluster1.toArray(new String[cluster1.size()]));
//				}
//				else
//				{
//					distance[0] = searchEnginewordDB.correlation(new String[]{contentWord},previousCluster1.toArray(new String[previousCluster1.size()]),true);// cluster1.toArray(new String[cluster1.size()]));
//					distance[1] = searchEnginewordDB.correlation(new String[]{contentWord},previousCluster2.toArray(new String[previousCluster2.size()]),true);// cluster1.toArray(new String[cluster1.size()]));
//				}
//				
//				if(distance[0]<distance[1])
//					cluster1.add(contentWord);
//				else
//					cluster2.add(contentWord);
//			}
//			
//			if(!start)
//			{
//				if(cluster1.equals(previousCluster1))
//					break;
//						
//			}
//			
//			start=false;
//			previousCluster1 = (Vector<String>) cluster1.clone();
//			previousCluster2 = (Vector<String>) cluster2.clone();
//			
//		}while(convergence);
//		
//		System.out.println(cluster1);
//		System.out.println(cluster2);
//		return contentWords;
//	}	
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 *		Global document methods													
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	
	/**
	 * For each word w extracted from documents, find the number of documents contain word w
	 */
	public void numDocsContainWord()
	{
		Vector<String> words;						// Words vector of document
		Iterator<String> iter;						// Words iterator0
		String word;								// Current word
		
		//Hashtable<String,Integer> wordsOccurrences;	// Words and their number of occurrences
		//Iterator<Map.Entry<String, Integer>> iter;	// Iterator of words
		//Map.Entry<String,Integer> entry;			// Currenty entry in words

		//int numOccurrences;							// Value of entry --> number of occurrences
		//String[] exclude = new String[]{"/","*","-","+"};
		
		// For each document, get its map with content words and corresponding number of occurences
		// If word does not exists in n_w map, add new entry. Otherwise, add current document statistics to this word entry
		for(WSDLDocument document:WSDLDocumentsC)
		{
			words = document.getWords(true);
			iter = words.iterator();
			
			while(iter.hasNext())
			{
				word = iter.next();
				
				if(n_w.keySet().contains(word))
					n_w.put(word, n_w.get(word)+1);
				else
					n_w.put(word, 1);
						
				
				//entry = iter.next();
				//word = entry.getKey();
				//numOccurrences = entry.getValue();

				/*if(n_w.keySet().contains(word))
					n_w.put(word, n_w.get(word)+numOccurrences);
				else
					n_w.put(word,numOccurrences);*/

				
			}
		}
	}
	
//	/**
//	 * Find overestimation factor for each word and in the end the overestimation factor threshold.
//	 */
//	public void overestimationFactor()
//	{
//		WordDatabase wordDb= new WordDatabase(SimilarityType.NGD,SearchEngine.BING);
//		String[] words; // words from all documents as extracted from n_w list
//		double Lambda;	// overestimation factor for word
//		
//		numDocsContainWord();	// get estimated document frequency 
//		words = n_w.keySet().toArray(new String[n_w.size()]);		// words from estimated document frequency list
//		double averageLambda=0.0;			// average lamdba
//		Double[] allLambdas;			// only overestimation factor values
//		
//		Lambda_w = new Hashtable<String,Double>();
//		
//		// Overestimation factor for each word
//		for(String word:words)
//		{
//			Lambda = wordDb.estimatedDocFreq(word) / ((double)n_w.get(word)/this.WSDLDocumentsC.size());
//			this.Lambda_w.put(word, Lambda);
//		}
//		
//		// Average Lambda
//		allLambdas = Lambda_w.values().toArray(new Double[Lambda_w.size()]);
//		for(double lambda:allLambdas)
//			averageLambda +=lambda;
//		averageLambda /= allLambdas.length;
//		
//		// ï¿½ threshold
//		if(averageLambda>1)
//			this.Lambda_threhold = averageLambda;
//	}

}
