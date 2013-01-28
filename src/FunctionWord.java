import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Vector;

import javax.persistence.*;

/**
 * Entity class of function word
 */
@Entity
public class FunctionWord
{
	/*=========================================================================
	 *					Fields
	 *=========================================================================*/
	
	private String[] words;	// member of class to store in database
	

	/*=========================================================================
	 *					Constructors
	 *=========================================================================*/
	
	public FunctionWord(String[] words)
	{
		this.words=words;
	}
	
	/*=========================================================================
	 *					Getters
	 *=========================================================================*/
	
	/**
	 * @return the word
	 */
	public String[] getWords()
	{
		return words;
	}
	
	/*=========================================================================
	 *					Methods
	 *=========================================================================*/
}
