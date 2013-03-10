import java.io.*;
import java.util.*;

import com.google.common.collect.*;

/**
 * Creates vector space model from a collection of wsdl documents 
 */
public class VectorSpaceModel
{
	/*=========================================================================
	 *					Fields
	 *=========================================================================*/
	
	private Vector<WSDLDocument> wsdlDocuments; 					// Collection of web services descriptions 
	private Set<String> terms;										// Terms-dimensions of vector space model
	private int N;													// Total number of documents
	private ArrayTable<String,WSDLDocument,Double> w_ij;			// Weights for each
	private HashMap<WSDLDocument,Double> documentVectorLengths;		// Length of each document vector 
	private ArrayTable<String,WSDLDocument,Double> f_ij;	// Frequency of term i in document j	(Key: Term-Document Value: Frequency)
	private HashMap<String,Double> idf_i;					// Inverse document frequency of term i ( Key: term - Value: Idf)
	
	/*=========================================================================
	 *					Constructors
	 *=========================================================================*/
	
	/**
	 * @param wSDLDocumentsC WSDL Documents
	 */
	public VectorSpaceModel(Vector<WSDLDocument> wsdlDocuments)
	{
		this.wsdlDocuments= wsdlDocuments;
		this.N = wsdlDocuments.size();
	}
	
	/*=========================================================================
	 *					Getters
	 *=========================================================================*/
	
	/*=========================================================================
	 *					Setters
	 *=========================================================================*/
	
	/*=========================================================================
	 *					Methods
	 *=========================================================================*/
	
