import edu.sussex.nlp.jws.*;
import java.util.*;
import java.io.*;

import net.billylieurance.azuresearch.*;

/**
 * 
 * Static methods about words (e.g. similarity)
 */
public class WordDatabase
{
	/*=========================================================================
	 *					Fields
	 *=========================================================================*/
	
	// Use wordnet for similarity
	private String dir;
	private JWS	ws;
	private Lin lin;
	
	// Word hits
	private String bingHitsFile = "hits.dat";
	private String googleHitsFile = "ghits.dat";		// holds a list of words search in bing and their corresponding hits in each line
	private String hitsFile;						// hits list based on search engine selection
	Hashtable<String,Long> hits;		// number of hits for each word
	
	SimilarityType simType;			// Type of  word similarity computation
	SearchEngine searchEngine;		// Selected search engine
	
	WebSearch webSearch;					// Web Search engine
	AzureSearchCompositeQuery  aq;	// Search in Bing
	
	long M = (long) 13.96e9;

	/*=========================================================================
	 *					Constructors
	 *=========================================================================*/
	
	/**
	 * Choose similarity measure. If similarity measure is WordNet, searchEngine should be none.
	 * @param simType Similarity Measure ( NGD or WordNet based)
	 * @param searchEngine In case of NGD, select search Engine Bing or Google
	 */
	public WordDatabase(SimilarityType simType,SearchEngine searchEngine)
	{
		this.simType = simType;
		this.searchEngine = searchEngine;
		
		if(simType == SimilarityType.NGD)
		{
			webSearch = new WebSearch(searchEngine);
			if(searchEngine == SearchEngine.BING)
			{
				/*aq = new AzureSearchCompositeQuery();
				aq.setSources(new AZURESEARCH_QUERYTYPE[] {
		       	     AZURESEARCH_QUERYTYPE.WEB
		      	});
				aq.setBingApi(AZURESEARCH_API.BINGSEARCH);
				aq.setMarket("el-GR");
		        aq.setAppid("rDmZjn0iKtQ6aJRfb38kKY2Al7CswpekLuCDzJ5xDvo=");*/
		        hitsFile = bingHitsFile;
			}
			else
			{
				
				hitsFile = googleHitsFile;
			}
	       
	        readHitsFile();
		}
		else
		{
			dir = "database/WordNet";
			ws = new JWS(dir, "3.0"); 
			lin = ws.getLin();
		}
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
		return lin.max(word1, word2, "n");
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
			NGD = 0;
		
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
		
		if(simType==SimilarityType.WordNet)
			sim = wordNetsimilarity(word1, word2);
		else
			sim = 1-NormalizedGoogleDistance(word1, word2);
			
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
		
		return diffWordHits;
	}
	
	/*-----------------------------------------------------------------------
	 *					Hits 
	 *-----------------------------------------------------------------------*/
	
	/**
	 * Reads hits file where each line store the keyword and its number of hits in bing search engine
	 */
	private void readHitsFile()
	{
		String word;	// current word
		long numHits;	// hits
		String strLine; // line read
		hits = new Hashtable<String,Long>();
		
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
			  this.hits.put(word, numHits);
		   }
		   
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
	 * Get number of hits for word. First check in hits map if word exists else
	 * search in bing search engine. In second case, insert new word in file
	 * @param word Word 
	 */
	public long getHits(String word)
	{	
		if(hits.keySet().contains(word))
			return hits.get(word);
		
		// if word not exist, search in bing search engine
		long numHits=0;
		FileWriter fstream;
		try
		{
		
//			if(searchEngine == SearchEngine.BING)
//			{
//				/*aq.setQuery(word);
//				aq.doQuery();
//				AzureSearchResultSet<AbstractAzureSearchResult> ars = aq.getQueryResult();
//				numHits = ars.getWebTotal();*/
//				Thread.sleep(100);// do search every second
//				numHits = gs.bingSearch(word.split(" "));	// If more than one words, split them based on whitespace
//			}
//			else if(searchEngine == SearchEngine.GOOGLE)
//			{
				Thread.sleep(100);// do search every second
				numHits = webSearch.search(word.split(" "));	// If more than one words, split them based on whitespace
				
//			}
		
			fstream = new FileWriter(hitsFile,true);
			BufferedWriter out = new BufferedWriter(fstream);
			
			out.write(word+"\t"+numHits);
			out.newLine();
			
			out.close();
		}
		catch (IOException | InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		hits.put(word, numHits);
		
		return numHits;
	}
}

enum SearchEngine{ BING,GOOGLE,NONE};

enum SimilarityType{NGD,WordNet}
