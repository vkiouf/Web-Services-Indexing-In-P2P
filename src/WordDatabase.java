import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import net.billylieurance.azuresearch.AzureSearchCompositeQuery;
import edu.sussex.nlp.jws.JWS;
import edu.sussex.nlp.jws.Lin;
import edu.mit.jwi.morph.WordnetStemmer;
//import edu.sussex.nlp.jws.WuAndPalmer;

/**
 *  Provides similarity methods for word based on wordnet database or in google/bing search engine.
 *  Holds information about number of hits of a word in a search engine
 */
public class WordDatabase
{
	/*=========================================================================
	 *					Fields
	 *=========================================================================*/
	
	// Use wordnet for similarity
	private static String dir="database/WordNet";
	private static JWS	ws= new JWS(dir, "3.0","ic-treebank-resnik.dat");
	private Lin lin;
	//private WuAndPalmer wuPalmer;
	private static WordnetStemmer stemmer= new WordnetStemmer(ws.getDictionary());;
	
	// Word hits
	private String bingHitsFile = "hits.dat";			// holds a list of words search in google and their corresponding hits 
	private String googleHitsFile = "ghits.dat";		// holds a list of words search in bing and their corresponding hits 
	private String hitsFile;							// hits list based on search engine selection
	Hashtable<String,Long> hits;						// number of hits for each word
	private static Hashtable<String,String> compositeWords=new Hashtable<String,String>();	// composite words separated by _

	// Function Words
	private transient String functionWordsFile = "function_words.dat";	// Function words filename
	private transient String[] functionWords;								// Array of function words
	
	SimilarityType simType;			// Type of  word similarity computation
	SearchEngine searchEngine;		// Selected search engine
	
	WebSearch webSearch;					// Web Search engine
	AzureSearchCompositeQuery  aq;			// Search in Bing
	
	long M = (long) 47e9;				// Total pages inddexed by search engine
	
	BufferedWriter out;					// Writer of word hits

	/*=========================================================================
	 *					Constructors
	 *=========================================================================*/
	
