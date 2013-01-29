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

import javax.persistence.*;
import java.util.*;

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
	private HashMap<WSDLDocument,WSDLDocumentHandler> WSDLDocumentHandlers;	// Handlers for all documents
	private Hashtable<String,Integer> n_w;					// Number of documents contain word w ( Key: Keyword, Value: Number)
	
	List<WebServicesCluster> clusters;						// Clusters of wsdl documents with similar functionality
	
	// for each pair of web services
	private MultiKeyMap wsdlComplexTypesMatch;					// Type matches 
	private MultiKeyMap wsdlMessagesMatch;						// Message matches					
	private MultiKeyMap wsdlPortsMatch;						// Ports matches
	private MultiKeyMap webServiceNameSimilarity;				// Name matches
	private MultiKeyMap similarityFactor;						// Similarity factor �
	
	private WordDatabase searchEnginewordDB;	// Word database instance where similarity is based on search engine results
	private WordDatabase wordNetWordDB;			// Word database instance where similarity is based on wordnet
	
	public  double similarityFactorThreshold = 0.7;		// Similarity Factor theta treshold above which web services match
	
	// Filenames
	private static String clusterFilename="clusters.dat";	// Clusters and their collection of documents
	private static String similarityResFilename= "similarity_results.dat";	// Matches and similarity value
	
	// Output file for similarity values for each pair of web services
	FileWriter similarityFStream;
	BufferedWriter similarityBufferedOutput;
	
	// Object database
	public static EntityManagerFactory emf = Persistence.createEntityManagerFactory("database/objectdb/web_services_clusterer.odb");
	public static EntityManager em =emf.createEntityManager();
	
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
		WSDLDocument wsdlDocument;
		
		// WSDL Documents collection
		this.WSDLDocuments=WSDLDocuments;
		this.WSDLDocumentsC = new Vector<WSDLDocument>(WSDLDocuments.size());
		this.similarityFactorThreshold= similarityFactorThreshold;
		this.WSDLDocumentHandlers = new HashMap<WSDLDocument,WSDLDocumentHandler>();
		
		// For each wsdl document create a WSDLDocument object reflected to this web service description
		for(String WSDLDocument:WSDLDocuments)
		{
			wsdlDocument = new WSDLDocument(WSDLDocument);
			this.WSDLDocumentsC.add(wsdlDocument);
			this.WSDLDocumentHandlers.put(wsdlDocument, new WSDLDocumentHandler(wsdlDocument));
		}
		
		this.n_w = new Hashtable<String,Integer>();
		this.searchEnginewordDB = new WordDatabase(SimilarityType.NGD);
		this.wordNetWordDB = new WordDatabase(SimilarityType.WordNet);
		
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
		
		// Object db

		Query query;
		
		em.getTransaction().begin();
		query = em.createQuery(
			      "DELETE FROM QualityFactor");
		query.executeUpdate();
		query = em.createQuery("DELETE FROM WebServicesCluster");
		query.executeUpdate();
		em.getTransaction().commit();
		
	}
	
	protected void finalize() throws Throwable
	{
		similarityBufferedOutput.close();
		em.close();
		emf.close();
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
		System.out.print("Writing clusters to database...");
	
		 // write quality factors among pairs of web services in database
		em.getTransaction().begin();
		for(int i=0;i<clusters.size();i++)
			em.persist(clusters.get(i));
		em.getTransaction().commit();
		
//		try
//		{
//			FileWriter fileWriter= new FileWriter(clusterFilename);
//			BufferedWriter out = new BufferedWriter(fileWriter);
//			
//			for(int i=0;i<clusters.size();i++)
//			{
//				out.write("Cluster "+(i+1)+" : ");
//				
//				for(int j=0;j<clusters.get(i).getDocuments().size();j++)
//					out.write(clusters.get(i).getDocuments().get(j).getNameWithoutExtension()+"\t");
//				out.newLine();
//			}
//			
//			out.close();
//		}
//		catch (IOException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
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
			documentHandler = this.WSDLDocumentHandlers.get(document);
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
		
		//MultiKeyMap complexTypeMatchesFlags;	// contains the status matches on messages for each pair of documents 
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
				
				//complexTypeMatchesFlags = new MultiKeyMap();
				messageMatchesFlags = new MultiKeyMap();
//				
//				// WSDL Types
				System.out.print("\t\t\tWSDL ComplexTypes...");
				match = matchWSDLTypes(wsdlDoc1,wsdlDoc2);
				wsdlComplexTypesMatch.put(wsdlDoc1, wsdlDoc2,match);
				System.out.println(match);
				
//				// WSDL Messages
				System.out.print("\t\t\tWSDL Messages...");
				match = matchWSDLMessages(wsdlDoc1,wsdlDoc2,messageMatchesFlags);
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
	private double matchWSDLTypes(WSDLDocument WSDLDocument_i,WSDLDocument WSDLDocument_j)//,MultiKeyMap complexTypeMatchesFlags)
	{
		int M=0;			// number of matched types
		double match = 0.0;	// match between the two document in complex element types

		// get handlers for each one of two documents
		WSDLDocumentHandler docHandler1 =WSDLDocumentHandlers.get(WSDLDocument_i);
		WSDLDocumentHandler docHandler2 = WSDLDocumentHandlers.get(WSDLDocument_j);
		
		HashMap<String,Integer> doc1PrimitiveTypes = docHandler1.getPrimitiveTypes();	// get primitive types in document 1 complex types
		HashMap<String,Integer> doc2PrimitiveTypes = docHandler2.getPrimitiveTypes();	// get primitive types in document 2 complex types
		
		// iterate through doc 1 primitive types
		Iterator<Entry<String,Integer>> doc1PrimitiveTypeIter = doc1PrimitiveTypes.entrySet().iterator();
		String curPrimitiveType;
		int numOccurencesInDoc1;		// number of primitive type occurences in document 1
		int numOccurencesInDoc2;		// number of primitive type occurences in document 2
		int total=0;					// total number of primitive types in two documents
		
		// for each primitive type appearing in document 1
		while(doc1PrimitiveTypeIter.hasNext())
		{
			curPrimitiveType = doc1PrimitiveTypeIter.next().getKey();	// get current primitive type from document 1
			numOccurencesInDoc1 = doc1PrimitiveTypes.get(curPrimitiveType);	// number of occurences  in document 1
			
			// if document 2 contains primitive type, get its num of occurrences
			if(doc2PrimitiveTypes.containsKey(curPrimitiveType))
				numOccurencesInDoc2 = doc2PrimitiveTypes.get(curPrimitiveType);
			else
				numOccurencesInDoc2 = 0;
			
			// In number of matched types , added the minimum value between the two number of occurrences as intersection
			M += Math.min(numOccurencesInDoc1, numOccurencesInDoc2);
		}
		
		// Number of total appearances of all primitive types in both documents
		List<Integer> numPrimitiveTypesInDoc1 = new ArrayList<Integer>(doc1PrimitiveTypes.values());
		List<Integer> numPrimitiveTypesInDoc2 = new ArrayList<Integer>(doc2PrimitiveTypes.values()); 
		// get total of appearances of all primitive types
		for(int num1:numPrimitiveTypesInDoc1)
			total+=num1;
		for(int num2:numPrimitiveTypesInDoc2)
			total+=num2;
		
		// Find match between primitive types in both documents
		match = (float) 2*M/total; 
		
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
	private double matchWSDLMessages(WSDLDocument WSDLDocument_i,WSDLDocument WSDLDocument_j//,MultiKeyMap complexTypeMatchesFlags
			,MultiKeyMap messageMatchesFlags)
	{
		int M=0;			// number of matched types
		double match = 0.0;	// match between the two document in complex element types
		int total=0;			// Total number of defined messages in two docuemnts
		int i,j=0;

		// get handlers for each one of two documents
		WSDLDocumentHandler docHandler1 =WSDLDocumentHandlers.get(WSDLDocument_i);
		WSDLDocumentHandler docHandler2 = WSDLDocumentHandlers.get(WSDLDocument_j);
		
		// get messages from two documents where their complex elements have been replaced by simple types
		HashMap<String,Vector<String>> doc1Messages = docHandler1.getMessagesInSimpleTypes();	
		HashMap<String,Vector<String>> doc2Messages = new HashMap<String,Vector<String>>( docHandler2.getMessagesInSimpleTypes());
		Iterator<Entry<String,Vector<String>>> doc1MessagesIter = doc1Messages.entrySet().iterator();
		Iterator<Entry<String,Vector<String>>> doc2MessagesIter = doc2Messages.entrySet().iterator();
		Vector<String> doc1MessagePrimitiveTypes;
		Vector<String> doc2MessagePrimitiveTypes;
		int numDoc1Messages = doc1Messages.keySet().size();
		int numDoc2Messages = doc2Messages.keySet().size();
		
		while(doc1MessagesIter.hasNext())
		{
			doc1MessagePrimitiveTypes = doc1MessagesIter.next().getValue();	// get primitive types from document 1 current message
			 doc2MessagesIter = doc2Messages.entrySet().iterator();
			while(doc2MessagesIter.hasNext())
			{
				doc2MessagePrimitiveTypes = doc2MessagesIter.next().getValue();	// get primitive types from document 2 current message
				
				// check if two vectors of primitive types match
				if(isMessageMatch(doc1MessagePrimitiveTypes,doc2MessagePrimitiveTypes))
				{
					M++;	// two vectors are exactly the same in num of elements and order
					doc2MessagesIter.remove();	// remove current message from a next comparison
					break;
				}
			}
		}
		
		// get number of messages in two documents
		total = numDoc1Messages + numDoc2Messages;
		
		// Find match between messages in both documents
		match = (float) 2*M/total; 
		
		// now find for all pairs of messages in two documents if match or not
		String message1;	// current message from first document
		String message2;	// current message from second document
		doc2Messages = new HashMap<String,Vector<String>>( docHandler2.getMessagesInSimpleTypes());
		doc1MessagesIter = doc1Messages.entrySet().iterator();
		while(doc1MessagesIter.hasNext())
		{
			message1 = doc1MessagesIter.next().getKey();	// name of current message
			doc1MessagePrimitiveTypes = doc1Messages.get(message1);	// get primitive types from document 1 current message
			doc2MessagesIter = doc2Messages.entrySet().iterator();
			while(doc2MessagesIter.hasNext())
			{
				message2 =  doc2MessagesIter.next().getKey();	// name of current message
				doc2MessagePrimitiveTypes = doc2Messages.get(message2);		// get primitive types from document 2 current message
				
				// check if two vectors of primitive types match
				if(isMessageMatch(doc1MessagePrimitiveTypes,doc2MessagePrimitiveTypes))
					messageMatchesFlags.put(message1,message2,true);
				else
					messageMatchesFlags.put(message1,message2,false);
			}
		}
		
		return match;
	}
	
	/**
	 * Check if two messages has same length and contains primitive types in same order
	 * @param message1 Message 1 
	 * @param message2 Message 2
	 * @return True if match
	 */
	private boolean isMessageMatch(Vector<String> message1,Vector<String> message2)
	{
		if(message1.size() != message2.size())
			return false;
		
		for(int i=0;i<message1.size();i++)
			if(!message1.get(i).contains(message2.get(i)))
				return false;
		
		return true;
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
		double match = 0.0;	// match between two documents
		double total;	// total of define messages between two documents
		
		int numDoc1PortTypes;	// num of ports for first web service
		int numDoc2PortTypes;	// num of ports for second web service
		
		// get handlers for each one of two documents
		WSDLDocumentHandler docHandler1 =WSDLDocumentHandlers.get(WSDLDocument_i);
		WSDLDocumentHandler docHandler2 = WSDLDocumentHandlers.get(WSDLDocument_j);
		
		// get each complex type as array of single elements
		HashMap<String,Vector<String>> doc1PortSequences = docHandler1.getPortSequences();
		HashMap<String,Vector<String>> doc2PortSequences = new HashMap<String,Vector<String>>(docHandler2.getPortSequences());
		HashMap<String,Vector<String>> doc1PortStructures =  docHandler1.getPortMessagesStructure();
		HashMap<String,Vector<String>> doc2PortStructures = docHandler2.getPortMessagesStructure();
		numDoc1PortTypes = doc1PortSequences.size();
		numDoc2PortTypes = doc1PortSequences.size();
		String doc1PortTypeName;
		String doc2PortTypeName;
		Vector<String> doc1Structure, doc1Sequence;	
		Vector<String> doc2Structure,doc2Sequence;
	
		// For each pair of complex types between the two web services , find if there is a match 
		// 1) In number of single elements
		// 2) If 1 is true, then check if every single element is the same type
		Iterator<String> doc1PortTypesIter = doc1PortSequences.keySet().iterator();
		Iterator<String> doc2PortTypesIter;
		while(doc1PortTypesIter.hasNext()) // doc 1 port 
		{
			doc1PortTypeName = doc1PortTypesIter.next();				// port name
			doc1Sequence = doc1PortSequences.get(doc1PortTypeName);		// message sequences
			doc1Structure = doc1PortStructures.get(doc1PortTypeName);	// message names
			doc2PortTypesIter = doc2PortSequences.keySet().iterator();	// iterator for sequences
			
			while(doc2PortTypesIter.hasNext())
			{
				doc2PortTypeName = doc2PortTypesIter.next();
				doc2Sequence = doc2PortSequences.get(doc2PortTypeName);
				doc2Structure = doc2PortStructures.get(doc2PortTypeName);
				
				//if(doc1Sequence==doc2Sequence)	//check if sequences matches ( input and output keywords)
				//{
					// check if messages in same order has the same primitive types
					if(isPortStructureMatch(doc1Structure,doc2Structure) && isPortSequenceMatch(doc1Sequence,doc2Sequence,messageMatchesFlags))
					{
						M++;
						doc2PortTypesIter.remove();
						break;
					}
				//}
			}

		}
		
		// total number of defined port types
		total = numDoc1PortTypes +numDoc2PortTypes;
		
		// get match
		match = 2*M/total;
		
		return match;
	}
	
	private boolean isPortStructureMatch(Vector<String> port1Structure,Vector<String> port2Structure)
	{
		if(port1Structure.size()!=port2Structure.size())
			return false;
		
		for(int i=0;i<port1Structure.size();i++)
			if(!port1Structure.get(i).contains(port2Structure.get(i)))
				return false;
		
		return true;
	}
	
	/**
	 * Check if messages of port in the same order have the same structure
	 * @param port1Structure	port 1 messages
	 * @param port2Structure	port 2 messages
	 * @param messageMatchesFlags matches between messages
	 * @return True if two structures match 
	 */
	private boolean isPortSequenceMatch(Vector<String> port1Sequence,Vector<String> port2Sequence,MultiKeyMap messageMatchesFlags )
	{
		int numMessages1 = port1Sequence.size();
		int numMessages2 = port2Sequence.size();
		
		if(numMessages1!=numMessages2)
			return false;
		
		for(int i=0;i<numMessages1;i++)
			if((boolean)messageMatchesFlags.get(port1Sequence.get(i),port2Sequence.get(i))==false)
				return false;
		
		return true;
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
		WSDLDocumentHandler docHandler1 =WSDLDocumentHandlers.get(WSDLDocument_i);
		WSDLDocumentHandler docHandler2 = WSDLDocumentHandlers.get(WSDLDocument_j);
		
		similarity = searchEnginewordDB.correlation(docHandler1.getWebServiceNameSplit(),
				docHandler2.getWebServiceNameSplit(),false);
		
		return similarity;
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
		
		 // write quality factors among pairs of web services in database
		em.getTransaction().begin();
		
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
				System.out.println("......Min Θ: "+minTheta);
				if(minTheta>=similarityFactorThreshold)
				{
					cluster.add(wSDLDocument);
					clusteredDocs.add(wSDLDocument);
					
					System.out.println("......Added to cluster "+cluster.getIndex()+"["+cluster.getDocumentNames()+"]");
				}
			}
		}
		
		em.getTransaction().commit();
		
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
	 * Similarity factor �(s_i,s_j) between a pair of web services based
	 * on five similarity features 
	 * 
	 * @param WSDLDocument_i Web service i
	 * @param WSDLDocument_j Web service j
	 * @return Similarity factor 
	 */
	private double similarityFactor(WSDLDocument WSDLDocument_i,WSDLDocument WSDLDocument_j)
	{
		double theta = 0.0;	// similarity factor
		QualityFactor qualityFactor;
		
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
		

		qualityFactor = new QualityFactor(WSDLDocument_i.getNameWithoutExtension(),WSDLDocument_j.getNameWithoutExtension(),
				contentWordSimilarity,webServiceNameSimilarity,complexTypeMatch,messageMatch,portTypeMatch,theta);
		em.persist(qualityFactor);
		//em.getTransaction().commit();
		
		// write to similarity file
//		try
//		{
//			similarityBufferedOutput.write(WSDLDocument_i.getNameWithoutExtension()+"\t"+WSDLDocument_j.getNameWithoutExtension()+"\t"+contentWordSimilarity+"\t"+webServiceNameSimilarity+"\t"+complexTypeMatch+"\t"+ 
//					messageMatch +"\t"+portTypeMatch+"\t"+theta);
//			similarityBufferedOutput.newLine();
//			similarityBufferedOutput.flush();
//		}
//		catch (IOException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		
		return theta;
	}
	
	/*-----------------------------------------------------------------------
	 *					Misc
	 *-----------------------------------------------------------------------*/
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 *		WSDL Content													
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	
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
	}
	
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
			}
		}
	}
}
