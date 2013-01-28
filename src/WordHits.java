import java.util.Vector;

import javax.persistence.Entity;

/**
 * 
 * 
 *
 */
@Entity
public class WordHits
{
	/*=========================================================================
	 *					Fields
	 *=========================================================================*/

	private String word;	// single word or co-occurence of words
	private long hits;		// number of hits for word 1 or co-occurrence
	

	/*=========================================================================
	 *					Constructors
	 *=========================================================================*/

	/**
	 * @param word1
	 * @param word2
	 * @param hits
	 */
	public WordHits(String word, long hits)
	{
		this.word = word;
		this.hits = hits;
	}

	/*=========================================================================
	 *					Getters
	 *=========================================================================*/

	/**
	 * @return the word1
	 */
	public String getWord()
	{
		return word;
	}

	/**
	 * @return the hits
	 */
	public long getHits()
	{
		return hits;
	}
}
