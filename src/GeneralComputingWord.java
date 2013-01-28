import java.util.Vector;

import javax.persistence.*;
/**
 * Entity class of general computing word
 */
@Entity
public class GeneralComputingWord
{
	/*=========================================================================
	 *					Fields
	 *=========================================================================*/
	
	private Vector<String> words;	// member of class to store in database
	

	/*=========================================================================
	 *					Constructors
	 *=========================================================================*/
	
	public GeneralComputingWord(Vector<String> words)
	{
		this.words=words;
	}
	
	/*=========================================================================
	 *					Getters
	 *=========================================================================*/
	
	/**
	 * @return the word
	 */
	public  Vector<String>getWords()
	{
		return words;
	}
	
	/*=========================================================================
	 *					Methods
	 *=========================================================================*/
	
	
}

