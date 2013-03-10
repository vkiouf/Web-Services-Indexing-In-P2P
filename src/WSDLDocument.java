import java.io.*;
import java.net.URL;
import java.util.*;

import java.util.Vector;

import javax.persistence.*;

import org.tartarus.martin.Stemmer;

import edu.udo.cs.wvtool.config.*;
import edu.udo.cs.wvtool.generic.stemmer.DummyStemmer;
import edu.udo.cs.wvtool.generic.tokenizer.*;
import edu.udo.cs.wvtool.main.*;
import edu.udo.cs.wvtool.util.*;
import edu.udo.cs.wvtool.wordlist.*;

/**
 * Attributes of a wsdl document
 */
@Entity
public class WSDLDocument
{
	/*=========================================================================
	 *					Fields
	 *=========================================================================*/

	String name;			// Name of wsdl document
	transient String extension;		// Extension of wsdl document
	transient String path;			// Full path of wsdl document
	transient URL url;				// Online document url
	transient Vector<String> tokens;	// Tokens separated by white space
	Hashtable<String,Integer> words;		// Content words extracted from document with number of occurences for each one
											// Key = word, Value = Number of occurences in document
	Hashtable<String,Integer> stemmedWords;	//	Content words reduced to their base the extracted from document
											// Key=Word, Value= Number of occurences in document
	String repository;							// Repository name where document belongs to

	String category;						// Category where document belongs to

	HashMap<String,Double> f_ij;			// Normalized term frequency document
	HashMap<String,Double> weights;			// Weights in vector space model
	
	/*=========================================================================
	 *					Constructors
	 *=========================================================================*/
	
	/**
	 * @param repository  Repository name of wsdl document
	 * @param path Path of wsdl document
	 * @param category Category name of document in repository
	 */
	public WSDLDocument(String repository,String path,String category)
	{
		this.path = path;
		this.repository = repository;
		
		// get name of wsdl document
		File file = new File(path);
		name = file.getName();
		int extIndx = name.lastIndexOf('.');	// start index of extension
		extension = name.substring(extIndx+1);	// extension is separated by dot from rest name 
		
		// Initialize lists
		tokens = new Vector<String>();
		words = new Hashtable<String,Integer>();
		stemmedWords = new Hashtable<String,Integer>();
		
		// find category of document
		this.category = category;
	}
	
	/**
	 * @param name
	 * @param words
	 * @param stemmedWords
	 * @param category
	 * @param f_ij
	 * @param weights
	 */
	public WSDLDocument(String repository,String name, Hashtable<String, Integer> words,
			Hashtable<String, Integer> stemmedWords, String category,
			HashMap<String, Double> f_ij, HashMap<String, Double> weights)
	{
		this.repository = repository;
		this.name = name;
		this.words = words;
		this.stemmedWords = stemmedWords;
		this.category = category;
		this.f_ij = f_ij;
		this.weights = weights;
	}
	

	/*=========================================================================
	 *					Getters
	 *=========================================================================*/

	/**
	 * @return Name of document
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * @return Extension of document
	 */
	public String getExtension()
	{		
		return extension;
	}

	/**
	 * @return the path
	 */
	public String getPath()
	{
		return path;
	}

	/**
	 * @return Contents parsed based on white space
	 */
	public Vector<String> getTokens()
	{
		return tokens;
	}
	
	/**
	 * @param isStemmed True to get words stemmed to their base word
	 * @return Content words extracted from document ( single occurrence of each one)
	 * @throws Exception If words is empty list.
	 */
	public Vector<String> getWords(boolean isStemmed) throws Exception
	{
		if(words==null || words.isEmpty())
			throw new Exception("Document words list is empty.\n");
		
		if(!isStemmed)
			return new Vector<String>(words.keySet());
		else
			return new Vector<String>(stemmedWords.keySet());
	}

	/**
	 * @param isStemmed  True to get words stemmed to their base word
	 * @return Content words extracted from document with frequencies
	 * @throws Exception If words is empty list
	 */
	public Hashtable<String, Integer> getTermsFrequencies(boolean isStemmed) throws Exception
	{
		if(words==null || words.isEmpty())
			throw new Exception("Document words list is empty.\n");
		
		if(isStemmed)
			return stemmedWords;
		else
			return words;
	}
	
	/**
	 * @return the category
	 */
	public String getCategory()
	{
		return category;
	}
	
	/**
	 * @return the f_ij
	 */
	public HashMap<String, Double> getF_ij()
	{
		return f_ij;
	}

	/**
	 * @return the weights
	 */
	public HashMap<String, Double> getWeights()
	{
		return weights;
	}
	
