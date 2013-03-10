import javax.persistence.*;

/**
 * Decompressed matrix entity to store in database as pairs Document 1- Document 2- similarity
 */
@Entity
public class DecompressedMatrix
{
	/*=========================================================================
	 *					Fields
	 *=========================================================================*/

	String Repository;
	String Document_1;
	String Document_2;
	double Similarity;
	
	/*=========================================================================
	 *					Constructors
	 *=========================================================================*/
	
	/**
	 * @param repository Source of documents
	 * @param document_1 Document 1 
	 * @param document_2 Document 2
	 * @param sim Similarity between the two documents
	 */
	public DecompressedMatrix(String repository,String document_1, String document_2, double Similarity)
	{
		this.Repository = repository;
		Document_1 = document_1;
		Document_2 = document_2;
		this.Similarity = Similarity;
	}
	
	/*=========================================================================
	 *					Getters
	 *=========================================================================*/
	
	/**
	 * @return the document_1
	 */
	public String getDocument_1()
	{
		return Document_1;
	}

	/**
	 * @return the document_2
	 */
	public String getDocument_2()
	{
		return Document_2;
	}

	/**
	 * @return the sim
	 */
	public double getSimilarity()
	{
		return Similarity;
	}
	
	/**
	 * @param repository the repository to set
	 */
	public void setRepository(String repository)
	{
		Repository = repository;
	}
	
	/*=========================================================================
	 *					Methods
	 *=========================================================================*/
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "DecompressedMatrix [Document_1=" + Document_1 + ", Document_2="
				+ Document_2 + ", Similarity=" + Similarity + "]";
	}

}
