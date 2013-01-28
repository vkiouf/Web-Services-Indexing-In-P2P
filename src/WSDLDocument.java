import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.tartarus.martin.Stemmer;

import edu.udo.cs.wvtool.config.WVTConfiguration;
import edu.udo.cs.wvtool.config.WVTConfigurationFact;
import edu.udo.cs.wvtool.generic.stemmer.DummyStemmer;
import edu.udo.cs.wvtool.generic.tokenizer.SimpleTokenizer;
import edu.udo.cs.wvtool.generic.tokenizer.WVTTokenizer;
import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.main.WVTFileInputList;
import edu.udo.cs.wvtool.main.WVTool;
import edu.udo.cs.wvtool.util.WVToolException;
import edu.udo.cs.wvtool.wordlist.WVTWordList;
// stemmer
// WV Tools

/**
 * Attributes of a wsdl document
 */
public class WSDLDocument
{
	/*=========================================================================
	 *					Fields
	 *=========================================================================*/
	
	String name;			// Name of wsdl document
	String extension;		// Extension of wsdl document
	String path;			// Full path of wsdl document
	URL url;				// Online document url
	Vector<String> tokens;	// Tokens separated by white space
	Vector<String> words;		// Content words extracted from document ( may contain same strings)
	Hashtable<String,Integer> stemmedWords;	//	Content words reduced to their base the extracted from document
											// Key=Word, Value= Number of occurences in document
	
	/*=========================================================================
	 *					Constructors
	 *=========================================================================*/
	
	/**
	 * @param path Path of wsdl document
	 */
	public WSDLDocument(String path)
	{
		this.path = path;
		
		// get name of wsdl document
		File file = new File(path);
		name = file.getName();
		int extIndx = name.lastIndexOf('.');	// start index of extension
		extension = name.substring(extIndx+1);	// extension is separated by dot from rest name 
		
		// Initialize lists
		tokens = new Vector<String>();
		words = new Vector<String>();
		stemmedWords = new Hashtable<String,Integer>();
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
	 * @param isStemmed True if is desired to get stemmed words
	 * @return Content words extracted from document ( may contain same strings)
	 */
	public Vector<String> getWords(boolean isStemmed)
	{
		Vector<String> mulStemmedWords = new Vector<String>();
		String word;
		int count;
		Vector<String> keys = new Vector<String>(stemmedWords.keySet());
		
		if(!isStemmed)
			return words;
		else
		{
			
			for(String key:keys)
			{
				count = stemmedWords.get(key);
				for(int i=0;i<count;i++)
					mulStemmedWords.add(key);
			}
			
			return mulStemmedWords;
			
			//return new Vector<String>(stemmedWords.keySet());
			//return  new Vector<String>(stemmedWords.keySet());
		}
	}

	/**
	 * @return Content words extracted from document reduced to their base pair with the number of occurrences in document
	 */
	public Hashtable<String, Integer> getWordsWithOccurrences()
	{
		return stemmedWords;
	}
	
	/**
	 * @return Content words extracted from document reduced to their base 
	 */
	/*public Vector<String> getBaseWords()
	{
		return new Vector<String>( baseWords.keySet());
	}*/

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
	 * @param words Content words extracted from document
	 */
	public void setWords(Vector<String> words)
	{
		this.words = words;
	}

	/**
	 * @param words Content words reduced to their base with number of occurrences in document
	 */
	public void setStemmedWords(Hashtable<String, Integer> stemmedWords)
	{
		this.stemmedWords = stemmedWords;
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
		/*--------------------------------------------------------------------------
		 * 				WVTOOL - Extract words from wsdl document without xml tags
		 *-------------------------------------------------------------------------*/
		
		// if file empty, then wordListFile has predefined name "wordlist.dat"
		if(wordListFile.isEmpty())
			wordListFile = "wordlist.dat";
		
		// get from WSDL document handler the general computing words
		//try
		//{
			if(WSDLDocumentHandler.generalComputingWords==null)
				WSDLDocumentHandler.readGeneralComputingWords();
		//}
//		catch (IOException e1)
//		{
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
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
			String[] words;	// words in composite word based on the assumption that a capital letter indicates the 
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
							this.words.add(word.toLowerCase());			// add word from file to words list of document
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
		for(String word:this.words)
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
			
			for(String word:this.words)
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
	 * Add content word to words vector. If word already exists, then is added again.
	 * @param word Content word of document
	 */
	public void addWord(String word)
	{
		words.add(word);
		addStemmedWord(WordDatabase.getWordNetStemWord(word));
	}
	
	/**
	 * Add base word in base words vector. 
	 * If word already exists, increment the occurrences number of word in document
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
	
	public String getNameWithoutExtension()
	{
		String nameWithoutExtension = name.substring(0,name.length()-extension.length()-1);	// name without extension
		
		return nameWithoutExtension;
	}
}		