	/**
	 * @return the repository
	 */
	public String getRepository()
	{
		return repository;
	}

	/*=========================================================================
	 *					Setters
	 *=========================================================================*/

	/**
	 * @param tokens Contents parsed based on white space
	 */
	public void setTokens(Vector<String> tokens)
	{
		this.tokens = tokens;
	}
	

	/**
	 * @param words Content words reduced to their base with number of occurrences in document
	 */
	public void setStemmedWords(Hashtable<String, Integer> stemmedWords)
	{
		this.stemmedWords = stemmedWords;
	}
	
	/**
	 * @param f_ij the f_ij to set
	 */
	public void setF_ij(HashMap<String, Double> f_ij)
	{
		this.f_ij = f_ij;
	}

	/**
	 * @param weights the weights to set
	 */
	public void setWeights(HashMap<String, Double> weights)
	{
		this.weights = weights;
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
	 * Extract words from wsdl document without xml tags,split compound words 
	 * and reduce them to their base words
	 * @param wordListFile Contains result of wvtool
	 */
	public void parse(String wordListFile)
	{
	
		String[] words;	// words in composite word based on the assumption that a capital letter indicates the 
		
		/*--------------------------------------------------------------------------
		 * 				WVTOOL - Extract words from wsdl document without xml tags
		 *-------------------------------------------------------------------------*/
		
		// if file empty, then wordListFile has predefined name "wordlist.dat"
		if(wordListFile.isEmpty())
			wordListFile = "wordlist.dat";
		
		WVTool wvt = new WVTool(false);
		
		// Initialize the configuration
		WVTConfiguration config = new WVTConfiguration();
		WVTTokenizer tokenizer= new SimpleTokenizer();
		
		config.setConfigurationRule(WVTConfiguration.STEP_TOKENIZER, new WVTConfigurationFact(tokenizer));
		config.setConfigurationRule(WVTConfiguration.STEP_STEMMER, new WVTConfigurationFact(new DummyStemmer()));
		//congig.setConfigurationRule(WVTConfiguration.s)
		
		// Initialize the input list with two classes
		WVTFileInputList list = new WVTFileInputList(0);
		
		// Add entries
		list.addEntry(new WVTDocumentInfo(this.path, "txt", "", "english", 0));
		WVTWordList wordList ;
		try
		{
			 wordList = wvt.createWordList(list, config);
							 
			// Store the word list in a file
		    wordList.storePlain(new FileWriter(wordListFile));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (WVToolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		/*--------------------------------------------------------------------------
		 * 			Store in vector
		 *-------------------------------------------------------------------------*/
		
		/* Reads from wordlist.txt the words extracted and store them in vector for processing*/
		
		try
		{
			// Open the file 
			FileInputStream fstream = new FileInputStream(wordListFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
  
			String strLine;	// line read from file
			// start of a new word

			//Read File Line By Line ( Each line contains one word)
			while ((strLine = br.readLine()) != null)   
			{
				while((strLine = br.readLine())!=null)
				{
					if(WSDLDocumentHandler.generalComputingWords.contains(strLine.toLowerCase()))
						continue;
						
					words = strLine.toString().split("(?=\\p{Upper})");	// split words based on upper cases
					for(String word:words)
						if(!WSDLDocumentHandler.generalComputingWords.contains(word.toLowerCase())
								&& !this.words.contains(word.toLowerCase()) && !word.trim().isEmpty()
								&& word.length()!=1)
							if(this.words.containsKey(word.toLowerCase()))
								this.words.put(word.toLowerCase(),1);			// add word from file to words list of document
							else
								this.words.put(word.toLowerCase(),this.words.get(word)+1);
				}
			}	
			
			br.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		/*--------------------------------------------------------------------------
		 * 			Stemmer
		 *-------------------------------------------------------------------------*/
		
		Stemmer stemmer = new Stemmer();				// object to reduce words to their base
		words =(String[]) this.words.keySet().toArray();	// convert words keyset to array
		for(String word:words)
		{
			stemmer.add(word.toLowerCase().toCharArray(),word.length());
			stemmer.stem();
			addStemmedWord(word);
		}
		
		FileWriter fstream;
		try
		{
			fstream = new FileWriter("document_words/"+getNameWithoutExtension()+"_words.dat");
			BufferedWriter out = new BufferedWriter(fstream);
			
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
		 
	}
	
	/**
	 * Add a new token parsed from document in tokens vector
	 * 
	 * @param token Token added to document
	 */
	public void addToken(String token)
	{
		tokens.add(token);
	}
	
	/**
	 * Add content word to words vector, setting also the number of occurrences
	 * @param word Content word of document
	 */
	public void addWord(String word)
	{
		if(this.words.containsKey(word))
			this.words.put(word, this.words.get(word)+1);
		else
			this.words.put(word, 1);
		
		addStemmedWord(WordDatabase.getStemmedWord(word));
	}
	
	/**
	 * Add a collection of words in word vector of document
	 * @param words Words collection
	 */
	public void addWord(Collection<String> words)
	{
		//this.words.addAll(words);
		for(String word:words)
		{
			addWord(word);
			addStemmedWord(WordDatabase.getStemmedWord(word));
		}
	}
	
	/**
	 * Add stemmed word in stemmed words vector. 
	 * If word already exists, increment the term frequency by one
	 * 
	 * @param stemmedWord Content word reduced to its base 
	 */
	public void addStemmedWord(String stemmedWord)
	{
		if(stemmedWord.isEmpty()) // if word is empty , return
			return;
		
		if(stemmedWord.length()==1)	// letter
			return;
		
		if(!stemmedWords.containsKey(stemmedWord))
			stemmedWords.put(stemmedWord,1);
		else
			stemmedWords.put(stemmedWord,stemmedWords.get(stemmedWord)+1);
	}
	
	/**
	 * Remove stemmed word word from stemmedWord words vector
	 * @param stemmedWord Base word to remove
	 */
	public void removeStemmedWord(String stemmedWord)
	{
		if(stemmedWords.contains(stemmedWord))
			stemmedWords.remove(stemmedWord);
	}
	
	/**
	 * Split the composite name of web service extracted  from its wsdl document into
	 * multiple names based on the assumption that a capital letter indicates the start of a new word
	 * @return Composite name into its parts
	 */ 
	public String[] getWebServiceNameWords()
	{
		String nameWithoutExtension = name.substring(0,name.length()-extension.length()-1);	// name without extension
		String[] names = nameWithoutExtension.split("(?=\\p{Upper})");						// split based on Capital letters
		
		// remove empty strings
		List<String> namesL = new ArrayList<String>(names.length);
		Collections.addAll(namesL, names);
		Iterator<String> namesLIter = namesL.iterator();
		while(namesLIter.hasNext())
			if(namesLIter.next().isEmpty())
				namesLIter.remove();
		
		return namesL.toArray(new String[namesL.size()]);
	}
	
	/**
	 * Get the filename without extension
	 * @return Document name without extension
	 */
	public String getNameWithoutExtension()
	{
		String nameWithoutExtension = name.substring(0,name.length()-extension.length()-1);	// name without extension
		
		return nameWithoutExtension;
	}
	
	/*//////////////////////////////////////////////////////////////////////////
	 *					Extract Content 
	 *////////////////////////////////////////////////////////////////////////*/
	
	/**
	 * Extracts a list of content words for each web service description
	 * excluding stop words, function words and general computing words.
	 * 
	 * The words vector is returned from getWords method
	 * 
	 * @param loadDB True to get word vector from database. The rest of parameters ignored if set to true.
	 * @param isStemmed True to extract words stemmed into their base words
	 * @param isDescriptionIncluded True to extract tokens from documentation tags
	 * @param isURLIncluded True to extract tokens from urls
	 * @param isCommentIncluded True to extract tokens from comments
	 */
	public void extractContent(boolean loadDB,boolean isStemmed,boolean isDescriptionIncluded,boolean isURLIncluded,boolean isCommentIncluded)
	{
		Set<String> words;					// Words extracted from document before removing function and general computing words
		String wordsFile;						// File to store content words vector
		WSDLDocumentHandler documentHandler;	// WSDL Document Handler
		FileWriter fstream;
		BufferedWriter out;
		
		this.words = new Hashtable<String,Integer>();
		this.stemmedWords = new Hashtable<String,Integer>();
		
		System.out.println("\t1. Extract wsdl content...");
		
		// Extract content
		System.out.println("\t\t Processing "+getName()+" -- >");
		System.out.println("\t\t\t Step 1 : Extract Content... ");
		documentHandler = new WSDLDocumentHandler(this);
		
		// If loadDB is false, then delete the record of document word vector from database to reconstruct it
		if(!loadDB)
		{
			ObjectDBConn.em.getTransaction().begin();
			ObjectDBConn.em.createQuery(
				      "DELETE FROM DocumentWordVector AS DocWordVector WHERE repository='"+repository+"' AND document = '"+getNameWithoutExtension()+"'");
			ObjectDBConn.em.getTransaction().commit();
		}
		
		//Check if there is word vector in database
		TypedQuery<DocumentWordVector> query = ObjectDBConn.em.createQuery(
			      "SELECT DocWordVector FROM DocumentWordVector AS DocWordVector WHERE repository='"+repository+"' AND document =  '"+getNameWithoutExtension()+"'", DocumentWordVector.class);
		
		// There is not yet a word vector of document in object db. Extract content from file
		if(query.getResultList().size()==0)
			documentHandler.extractContent(isDescriptionIncluded,isURLIncluded,isCommentIncluded);
		else
		{
			// Get word vector from database
			DocumentWordVector documentWordVector = query.getResultList().get(0);
			this.words = documentWordVector.getWords();
			this.stemmedWords = documentWordVector.getStemmedWords();
		}
		
		// Remove function words
		System.out.println("\t\t\tStep 2 : Remove Function Words... ");
		removeFunctionWords(isStemmed);
	
		wordsFile = "document_words/"+getNameWithoutExtension()+"_words.dat";	// file to store words extracted from wsdl document
		//System.out.println("\t\t\t... Write words to "+wordsFile);
		
		words= this.stemmedWords.keySet();
		
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
		
		// Remove general computing words
		System.out.println("\t\t\tStep 3 : Remove General Computing Words... ");
		recogniseContentWords();
		
		// Save the remaining words as word vector of document
		if(query.getResultList().size()==0)
		{
			ObjectDBConn.em.getTransaction().begin();
			ObjectDBConn.em.persist(new DocumentWordVector(repository,getNameWithoutExtension(),this.words,stemmedWords));
			ObjectDBConn.em.getTransaction().commit();
		}
		
		//wordsFile = "document_words/"+getNameWithoutExtension()+"_content.dat";	// file to store words extracted from wsdl document
		//System.out.println("\t\t\t... Write content to "+wordsFile);
		//contentWords = getWords(isStemmed); // get map of
		/*try
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
		}*/
	}
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 *		WSDL Content													
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	
	/**
	 * Remove all function words from the  word vector
	 * 
	 * @param isStemmed True to remove function words only from stemmed words
	 * @param document WSDL document
	 */
	private void removeFunctionWords(boolean isStemmed)
	{
	//	Hashtable<String,Integer> words = getTermsFrequencies(isStemmed);		// Stemmed words with frequencies in document
		String stemmedWord;		// word reduced to its base
		String[] functionWords = WordDatabase.getFunctionWords();			// Function words
		
		// If document words vector contains any of function words, remove it from words vector
		for(String functionWord:functionWords)
			if(words.containsKey(functionWord))
			{
				words.remove(functionWord);
				stemmedWord = WordDatabase.getStemmedWord(functionWord);
				stemmedWords.remove(stemmedWord);
			}
	}
	
	/**
	 * Remove words that do not describe the specific semantics of the web service such
	 * as 'data','web','port'
	 */
	private void recogniseContentWords()
	{
		try
		{
			/*Hashtable<String, Integer> words;
			words = getTermsFrequencies(false);
			Hashtable<String,Integer> stemmedWords = getTermsFrequencies(true);*/
			String stemmedWord;		// word reduced to its base
			Vector<String> generalComputingWords = WSDLDocumentHandler.getGeneralComputingWords();
		
			// If document words vector contains any of general computing words, remove it from words vector.
			// Also, remove it from stemmed words
			for(String generalComputingWord:generalComputingWords)
				if(words.containsKey(generalComputingWord))
				{
					words.remove(generalComputingWord);
					stemmedWord = WordDatabase.getStemmedWord(generalComputingWord);
					stemmedWords.remove(stemmedWord);
				}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 *		Misc													
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	
	/**
	 * Find the category where document belongs to and update category property
	 * @param categories A predefined set of categories which should contain the current document
	 */
	/*private void findCategory()
	{
		WSDLCategory wsdlCategory = new WSDLCategory(repository);
		HashMap<String,Vector<String>> categories = wsdlCategory.getCategories();	// retrieve all categories
		
 	}*/
	
	/**
	 * Updates normalized frequency for a term of document
	 * @param term Document term
	 * @param f_term Normalized frequency
	 */
	public void addNormalizedTermFrequency(String term,double f_term)
	{
		if(f_ij==null)
			f_ij = new HashMap<String,Double>(words.size());
		
		if(this.stemmedWords.containsKey(term))
			f_ij.put(term, f_term);	
	}
	
	/**
	 * Updates weight of a document term in vector space model
	 * @param term Document term
	 * @param f_term Weight of term in document
	 */
	public void updateWeights(String term,double w)
	{
		if(weights==null)
			weights = new HashMap<String,Double>(words.size());
		
		if(this.stemmedWords.containsKey(term))
			weights.put(term, w);
	}
}		
