import java.util.*;
import java.io.*;

import org.tartarus.martin.Stemmer;	// stemmer

// WV Tools
import edu.udo.cs.wvtool.main.*;
import edu.udo.cs.wvtool.config.*;
import edu.udo.cs.wvtool.generic.stemmer.*;
import edu.udo.cs.wvtool.generic.tokenizer.*;
import edu.udo.cs.wvtool.util.WVToolException;
import edu.udo.cs.wvtool.wordlist.*;

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
	Vector<String> tokens;	// Tokens separated by white space
	Vector<String> words;		// Content words extracted from document ( may contain same strings)
	Hashtable<String,Integer> baseWords;	//	Content base words extracted from document
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
		baseWords = new Hashtable<String,Integer>();
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
	 * @return Content words extracted from document ( may contain same strings)
	 */
	public Vector<String> getWords()
	{
		return words;
	}

	/**
	 * @return Content words extracted from document reduced to their base pair with the number of occurrences in document
	 */
	public Hashtable<String, Integer> getBaseWordsWithOccurrences()
	{
		return baseWords;
	}
	
	/**
	 * @return Content words extracted from document reduced to their base 
	 */
	public Vector<String> getBaseWords()
	{
		return new Vector<String>( baseWords.keySet());
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
	 * @param words Content words extracted from document
	 */
	public void setWords(Vector<String> words)
	{
		this.words = words;
	}

	/**
	 * @param words Content words reduced to their base with number of occurrences in document
	 */
	public void setBaseWords(Hashtable<String, Integer> baseWords)
	{
		this.baseWords = baseWords;
	}
	
	
	/*=========================================================================
	 *					Methods
	 *=========================================================================*/
	
	/**
	 * Extract words from wsdl document without xml tags,split compound words 
	 * and reduce them to their base words
	 */
	public void parse()
	{
		/*--------------------------------------------------------------------------
		 * 				WVTOOL - Extract words from wsdl document without xml tags
		 *-------------------------------------------------------------------------*/
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
		list.addEntry(new WVTDocumentInfo(path, "txt", "", "english", 0));
		WVTWordList wordList ;
		try
		{
			 wordList = wvt.createWordList(list, config);
							 
			// Store the word list in a file
		    wordList.storePlain(new FileWriter("wordlist.txt"));
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
			FileInputStream fstream = new FileInputStream("wordlist.txt");
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
					words = strLine.toString().split("(?=\\p{Upper})");	// split words based on upper cases
					for(String word:words)
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
			addBaseWord(stemmer.toString());
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
	}
	
	/**
	 * Add base word in base words vector. 
	 * If word already exists, increment the occurrences number of word in document
	 * 
	 * @param baseWord Content word reduced to its base 
	 */
	public void addBaseWord(String baseWord)
	{
		if(baseWord.isEmpty()) // if word is empty , return
			return;
		
		if(baseWord.length()==1)	// letter
			return;
		
		if(!getBaseWords().contains(baseWord))
			baseWords.put(baseWord,1);
		else
			baseWords.put(baseWord,baseWords.get(baseWord)+1);
	}
	
	/**
	 * Remove baseWord word from baseWord words vector
	 * @param baseWord Base word to remove
	 */
	public void removeBaseWord(String baseWord)
	{
		if(baseWords.contains(baseWord))
			baseWords.remove(baseWord);
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