	/**
	 * Choose similarity measure. If similarity measure is WordNet, searchEngine should be none.
	 * @param simType Similarity Measure ( NGD or WordNet based)
	 * @param searchEngine In case of NGD, select search Engine Bing or Google
	 */
	public WordDatabase(SimilarityType simType)//,SearchEngine searchEngine)
	{
		this.simType = simType;
		this.searchEngine = SearchEngine.GOOGLE;
		
		if(simType == SimilarityType.NGD)
		{
			// Set search engine and storage of word resuls number file
			webSearch = new WebSearch(searchEngine);
			/*if(searchEngine == SearchEngine.BING)
		        hitsFile = bingHitsFile;
			else*/
			hitsFile = googleHitsFile;
	       
			// Read number of hits for each word from file
			loadHits();
	        
	        // Open file to write new words
			FileWriter fstream;
			try
			{
				fstream = new FileWriter(hitsFile,true);
				out = new BufferedWriter(fstream);
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		}
		else	// Initialise lin metric distance for each pair of words
		{
			lin = ws.getLin();
			//wuPalmer = ws.getWuAndPalmer();
		}
	}
	
	protected void finalize() throws Throwable
	{
		out.close();
		super.finalize(); //not necessary if extending Object.
	}
	
	/*=========================================================================
	 *					Getters
	 *=========================================================================*/
	
	/**
	 * @return the functionWords
	 */
	public String[] getFunctionWords()
	{
		return functionWords;
	}
	
	/*=========================================================================
	 *					Methods
	 *=========================================================================*/

	/*-----------------------------------------------------------------------
	 *					Similarity
	 *-----------------------------------------------------------------------*/
	
	/**
	 * Get similarity of two words using lin measure and Wordnet database
	 * @param word1 First Word
	 * @param word2 Second word
	 * @return Similarity between the two words
	 */
	private double wordNetsimilarity(String word1,String word2)
	{
		List<String> stemWords1 = stemmer.findStems(word1);	// list of stem words  for first word 
		List<String> stemWords2 = stemmer.findStems(word2); // list of stem words  for second word
		String stemWord1;
		String stemWord2;
		
		// For each word, check if stem words contains any stemmed word. If true, get first occurence
		// otherwise check if related to composite word ( which will be split by underscore)
		// If no of above conditions apply, get the original word ( returned from getWordNetCompositeWord)
		stemWord1 = stemWords1.size()>0?stemWords1.get(0):getWordNetCompositeWord(word1);
		stemWord2 = stemWords2.size()>0?stemWords2.get(0):getWordNetCompositeWord(word2);
		
		double val = lin.max(stemWord1, stemWord2,"n");
		return val;
	}
	
	/**
	 * Return the stem of word as contained in wordnet corpus. (e.g.) For word "bodies" , "body" will be returned and not "bodi".
	 * @param Word Word to reduce to base
	 * @return Stem of word
	 */
	public static String getWordNetStemWord(String word)
	{
		if(word.equals("sms"))
			return word;
			
		List<String> stemmedWords = stemmer.findStems(word);	// List of word stems
		String stemmedWord =  stemmedWords.size()>0?stemmedWords.get(0):word;	// Return the first one if exists. Otherwise, the original word
		
		return stemmedWord;
	}
	
	/**
	 * Find the composite word as contained in word net 
	 * @param word
	 * @return
	 */
	private static String getWordNetCompositeWord(String word)
	{
		StringBuffer wordBuff=new StringBuffer(word);
		List<String> stemWords;
		
		// Get composite word as contained in word net database ( separate simple words with underscore)
		if(compositeWords.containsKey(word))
			return compositeWords.get(word);
		
		// Put underscore in i-th place of word and test if this is contained in word net database
		// (using stemmer)
		for(int i=1;i<word.length();i++)
		{
			wordBuff = new StringBuffer(word);
			wordBuff.insert(i, "_");
			
			stemWords = stemmer.findStems(word);
			if(stemWords.size()>0)
			{
				compositeWords.put(word, stemWords.get(0));
				return stemWords.get(0);
			}
		}
		
		// Not found in wordnet. Return word as it is
		return word;
	}
	
	/**
	 * Google distance as similarity measure
	 * @param word1  First Word
	 * @param word2  Second word
	 * @return Normalized google distance between two words
	 */
	public double NormalizedGoogleDistance(String word1,String word2)
	{
		double NGD=0.0;
		long word1Hits = getHits(word1);
		long word2Hits = getHits(word2);
		long twoWordsHits = getHits(word1+" "+word2);
		
		NGD = (Math.max(Math.log10(word1Hits), Math.log10(word2Hits))-Math.log10(twoWordsHits))/
				(Math.log10(M)-Math.min(Math.log10(word1Hits), Math.log10(word2Hits)));
		
		if(NGD<0)
			NGD=0;
	
		return NGD;
	}
	
	/**
	 * Return the similarity between two words
	 * @param word1 First Word
	 * @param word2 Second word
	 * @return similarity
	 */
	public double similarity(String word1,String word2)
	{
		double sim= 0.0;
		double distance=0.0;
		
		if(simType==SimilarityType.WordNet)
			sim = wordNetsimilarity(word1, word2);
		else
		{
			if(!word1.contentEquals(word2))
				distance = NormalizedGoogleDistance(word1, word2);
			else
				distance = 0;
			
			if(distance>1)
				sim=0;
			else
				sim = 1-distance;
		}
			
		return sim;
	}
	
	/**
	 * Return the distance between two words
	 * @param word1 First Word
	 * @param word2 Second word
	 * @return similarity
	 */
	public double distance(String word1,String word2)
	{
		double distance= 0.0;
		
		if(simType==SimilarityType.WordNet)
			distance = 1-wordNetsimilarity(word1, word2);
		else
			distance = NormalizedGoogleDistance(word1, word2);
			
		return distance;
	}
	
	/**
	 * Get similarity or distance between two word arrays using lin measure and Wordnet database or Normalized google distance
	 * based on choice of this constructor
	 * Similarity is computed as the average of similarities of each pair of words
	 * @param words1 Words array 1
	 * @param words2 Words array 2
	 * @param isDistance True for distance. False for similarity
	 * @return Similarity Or Distance between two word arrays
	 */
	public double correlation(String[] words1,String[] words2,boolean isDistance)
	{
		double sum=0.0;	// sum of similarities
		int words1Len = words1.length;
		int words2Len = words2.length;
		double N =words1Len*words2Len;
		double average = 0.0;
		int i=0,j=0;
		
		for(i=0;i<words1Len;i++)
			for(j=0;j<words2Len;j++)
				if(isDistance)
					sum += distance(words1[i],words2[j]);
				else
					sum += similarity(words1[i],words2[j]);
		
		average = sum/N;
		
		return average;
	}
	
	public double correlation(Vector<String> words1,Vector<String> words2,boolean isDistance)
	{
		return correlation(words1.toArray(new String[words1.size()]),words2.toArray(new String[words2.size()]),isDistance);
	}
	
	/**
	 * Find estimated document frequency for a word based on difference of num hits for at least one occurrence
	 * and at at least two occurrences of word
	 * @param word Word to estimate
	 * @return Estimated document frequency
	 */
	public double estimatedDocFreq(String word)
	{
		long singleWordHits = getHits(word);
		long doubleWordHits = getHits(word+"*"+word);
		long diffWordHits = singleWordHits-doubleWordHits;
		
		if( Double.isInfinite(diffWordHits))
			return 0.0;
		
		return (double)diffWordHits/singleWordHits;
	}
	
	/*-----------------------------------------------------------------------
	 *					Hits 
	 *-----------------------------------------------------------------------*/
	
	/**
	 *	Update word hits database read from file
	 */
	public void updateHitsDBFromFile()
	{
		String word;	// current word
		long numHits;	// hits
		String strLine; // line read
	//	hits = new Hashtable<String,Long>();
		
		// delete all entries
		WebServicesClusterer.em.getTransaction().begin();
		Query query = WebServicesClusterer.em.createQuery(
			      "DELETE FROM WordHits");
		query.executeUpdate();
				
		try
		{
			FileInputStream fstream = new FileInputStream(hitsFile);

			// Get the object of DataInputStream
		   DataInputStream in = new DataInputStream(fstream);
		   BufferedReader br = new BufferedReader(new InputStreamReader(in));
		   
		   //Read File Line By Line
		   while ((strLine = br.readLine()) != null)   
		   {
			  word =strLine.split("\t")[0];
			  numHits = Long.parseLong(strLine.split("\t")[1]);
			  WebServicesClusterer.em.persist(new WordHits(word,numHits));
			 // this.hits.put(word, numHits);
		   }
		   
		   WebServicesClusterer.em.getTransaction().commit();
		   
		   //Close the input stream
		   in.close();
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Load all words with hits from database
	 */
	public void loadHits()
	{
		TypedQuery<WordHits> query = WebServicesClusterer.em.createQuery("SELECT wordhits FROM WordHits wordhits", WordHits.class);
		List<WordHits> wordsHits = query.getResultList();
		
		this.hits = new Hashtable<String,Long>();
		for(WordHits wordHits:wordsHits)
			this.hits.put(wordHits.getWord(), wordHits.getHits());
	}
	
	/**
	 * Get number of hits for word. First check if word exists in hits map , otherwise do a query
	 * to search engine and store result to hits file and map.
	 * @param word Word 
	 */
	public long getHits(String word)
	{	
		if(hits.keySet().contains(word))
			return hits.get(word);
		/*TypedQuery<Long> query = WebServicesClusterer.em.createQuery(
			      "SELECT wordhits.hits  FROM WordHits wordhits, Long.class);
		if( query.getResultList().size()>0)
			return query.getResultList().get(0);*/
		
		// if word not exist, search in bing search engine
		long numHits=0;
		
		try
		{
			Thread.sleep(1000);// do search every second
			numHits = webSearch.search(word.split(" "));	// If more than one words, split them based on whitespace
			
			// update db
			if(!WebServicesClusterer.em.getTransaction().isActive())
			{
				WebServicesClusterer.em.getTransaction().begin();
				WebServicesClusterer.em.persist(new WordHits(word,numHits));
				WebServicesClusterer.em.getTransaction().commit();
			}
			else
				WebServicesClusterer.em.persist(new WordHits(word,numHits));
			
			// update file
			out.write(word+"\t"+numHits);
			out.newLine();
			out.flush();
		}
		catch (IOException | InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		hits.put(word, numHits);
		
		return numHits;
	}
	
	/*-----------------------------------------------------------------------
	 *					Function Words
	 *-----------------------------------------------------------------------*/
	
	/**
	 * Read the file of function words and stores them in database( rewrite table)
	 * @return Array of function words
	 * @throws IOException 
	 */
	public void updateFunctionWordsDBFromFile() 
	{
		int numFunctionWords=0;						// number of function words contained in file
		LineNumberReader lineNumberReader=null;		// reader of line number 
		BufferedReader br=null;						// function word file reader
		FileInputStream fstream = null;
		
		// delete all entries
		WebServicesClusterer.em.getTransaction().begin();
		Query query = WebServicesClusterer.em.createQuery(
			      "DELETE FROM FunctionWord");
		query.executeUpdate();
		
		try
		{
			// Open the file
			fstream = new FileInputStream(functionWordsFile);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			 
			// Get number of function words equal to number of lines
			lineNumberReader = new LineNumberReader(br);
			while(lineNumberReader.readLine()!=null);
			numFunctionWords = lineNumberReader.getLineNumber();
			
			// Initialise function words
			this.functionWords = new String[numFunctionWords];
			
			// Reset stream and read each function word
			fstream.getChannel().position(0);
			br = new BufferedReader(new InputStreamReader(in));
			for(int i=0;i<numFunctionWords;i++)
			{
				functionWords[i] = br.readLine().trim();
			//	WebServicesClusterer.em.persist(functionWords[i] );
			}
			
			WebServicesClusterer.em.persist(new FunctionWord(functionWords));
			WebServicesClusterer.em.getTransaction().commit();
			
			lineNumberReader.close();
			
			fstream.close();
			br.close();
		}
		catch(IOException ioex)
		{
			ioex.printStackTrace();
		}
		
	}
	
	/**
	 * Read function words from database
	 * @return Array of function words
	 */
	public String[] readFunctionWords()
	{
		TypedQuery<FunctionWord> query = WebServicesClusterer.em.createQuery(
			      "SELECT fw FROM FunctionWord AS fw ", FunctionWord.class);
		FunctionWord functionWord = query.getResultList().get(0);
		
		this.functionWords = functionWord.getWords();
		
		return this.functionWords;
	}
	
	/*-----------------------------------------------------------------------
	 *					Word Manipulation
	 *-----------------------------------------------------------------------*/
	
	/**
	 * Split composite word in multiple words based on the assumption that a capital
	 * letter indicates the start of a new word
	 * @param word
	 * @return
	 */
	public static Vector<String> splitWord(String word)
	{
		Vector<String> words = new Vector<String>();				// simple words
		Vector<String> abbreviations = findWordsInCapitals(word);	// words composed of capital letters
		StringBuffer wordBuffer = new StringBuffer(word);			// remove abbreviations
		String[] simpleWords;						// words without abbreviations
		
		// remove all abbreviations from word
		for(int i=0;i<abbreviations.size();i++)
			word = word.replaceAll(abbreviations.get(i), "");
		
		words.addAll(abbreviations);	// add abbreviations in words set
		
		// add words separated by capital letters
		simpleWords = wordBuffer.toString().split("(?=\\p{Upper})");
		for(String simpleWord:simpleWords)
		{
			if(simpleWord.matches(".*\\d.*")) // if word contains number, remove number
				simpleWord = simpleWord.replaceAll("\\d","");
			
			simpleWord = simpleWord.trim();
			if(!simpleWord.isEmpty() && simpleWord.length()>1 && !simpleWord.contains("_"))
				words.add(simpleWord);
		}
		
		return words;
	}
	
	/**
	 * Searches in a string for continuous capitals  
	 * @param word Word to check
	 * @return Words in capitals
	 */
	public static Vector<String> findWordsInCapitals(String word)
	{
		Vector<String> wordsInCapitals = new Vector<String>();
		StringBuffer wordBuff=new StringBuffer();
		
		for(int i=0;i<word.length();i++)
		{
			if(Character.isUpperCase(word.charAt(i)))
				wordBuff.append(word.charAt(i));
			
			if(Character.isLowerCase(word.charAt(i)) || Character.isDigit(word.charAt(i))
					|| i==word.length()-1)
			{
				if(wordBuff.length()>0)
				{
					if(wordBuff.length()>1)
						if(Character.isLowerCase(word.charAt(i)))
							wordsInCapitals.add(wordBuff.substring(0,wordBuff.length()-1));
						else
							wordsInCapitals.add(wordBuff.substring(0,wordBuff.length()));
			
					wordBuff = new StringBuffer();
				}
			}
	
		}
		
		return wordsInCapitals;
	}
}

enum SearchEngine{ BING,GOOGLE,NONE};

enum SimilarityType{NGD,WordNet}
