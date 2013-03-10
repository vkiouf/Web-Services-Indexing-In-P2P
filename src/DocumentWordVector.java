import java.util.*;

import javax.persistence.*;


/**
 *  Word vector of document for storing in objectdb
 */
@Entity
public class DocumentWordVector
{
	/*=========================================================================
	 *					Fields
	 *=========================================================================*/
	
	String repository;

	String document;
	Hashtable<String,Integer> words;	
	Hashtable<String,Integer> stemmedWords;
	
	/*=========================================================================
	 *					Constructors
	 *=========================================================================*/
	
	/**
	 * @param document
	 * @param words
	 * @param stemmedWords
	 */
	public DocumentWordVector(String repository,String document, Hashtable<String, Integer> words,
			Hashtable<String, Integer> stemmedWords)
	{
		this.repository = repository;
		this.document = document;
		this.words = words;
		this.stemmedWords = stemmedWords;
	}
	
	/*=========================================================================
	 *					Getters
	 *=========================================================================*/
	
	/**
	 * @return the repository
	 */
	public String getRepository()
	{
		return repository;
	}

	/**
	 * @return the document
	 */
	public String getDocument()
	{
		return document;
	}

	/**
	 * @return the words
	 */
	public Hashtable<String, Integer> getWords()
	{
		return words;
	}
	/**
	 * @return the stemmedWords
	 */
	public Hashtable<String, Integer> getStemmedWords()
	{
		return stemmedWords;
	}
	
	/**
	 * @param repository the repository to set
	 */
	public void setRepository(String repository)
	{
		this.repository = repository;
	}
}
