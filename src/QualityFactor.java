import javax.persistence.*;

/**
 * Object db entity to store the five similarity features and quality factor
 * between two web services , following the paper "Clustering WSDL Documents to Bootstrap the Discovery of Web Services"
 */
@Entity
public class QualityFactor
{
	/*=========================================================================
	 *					Fields
	 *=========================================================================*/

	String repository;
	String Document_1;
	String Document_2;
	double content_sim;
	double names_sim;
	double types_sim;
	double messages_sim;
	double ports_sim;
	double theta;
	
	/*=========================================================================
	 *					Constructors
	 *=========================================================================*/

	/**
	 * @param document_1
	 * @param document_2
	 * @param content_sim
	 * @param names_sim
	 * @param types_sim
	 * @param messages_sim
	 * @param ports_sim
	 */
	public QualityFactor(String repository,String document_1, String document_2,
			double content_sim, double names_sim, double types_sim,
			double messages_sim, double ports_sim,double theta)
	{
		this.repository = repository;
		Document_1 = document_1;
		Document_2 = document_2;
		this.content_sim = content_sim;
		this.names_sim = names_sim;
		this.types_sim = types_sim;
		this.messages_sim = messages_sim;
		this.ports_sim = ports_sim;
		this.theta=theta;
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
	 * @return the content_sim
	 */
	public double getContent_sim()
	{
		return content_sim;
	}
	/**
	 * @return the names_sim
	 */
	public double getNames_sim()
	{
		return names_sim;
	}
	/**
	 * @return the types_sim
	 */
	public double getTypes_sim()
	{
		return types_sim;
	}
	/**
	 * @return the messages_sim
	 */
	public double getMessages_sim()
	{
		return messages_sim;
	}
	/**
	 * @return the ports_sim
	 */
	public double getPorts_sim()
	{
		return ports_sim;
	}
	
	/**
	 * @return the theta
	 */
	public double getTheta()
	{
		return theta;
	}
	
	public String getRepository()
	{
		return repository;
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
		return "QualityFactor [Document_1=" + Document_1 + ", Document_2="
				+ Document_2 + ", content_sim=" + content_sim + ", names_sim="
				+ names_sim + ", types_sim=" + types_sim + ", messages_sim="
				+ messages_sim + ", ports_sim=" + ports_sim + ", theta="
				+ theta + "]";
	}
}