	/**
	 * Compute weights for each document in each term-dimension 
	 */
	public void computeWeights()
	{
		Hashtable<String,Integer> freq_ij;		// frequency of each term in current document ( Key:term - Value:Frequency)
		int maxFreq_ij;							// maximum term frequency in document
		double f_i;								// normalized term frequency in current document
		double idf;								// idf of a term
		double w;								// weight
		double documentVectorLength=0.0;		// length of document vector
		int i=0;
		
		HashMap<String,Integer> n_i;					// Number of documents that contain term i ( Key:term - Value: Num of documents)
		
		this.terms = new HashSet<String>();				// Initialize terms set
		n_i = new HashMap<String,Integer>();			//
		idf_i = new HashMap<String,Double>();
		
		//FileWriter fWriter=null;
		//BufferedWriter out=null;
		
		try
		{
//			fWriter = new FileWriter("debug.txt");
//			 out = new BufferedWriter(fWriter);
			
			/* Create term vector for each document. Extract content from each wsdl file
			 and add its base words into terms*/
			for(WSDLDocument wsdlDocument:wsdlDocuments)
			{
				//wsdlDocument.extractContent(false,true,false,true,true);
				terms.addAll(wsdlDocument.getWords(true));
			}
			
			System.out.println("Terms: "+terms+" "+terms.size());
			
//			out.write("Terms");
//			out.newLine();
//			out.write(terms.toString());
//			out.newLine();
//			
//			out.write("Frequencies");
//			out.write("----------------------\n");
			
			// Initialize frequencies and weight 2-D tables where first dimension is term and second is document
			f_ij = ArrayTable.create(terms, wsdlDocuments);
			this.w_ij = ArrayTable.create(terms,wsdlDocuments);
			
			// Compute term frequencies for each document
			System.out.println("Term Frequencies\n");
			for(WSDLDocument wsdlDocument:wsdlDocuments)
			{
				System.out.println("Freq Document "+wsdlDocument.getNameWithoutExtension()+" ...");
				
				freq_ij = wsdlDocument.getTermsFrequencies(true);	// get frequencies for each term in current document
				maxFreq_ij = Collections.max(freq_ij.values()); 	// get max term frequency in document	
			
//				out.write("\n---------------------------------------------------------------------------------------------------\n");
//				out.write(wsdlDocument.getNameWithoutExtension());
//				out.newLine();
//				out.write("\t"+freq_ij.toString());
//				out.newLine();
//				out.write("\t "+maxFreq_ij+"\n");
//				out.newLine();
//				out.flush();
//				
				//System.out.println("\t"+(i++)+"."+term+" ");
				
				/* For each term, if document contains term, compute normalized frequency else
				 normalized frequency is 0*/
				i=0;
				for(String term:terms)
				{
					if(freq_ij.keySet().contains(term))
					{
						f_i = (double) freq_ij.get(term)/maxFreq_ij;
						f_ij.put(term, wsdlDocument,f_i);
						
						// update the number of documents which contain term
						if(n_i.keySet().contains(term))
							n_i.put(term, n_i.get(term)+1);
						else
							n_i.put(term, 1);
					}
					else
						f_ij.put(term, wsdlDocument,0.0);
				}
				
				// update document frequencies
				for(String term:terms)
					if(freq_ij.keySet().contains(term))
						wsdlDocument.addNormalizedTermFrequency(term, f_ij.get(term, wsdlDocument));
				
//				out.write("\t "+f_ij.toString()+"\n");
//				out.flush();
						
			}
			
//			out.newLine();
//			out.write(n_i.toString()+"\n");
//			
//			out.newLine();
			
			// Inverse document frequency 
			System.out.println("\t IDF ...");
			i=0;
			for(String term:terms)
			{
				// Compute inverse document frequency for each word
				idf = Math.log10((double)N/n_i.get(term));
				idf_i.put(term,idf );
				System.out.println("IDF "+(i++)+" "+term);
			}
////			
////			out.write("Idfs");
////			out.newLine();
////			out.write("-------------------");
////			out.newLine();
////			out.write(idf_i.toString()+"\n");
////			
////			out.newLine();
//			
			// Weights
			documentVectorLengths = new HashMap<WSDLDocument,Double>(wsdlDocuments.size());	// length of each document vector
			System.out.println("\t Weights ...");
			i=0;
			for(WSDLDocument wsdlDocument:wsdlDocuments)
			{
				documentVectorLength=0.0;
				
				System.out.println("\t Weight ..."+wsdlDocument.getNameWithoutExtension());
				for(String term:terms)
				{
					// weight
					f_i = f_ij.get(term,wsdlDocument);
					w = f_i*idf_i.get(term);
					this.w_ij.put(term,wsdlDocument,w);
					wsdlDocument.updateWeights(term, w);
					
					// include term weight in vector length computation 
					documentVectorLength += Math.pow(w,2);
				}
				
				documentVectorLength =  Math.sqrt(documentVectorLength);
				documentVectorLengths.put(wsdlDocument, documentVectorLength);
			}
			
//			out.write("weights");
//			out.newLine();
//			out.write("-------------------");
//			out.write(w_ij.toString());
//			out.newLine();
//			
//			for(WSDLDocument wsdlDocument:WSDLDocuments)
//				for(WSDLDocument wsdlDocumentt:WSDLDocuments)
//					System.out.println(wsdlDocument.getNameWithoutExtension()+" - "+wsdlDocumentt.getNameWithoutExtension()+" "+sim(wsdlDocument,wsdlDocumentt));
//				
//			out.close();
//			
		}
		catch(OutOfMemoryError | Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * Similarity between two documents
	 * @param doc1 First document to compare 
	 * @param doc2 Second document to compare
	 * @return Similarity ( 0<=sim<=1)
	 */
	public double sim(WSDLDocument document_i,WSDLDocument document_j)
	{
		double _sim=0.0;	// the similarity between the two documents
		double dot=0.0;			// dot product between document vectors
		
		// dot product of two document vectors
		for(String term:terms)
			dot += this.w_ij.get(term,document_i)*this.w_ij.get(term,document_j);
			
		// similarity
		_sim = dot/(documentVectorLengths.get(document_i)*documentVectorLengths.get(document_j));
		
		return _sim;
	}
	
	/**
	 * Print vector space model in files and store documents in database
	 * Output
	 * 
	 */
	public void printToFileAndSaveToDb()
	{
		String frequenciesFile = "output/vsm_freq.txt";	// term frequencies in each document
		String idfFile = "output/vsm_idf.txt";				// term idf
		String weightFile= "output/vsm_weight.txt";		// document term vector with weights
		String documentName;						// document name
			
		// First, stroe all documents in objectdb
		ObjectDBConn.em.getTransaction().begin();
		ObjectDBConn.em.createQuery("DELETE FROM WSDLDocument WHERE repository='"+wsdlDocuments.get(0).getRepository()+"'").executeUpdate();
		ObjectDBConn.em.getTransaction().commit();
		
		ObjectDBConn.em.getTransaction().begin();
		for(WSDLDocument wsdlDocument:wsdlDocuments)
			ObjectDBConn.em.persist(wsdlDocument);
		ObjectDBConn.em.getTransaction().commit();
		ObjectDBConn.em.clear();
		
		try
		{
			FileWriter freqWriter = new FileWriter(frequenciesFile);
			BufferedWriter freqOut = new BufferedWriter(freqWriter);
			FileWriter weightWriter= new FileWriter(weightFile);
			BufferedWriter weightOut = new BufferedWriter(weightWriter);
			FileWriter idfWriter = new FileWriter(idfFile);
			BufferedWriter idfOut = new BufferedWriter(idfWriter);
			
			// First line are documents
			freqOut.write("\t");
			weightOut.write("\t");
			for(WSDLDocument wsdlDocument:wsdlDocuments)
			{
				documentName = wsdlDocument.getNameWithoutExtension();
				freqOut.write(documentName+"\t");
				weightOut.write(documentName+"\t");
			}
			
			// Print frequencies and weights
			for(String term:terms)
			{
				freqOut.newLine();
				weightOut.newLine();
				freqOut.write(term+"\t");
				weightOut.write(term+"\t");
				for(WSDLDocument wsdlDocument:wsdlDocuments)
				{
					freqOut.write(f_ij.get(term, wsdlDocument)+"\t");
					weightOut.write(w_ij.get(term, wsdlDocument)+"\t");
				}
				
				idfOut.write(term+"\t"+idf_i.get(term)+"\n");
			}
			
			idfOut.close();
			freqOut.close();
			weightOut.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
